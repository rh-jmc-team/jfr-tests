package com.redhat.jfr.tests.recording;

import com.redhat.jfr.events.StringEvent;
import jdk.jfr.Recording;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TestConsecutiveRecordings {
    public static void main(String[] args) throws IOException {
        String name = "One";
        Recording r = new Recording();
        Path destination = File.createTempFile(name, ".jfr").toPath();
        r.setDestination(destination);

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
        destination = File.createTempFile(name, ".jfr").toPath();
        r.dump(destination);
        r.close();
    }
}
