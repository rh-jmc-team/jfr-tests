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
import com.redhat.jfr.events.InheritanceEvent;
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


// PeriodicEvent - Registers a periodic event that is emitted at the beginning of every chunk.
//
// TestMultipleEvents - Event emitting code from TestMultipleEvents.
//
// TestConcurrentEvents - Event emitting code from TestConcurrentEvents.
//
// EnabledEvent - Emits a disabled event.
//
// RegisteredEvent - Emits a registered, disabled event.
//
// UnregisteredEvent - Emits an unregistered, disabled event.
//
// CategoryEvents - Emits timed and overlapping events with categories.
//
// DataTypesEvent - Emits an event with all possible data types and data type annotations.
//
// DynamicEvent1 - Creates and emits a dynamic event.
//
// CustomAnnotationsEvent - Emits an event with custom annotations. Also has a private field.
//
// InheritanceEvent - Emits an event that extends CustomAnnotationsEvent. Also has stack trace enabled and a transient field, just to test those without making seperate events.
//
// ThresholdEvent - Emits an event with a threshold: once without meeting the threshold, and once meeting it.

public class TestDiverseEventsClean {
    private static final int COUNT = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();

        TestPeriodicEventClean.run();
        TestMultipleEventsClean.run();
        TestConcurrentEventsClean.run();
        TestEnabledAndRegisteredEventsClean.run();
        TestCategoryEventsClean.run();
        TestDataTypesEventClean.run();
        TestDynamicEventClean.run();
        TestCustomAnnotationsEventClean.run();
        TestInheritanceEventClean.run();
        TestThresholdEventClean.run();

        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }
}
