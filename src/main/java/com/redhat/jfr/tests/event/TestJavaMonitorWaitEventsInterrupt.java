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

/*
 * This program should test whether JavaMonitorWait events are emitted correctly.
 * Specifically, it tests whether notifyAll() results in emitting the correct events for all waiters.
 * You should observe that one notifier thread TID wakes up 2 waiters when checked in JMC periodically.
 * With a period defined by MILLIS.
 */
public class TestJavaMonitorWaitEventsInterrupt {
    private static final int MILLIS = 50;
    static Helper helper = new Helper();


    public static void main(String args[]) throws Exception {
        long s0 = System.currentTimeMillis();
        run();
        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }

    public static void run() throws Exception {
        Runnable interrupter = () -> {
            try {
                helper.interrupt();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable interrupted = () -> {
            try {
                helper.interrupted();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable simpleWaiter = () -> {
            try {
                helper.simpleWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable simpleNotifier = () -> {
            try {
                helper.simpleNotify();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread interrupterThread = new Thread(interrupter);
        Thread interruptedThread = new Thread(interrupted);
        helper.interrupted = interruptedThread;


        interruptedThread.start();
        Thread.sleep(50); //pause to ensure expected ordering of lock acquisition
        interrupterThread.start();

        interruptedThread.join();
        interrupterThread.join();

        Thread tw = new Thread(simpleWaiter);
        Thread tn = new Thread(simpleNotifier);


        tw.start();
        Thread.sleep(50);
        tn.start();

        tw.join();
        tn.join();
    }

    static class Helper {
        public Thread interrupted;
        public synchronized void interrupted() throws InterruptedException {
            wait( );
        }

        public synchronized void interrupt() throws InterruptedException {
//            notify();
            interrupted.interrupt();
            System.out.println("notify and interrupt");
            System.out.println(Thread.currentThread().getName());
        }

        public synchronized void simpleWait() throws InterruptedException {
            wait();
        }
        public synchronized void simpleNotify() throws InterruptedException {
            Thread.sleep(100);
            notify();
            System.out.println("simple notify");
            System.out.println(Thread.currentThread().getName());
        }
    }
}
