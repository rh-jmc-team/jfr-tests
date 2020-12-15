package com.redhat.jfr.tests.recording;

import com.redhat.jfr.events.StringEvent;
import jdk.jfr.Recording;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TestConsecutiveRecordings {
    public static void main(String[] args) throws IOException {
        long s0 = System.currentTimeMillis();
        String name = "One";
        Recording r = new Recording();
        Path destination1 = File.createTempFile(name, ".jfr").toPath();
        r.setDestination(destination1);

        r.start();
        for (int i = 0; i < 2; i++) {
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }
        r.stop();
        r.close();

        name = "Two";
        r = new Recording();
        r.start();
        for (int i = 0; i < 2; i++) {
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }
        r.stop();
        Path destination2 = File.createTempFile(name, ".jfr").toPath();
        r.dump(destination2);
        r.close();

        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
        System.err.println("jfr recording: " + destination1);
        System.err.println("jfr recording: " + destination2);
    }
}
