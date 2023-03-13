/*
 * Copyright (c) 2022, Red Hat, Inc.
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

package com.redhat.jfr.tests.jmx;

import jdk.management.jfr.FlightRecorderMXBean;

import javax.management.MBeanServerConnection;
import java.lang.management.ManagementFactory;


public class Tester {
    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = JmxUtils.getLocalMBeanServerConnectionStatic(args);
        System.out.println("made connection: "+ mbsc.toString());

        FlightRecorderMXBean flightRecorderMXBean = ManagementFactory.getPlatformMXBean(mbsc, FlightRecorderMXBean.class);
        long recording = flightRecorderMXBean.newRecording();
        System.out.println("Starting recording");
        flightRecorderMXBean.startRecording(recording);
        flightRecorderMXBean.stopRecording(recording);
    }

}
