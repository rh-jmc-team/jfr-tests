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

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;

public class JfrNotificationTester {
    private static Listener listener = new Listener();

    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = JmxUtils.getLocalMBeanServerConnectionStatic(args);
        System.out.println("Made connection: " + mbsc.toString());
        NotificationFilterSupport filter = new NotificationFilterSupport();
        filter.enableType("jmx.attribute.change");
        mbsc.addNotificationListener(new ObjectName("jdk.management.jfr:type=FlightRecorder"),listener, filter, null);

        //Trigger a bunch of notifications
        FlightRecorderMXBean flightRecorderMXBean = ManagementFactory.getPlatformMXBean(mbsc, FlightRecorderMXBean.class);
        long recording = flightRecorderMXBean.newRecording();
        flightRecorderMXBean.startRecording(recording);

        // enable events
        var settings = new HashMap<String,String>();
        settings.put("jdk.JavaMonitorEnter#enabled","true");
        flightRecorderMXBean.setRecordingSettings(recording, settings);

        Thread.sleep(3000);
        flightRecorderMXBean.stopRecording(recording);

    }
}
final class Listener implements NotificationListener {
    public void handleNotification(Notification notif, Object handback)
    {
        System.out.println("notified!");
    }
}