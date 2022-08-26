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

package com.redhat.jfr.tests.event;

import java.util.concurrent.locks.LockSupport;

public class TestThreadParkEvent {
    static class Blocker {
    }
    static final Blocker blocker = new Blocker();
    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();
        LockSupport.parkNanos(blocker, 2000*1000000);
        LockSupport.parkNanos(1000*1000000);
        LockSupport.parkUntil(System.currentTimeMillis() + 1000);
        Thread mainThread = Thread.currentThread();
        Runnable unparker = () -> {
            try {
                Thread.sleep(3000);
                LockSupport.unpark(mainThread);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };


        Thread unparkerThread = new Thread(unparker);
        unparkerThread.start();
        LockSupport.park();
        unparkerThread.join();

        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }
}
