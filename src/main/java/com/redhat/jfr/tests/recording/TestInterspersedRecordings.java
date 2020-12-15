package com.redhat.jfr.tests.recording;

import com.redhat.jfr.events.StringEvent;
import jdk.jfr.Recording;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TestInterspersedRecordings {
    public static void main(String[] args) throws IOException {
        long s0 = System.currentTimeMillis();
        String nameOne = "One";
        Recording r1 = new Recording();
        Path destination1 = File.createTempFile(nameOne, ".jfr").toPath();
        r1.setDestination(destination1);

        String nameTwo = "Two";
        Recording r2 = new Recording();

        r1.start();
        r2.start();

        for (int i = 0; i < 2; i++) {
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }

        r1.stop();
        r1.close();

        for (int i = 0; i < 2; i++) {
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }

        r2.stop();
        Path destination2 = File.createTempFile(nameTwo, ".jfr").toPath();
        r2.dump(destination2);
        r2.close();

        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
        System.err.println("jfr recording: " + destination1);
        System.err.println("jfr recording: " + destination2);
    }
}
