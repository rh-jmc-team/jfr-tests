package com.redhat.jfr.tests.event;

import com.redhat.jfr.events.ClassEvent;
import com.redhat.jfr.events.StringEvent;
import com.redhat.jfr.utils.JFR;
import com.redhat.jfr.utils.LocalJFR;

import java.io.File;

public class TestMultipleEvents {

    public static void main(String[] args) throws Exception {
        long s0 = System.currentTimeMillis();
        JFR jfr = new LocalJFR();
        long id = jfr.startRecording("TestMultipleEvents");

        int count = 512 * 1024;


        for (int i = 0; i < count; i++) {
            StringEvent stringEvent = new StringEvent();
            stringEvent.message = "Event has been generated!";
            stringEvent.commit();

            ClassEvent classEvent = new ClassEvent();
            classEvent.klass = TestMultipleEvents.class;
            classEvent.commit();
        }

        File recording = jfr.endRecording(id);

        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
        System.err.println("jfr recording: " + recording);
    }
}
