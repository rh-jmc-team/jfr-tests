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



/*
This is to test the thread sleep event emission by virtual threads. You need to compile with javac and use jre 19+.
You'll need <compilerArgs>--enable-preview</compilerArgs> added to your pom.xml as well.
This test is commented out so it doesn't affect the compilation of the other tests (which you don't need jdk 19+ for) when you build with mvn.
To run you also need the --enable-preview flag included.
 */

/*
package com.redhat.jfr.tests.event;


public class TestThreadSleepVirtual {
    private static final int MILLIS = 50;


    public static void main(String args[]) throws Exception {
        long s0 = System.currentTimeMillis();
        run();
        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }

    public static void run() throws Exception {
        var vThread = Thread.startVirtualThread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(MILLIS);
                    System.out.println("Hello from the virtual thread");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        vThread.join();
    }


}

 */
