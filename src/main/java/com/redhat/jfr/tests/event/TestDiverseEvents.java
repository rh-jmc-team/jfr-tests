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
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */
     
package com.redhat.jfr.tests.event;

import com.redhat.jfr.events.StringEvent;
import com.redhat.jfr.events.TimestampedEvent;
import com.redhat.jfr.events.annotated.EnabledEvent;
import com.redhat.jfr.events.annotated.RegisteredEvent;
import com.redhat.jfr.events.annotated.UnregisteredEvent;
import com.redhat.jfr.utils.JFR;
import com.redhat.jfr.utils.LocalJFR;
import com.redhat.jfr.utils.Stressor;

public class TestDiverseEvents {
    private static final int COUNT = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();

        // TestMultipleEvents
        for (int i = 0; i < COUNT; i++) {
            StringEvent event = new StringEvent();
            event.message = "StringEvent has been generated as part of TestMultipleEvents!";
            event.commit();
        }

        // TestConcurrentEvents
        int threadCount = 8;
        Runnable r = () -> {
            for (int i = 0; i < COUNT; i++) {
                StringEvent event = new StringEvent();
                event.message = "StringEvent has been generated as part of TestConcurrentEvents!";
                event.commit();
            }
        };
        Thread.UncaughtExceptionHandler eh = (t, e) -> e.printStackTrace();
        Stressor.execute(threadCount, eh, r);

        // TimestampedEvent
        TimestampedEvent timestampedEvent = new TimestampedEvent();
        timestampedEvent.message = "TimestampedEvent has been generated!";
        timestampedEvent.commit();

        // EnabledEvent (@Enabled(false))
        new EnabledEvent().commit();

        // RegisteredEvent (@Enabled(false))
        new RegisteredEvent().commit();

        // UnregisteredEvent (@Enabled(false))
        new UnregisteredEvent().commit();


        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }
}
