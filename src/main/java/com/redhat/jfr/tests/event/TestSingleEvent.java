/*
 * Copyright (c) 2020, Red Hat, Inc.
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

import com.redhat.jfr.events.StringEvent;
import com.redhat.jfr.utils.JFR;
import com.redhat.jfr.utils.LocalJFR;

import java.io.File;

public class TestSingleEvent {

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();
        JFR jfr = new LocalJFR();
        long id = jfr.startRecording("TestSingleEvent");

        StringEvent event = new StringEvent();
        event.message = "Event has been generated!";
        event.commit();

        File recording = jfr.endRecording(id);
        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
        System.err.println("jfr recording: " + recording);
    }
}
