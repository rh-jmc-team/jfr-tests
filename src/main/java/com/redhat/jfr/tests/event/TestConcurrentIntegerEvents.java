package com.redhat.jfr.tests.event;

import com.redhat.jfr.events.IntegerEvent;
import com.redhat.jfr.utils.JFR;
import com.redhat.jfr.utils.LocalJFR;
import com.redhat.jfr.utils.Stressor;

import java.io.File;

public class TestConcurrentIntegerEvents {

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();
        JFR jfr = new LocalJFR();
        int threadCount = 8;
        long id = jfr.startRecording("TestConcurrentIntegerEvents");

        Runnable r = () -> {
            int count = 1024 * 1024;
            for (int i = 0; i < count; i++) {
                IntegerEvent integerEvent = new IntegerEvent();
                integerEvent.number = Integer.MAX_VALUE;
                integerEvent.commit();
            }
        };
        Thread.UncaughtExceptionHandler eh = (t, e) -> e.printStackTrace();
        Stressor.execute(threadCount, eh, r);

        File recording = jfr.endRecording(id);

        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
        System.err.println("jfr recording: " + recording);
    }
}
