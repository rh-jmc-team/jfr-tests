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

// TODO: Add summary from GH in comment
public class TestDiverseEventsClean {
    private static final int COUNT = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();

        TestPeriodicEventClean.main(null);
        TestMultipleEventsClean.main(null);
        TestConcurrentEventsClean.main(null);
        TestEnabledAndRegisteredEventsClean.main(null);
        TestCategoryEventsClean.main(null);
        TestDataTypesEventClean.main(null);
        TestDynamicEventClean.main(null);
        TestCustomAnnotationsEventClean.main(null);
        TestInheretanceEventClean.main(null);
        TestThresholdEventClean.main(null);

        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }
}
