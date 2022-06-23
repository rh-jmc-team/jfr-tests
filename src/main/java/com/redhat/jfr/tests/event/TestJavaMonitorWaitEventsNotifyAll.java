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

public class TestJavaMonitorWaitEventsNotifyAll {
    private static final int MILLIS = 50;
    static Helper helper = new Helper();


    public static void main(String args[]) throws Exception {
        long s0 = System.currentTimeMillis();
        run();
        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }

    public static void run() throws Exception {
        Runnable consumer = () -> {
            try {
                helper.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable producer = () -> {
            try {
                helper.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread tc1 = new Thread(consumer);
        Thread tp1 = new Thread(producer);
        Thread tp2 = new Thread(producer);


        tp1.start();
        tp2.start();
        //give the producers a headstart so they can start waiting
        Thread.sleep(100);
        tc1.start();

        tp1.join();
        tp2.join();
        tc1.join();
    }

    static class Helper {
        private int count = 2;
        private final int bufferSize = 2;

        public synchronized void produce() throws InterruptedException {
            for (int i = 0; i< 10; i++) {
                wait();
                System.out.println("produce");
                count++;
            }
        }

        public synchronized void consume() throws InterruptedException {
            for (int i = 0; i< 10; i++) {
                while (count < bufferSize) {
                    wait(MILLIS);
                }
                System.out.println("consume");
                count-=2;
                notifyAll(); //should wake up both producers
            }
        }
    }
}
