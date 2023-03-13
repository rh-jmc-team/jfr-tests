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


import com.redhat.jfr.events.StringEvent;
import com.redhat.jfr.utils.Stressor;

/*
* The result of this test should be THREADS threads acquiring the lock one by one.
* The wait times should be cumulative.
* So the first thread should acquire the lock immediately, the second thread after MILLIS time and the third thread after 2*MILLIS time.
*/
public class JfrTestServer {
    private static final int THREADS = 10;
    private static final int MILLIS = 60;

    static Object monitor = new Object();
    public static void main(String args[]) throws Exception {
        while(true) {
            Thread.sleep(500);
            run();
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }
    }
    private static void doWork(Object obj) throws InterruptedException {
        synchronized(obj){
            Thread.sleep(MILLIS);
        }

    }
    public static void run() throws Exception {
        int threadCount = THREADS;
        Runnable r = () -> {
            // create contention between threads for one lock
            try {
                doWork(monitor);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        };
        Thread.UncaughtExceptionHandler eh = (t, e) -> e.printStackTrace();
        Stressor.execute(threadCount, eh, r);
    }
}
