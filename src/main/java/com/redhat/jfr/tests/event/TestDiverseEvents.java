package com.redhat.jfr.tests.event;

import com.redhat.jfr.events.StringEvent;
import com.redhat.jfr.utils.JFR;
import com.redhat.jfr.utils.LocalJFR;
import com.redhat.jfr.utils.Stressor;

public class TestDiverseEvents {
    private static final int COUNT = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();

        // TestMultipleEvents
        for (int i = 0; i < COUNT; i++) {
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }


        // TestConcurrentEvents
        int threadCount = 8;
        Runnable r = () -> {
            for (int i = 0; i < COUNT; i++) {
                StringEvent event = new StringEvent();
                event.message = "Event has been generated!";
                event.commit();
            }
        };
        Thread.UncaughtExceptionHandler eh = (t, e) -> e.printStackTrace();
        Stressor.execute(threadCount, eh, r);


        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }
}
