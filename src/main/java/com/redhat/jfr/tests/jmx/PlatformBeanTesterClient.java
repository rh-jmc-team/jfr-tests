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


import javax.management.MBeanServerConnection;
import java.lang.management.*;

/**
 * This is a JMX client that tests several platform MXBeans.
 */

public class PlatformBeanTesterClient {

    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = JmxUtils.getLocalMBeanServerConnectionStatic(args);
        System.out.println("Made connection: " + mbsc.toString());

        System.out.println("----RuntimeMXBean----");
        RuntimeMXBean runtimeMXBean = ManagementFactory.getPlatformMXBean(mbsc, RuntimeMXBean.class);
        System.out.println("Uptime: "+ runtimeMXBean.getUptime());
        System.out.println("PID: "+ runtimeMXBean.getPid());
        System.out.println("Class Path: "+ runtimeMXBean.getClassPath());
        System.out.println("Start Time: "+ runtimeMXBean.getStartTime());

        System.out.println("----ClassLoadingMXBean----");
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getPlatformMXBean(mbsc, ClassLoadingMXBean.class);
        System.out.println("Loaded Class count (hardcoded at 0): "+classLoadingMXBean.getLoadedClassCount());

        System.out.println("----ThreadMXBean----");
        ThreadMXBean threadMXBean = ManagementFactory.getPlatformMXBean(mbsc, ThreadMXBean.class);
        System.out.println("Thread CPU time supported: "+ threadMXBean.isThreadCpuTimeSupported());
        System.out.println("Peak thread count: "+ threadMXBean.getPeakThreadCount());
        System.out.println("Reset peak thread count...");
        threadMXBean.resetPeakThreadCount();
        System.out.println("Peak thread count: "+ threadMXBean.getPeakThreadCount());
        System.out.println("Current thread user time: "+ threadMXBean.getCurrentThreadUserTime());

        System.out.println("----HeapImplMemoryMXBean----");
        MemoryMXBean memoryMXBean = ManagementFactory.getPlatformMXBean(mbsc, MemoryMXBean.class);
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        System.out.println("Memory usage: " + memoryUsage.getUsed());
        System.out.println("Max memory usage: " + memoryUsage.getMax());


        //TODO: add GarbageCollectorMXbean too
    }
}
