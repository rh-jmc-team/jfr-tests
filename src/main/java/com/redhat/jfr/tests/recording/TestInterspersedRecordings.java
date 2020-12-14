package com.redhat.jfr.tests.recording;

import com.redhat.jfr.events.StringEvent;
import jdk.jfr.Recording;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TestInterspersedRecordings {
    public static void main(String[] args) throws IOException {
        String nameOne = "One";
        Recording r1 = new Recording();
        Path destination = File.createTempFile(nameOne, ".jfr").toPath();
        r1.setDestination(destination);

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
        destination = File.createTempFile(nameTwo, ".jfr").toPath();
        r2.dump(destination);
        r2.close();
    }
}
