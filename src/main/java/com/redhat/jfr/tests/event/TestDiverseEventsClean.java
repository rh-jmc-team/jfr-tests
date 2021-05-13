/*
 * Copyright (c) 2021, Red Hat, Inc.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of the Red Hat GraalVM Testing Suite (the suite).
 *
 * The suite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * The suite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the suite.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.redhat.jfr.tests.event;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import jdk.jfr.Event;
import jdk.jfr.EventFactory;
import jdk.jfr.Name;
import jdk.jfr.Label;
import jdk.jfr.Description;
import jdk.jfr.EventFactory;
import jdk.jfr.Period;
import jdk.jfr.Unsigned;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.AnnotationElement;
import jdk.jfr.FlightRecorder;

import com.redhat.jfr.events.StringEvent;
import com.redhat.jfr.events.InheretanceEvent;
import com.redhat.jfr.events.DataTypesEvent;
import com.redhat.jfr.events.annotated.EnabledEvent;
import com.redhat.jfr.events.annotated.RegisteredEvent;
import com.redhat.jfr.events.annotated.UnregisteredEvent;
import com.redhat.jfr.events.annotated.CustomAnnotationsEvent;
import com.redhat.jfr.events.annotated.ThresholdEvent;
import com.redhat.jfr.events.annotated.PeriodicEvent;
import com.redhat.jfr.utils.JFR;
import com.redhat.jfr.utils.LocalJFR;
import com.redhat.jfr.utils.Stressor;

import static com.redhat.jfr.events.annotated.CategoryEvents.AlphaEvent;
import static com.redhat.jfr.events.annotated.CategoryEvents.BetaEvent;
import static com.redhat.jfr.events.annotated.CategoryEvents.AlphaBetaEvent;

public class TestDiverseEventsClean {
    private static final int COUNT = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();

        // PeriodicEvent @Period("beginChunk")
        Runnable hook = () -> {
            PeriodicEvent event = new PeriodicEvent();
            event.time = System.currentTimeMillis();
            event.commit();
        };
        FlightRecorder.addPeriodicEvent(PeriodicEvent.class, hook);

        // TestMultipleEvents
        for (int i = 0; i < COUNT; i++) {
            StringEvent event = new StringEvent();
            event.message = "StringEvent has been generated as part of TestMultipleEvents.";
            event.commit();
        }

        // TestConcurrentEvents
        int threadCount = 8;
        Runnable r = () -> {
            for (int i = 0; i < COUNT; i++) {
                StringEvent event = new StringEvent();
                event.message = "StringEvent has been generated as part of TestConcurrentEvents.";
                event.commit();
            }
        };
        Thread.UncaughtExceptionHandler eh = (t, e) -> e.printStackTrace();
        Stressor.execute(threadCount, eh, r);

        // EnabledEvent @Enabled(false)
        new EnabledEvent().commit();

        // RegisteredEvent @Enabled(false)
        new RegisteredEvent().commit();

        // UnregisteredEvent @Enabled(false)
        new UnregisteredEvent().commit();

        // CategoryEvents
        //   * AlphaEvent     @Category("Alpha")           @Name("AlphaEvent")
        //   * BetaEvent      @Category("Beta")            @Name("BetaEvent")
        //   * AlphaBetaEvent @Category({"Alpha", "Beta"}) @Name("AlphaBetaEvent")
        // -----------------------------------------------------------
        //   |            alphaEvent                 |
        // -----------------------------------------------------------
        //   | betaEvent1 |          |          betaEvent2         |
        // -----------------------------------------------------------
        //   |     alphaBetaEvent    |
        // -----------------------------------------------------------
        AlphaEvent alphaEvent = new AlphaEvent();
        BetaEvent betaEvent1 = new BetaEvent();
        BetaEvent betaEvent2 = new BetaEvent();
        AlphaBetaEvent alphaBetaEvent = new AlphaBetaEvent();
        alphaEvent.begin();
        betaEvent1.begin();
        alphaBetaEvent.begin();
        Thread.sleep(100);
        betaEvent1.commit();
        Thread.sleep(100);
        betaEvent2.begin();
        alphaBetaEvent.commit();
        Thread.sleep(100);
        alphaEvent.commit();
        Thread.sleep(100);
        betaEvent2.commit();

        // DataTypesEvent
        DataTypesEvent dataTypesEvent = new DataTypesEvent();
        dataTypesEvent.classValue1 = Math.class;
        dataTypesEvent.threadValue1 = Thread.currentThread();
        dataTypesEvent.stringValue1 = "Hello, World!";
        dataTypesEvent.classValue2 = null;
        dataTypesEvent.threadValue2 = null;
        dataTypesEvent.stringValue2 = null;
        dataTypesEvent.byteValue = (byte) 1;
        dataTypesEvent.shortValue = (short) 0111;
        dataTypesEvent.intValue = 0x111;
        dataTypesEvent.longValue = Long.MAX_VALUE;
        dataTypesEvent.floatValue = Float.MIN_VALUE;
        dataTypesEvent.doubleValue = Math.PI;
        dataTypesEvent.characterValue = 'C';
        dataTypesEvent.booleanValue = true;
        dataTypesEvent.booleanFlagValue = false;
        dataTypesEvent.dataAmountValue = -1;
        dataTypesEvent.frequencyValue = 1;
        dataTypesEvent.memoryAddressValue = 1;
        dataTypesEvent.percentageValue = 101;
        dataTypesEvent.timespanValue = 1;
        dataTypesEvent.timestampValue = System.currentTimeMillis();
        dataTypesEvent.unsignedValue = -5;
        dataTypesEvent.commit();

        // Dynamic event
        // Can be found by jfr print --events DynamicEvent1
        List<AnnotationElement> eventAnnotations = new ArrayList<>();
        eventAnnotations.add(new AnnotationElement(Label.class, "Dynamic Event"));
        eventAnnotations.add(new AnnotationElement(Description.class, "This is a dynamically created event"));
        List<AnnotationElement> fooFieldAnnotations = new ArrayList<>();
        fooFieldAnnotations.add(new AnnotationElement(Label.class, "Foo"));
        fooFieldAnnotations.add(new AnnotationElement(Description.class, "This is a dynamically created field"));
        fooFieldAnnotations.add(new AnnotationElement(Unsigned.class));
        List<ValueDescriptor> eventFields =
            Collections.singletonList(new ValueDescriptor(int.class, "foo", fooFieldAnnotations));
        EventFactory ef = EventFactory.create(eventAnnotations, eventFields);
        Event dynamicEvent = ef.newEvent();
        dynamicEvent.set(0, 123);
        dynamicEvent.commit();

        // CustomAnnotationsEvent
        CustomAnnotationsEvent cae = new CustomAnnotationsEvent();
        cae.barAnnotatedField = "BARRR";
        cae.commit();

        // InheretanceEvent
        InheretanceEvent ie = new InheretanceEvent();
        ie.barAnnotatedField = "BAZZZ";
        ie.commit();

        // ThresholdEvent
        ThresholdEvent meetsThreshold = new ThresholdEvent();
        ThresholdEvent doesNotMeetThreshold = new ThresholdEvent();
        meetsThreshold.begin();
        doesNotMeetThreshold.begin();
        Thread.sleep(30);
        doesNotMeetThreshold.commit();
        Thread.sleep(30);
        meetsThreshold.commit();

        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }
}
