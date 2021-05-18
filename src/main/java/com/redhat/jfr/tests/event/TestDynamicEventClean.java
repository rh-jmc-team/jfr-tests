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
import jdk.jfr.Label;
import jdk.jfr.Description;
import jdk.jfr.Unsigned;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.AnnotationElement;

// Can be found by jfr print --events DynamicEvent1
public class TestDynamicEventClean {

    public static void main(String args[]) {
        long s0 = System.currentTimeMillis();
        run();
        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }

    public static void run() {
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
    }
}
