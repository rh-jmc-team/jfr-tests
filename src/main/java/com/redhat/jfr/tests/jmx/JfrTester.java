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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.management.ManagementFactory;
import java.util.HashMap;

/**
 * This is a JMX client. It is almost identical to JfrTesterInvoke, except it invoke operations directly on the
 * FlightRecorderMXBean instead of through the MBeanServerConnection reflectively.
 */
public class JfrTester {
    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = JmxUtils.getLocalMBeanServerConnectionStatic(args);
        System.out.println("made connection: " + mbsc.toString());

        FlightRecorderMXBean flightRecorderMXBean = ManagementFactory.getPlatformMXBean(mbsc, FlightRecorderMXBean.class);
        long recording = flightRecorderMXBean.newRecording();
        flightRecorderMXBean.startRecording(recording);

        // enable events
        var settings = new HashMap<String,String>();
        settings.put("jdk.JavaMonitorEnter#enabled","true");
        flightRecorderMXBean.setRecordingSettings(recording, settings);

        Thread.sleep(3000);
        flightRecorderMXBean.stopRecording(recording);

        System.out.println("stopped");
        long streamId = flightRecorderMXBean.openStream(recording,null);
        System.out.println("opened");
        File f = new File("stream_" + recording + ".jfr");
        try (var fos = new FileOutputStream(f); var bos = new BufferedOutputStream(fos)) {
            System.out.println("reading");

            byte[] buff;
            while (true)
            {
                buff = flightRecorderMXBean.readStream(streamId);
                if (buff != null)
                {
                    fos.write(buff);
                } else {
                    break;
                }
            }
            flightRecorderMXBean.closeStream(streamId);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            System.out.println("FAILED: "+ e);
        }
    }
}
