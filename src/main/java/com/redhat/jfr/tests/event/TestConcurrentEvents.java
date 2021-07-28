package com.redhat.jfr.tests.event;

import com.redhat.jfr.events.StringEvent;
import com.redhat.jfr.events.IntegerEvent;
import com.redhat.jfr.events.ClassEvent;
import com.redhat.jfr.utils.JFR;
import com.redhat.jfr.utils.LocalJFR;
import com.redhat.jfr.utils.Stressor;

import java.io.File;

public class TestConcurrentEvents {

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();
        JFR jfr = new LocalJFR();
        // threadCount set to # of cores of ibm-x3650m4-01-vm-05.ibm2.lab.eng.bos.redhat.com
        int threadCount = 2;
        long id = jfr.startRecording("TestConcurrentEvents");

        Runnable r = () -> {
            int count = 1024 * 1024;
            for (int i = 0; i < count; i++) {
                StringEvent stringEvent = new StringEvent();
                stringEvent.message = "Event has been generated!";
                stringEvent.commit();

                IntegerEvent integerEvent = new IntegerEvent();
                integerEvent.number = Integer.MAX_VALUE;
                integerEvent.commit();

                ClassEvent classEvent = new ClassEvent();
                classEvent.clazz = Math.class;
                classEvent.commit();
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
