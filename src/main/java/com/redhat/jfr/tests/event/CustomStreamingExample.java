package com.redhat.jfr.tests.event;

import com.redhat.jfr.events.StringEvent;
import com.redhat.jfr.utils.Stressor;
import jdk.jfr.consumer.RecordingStream;

import java.time.Duration;

public class CustomStreamingExample {
    private static final int THREADS = 10;
    private static final int MILLIS = 60;
    static Object monitor = new Object();
    public static void main(String args[]) throws Exception {
        long s0 = System.currentTimeMillis();
        run();
        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }
    private static void doWork(Object obj) throws InterruptedException {
        synchronized(obj){
            System.out.println("Doing work");
            Thread.sleep(MILLIS);
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }

    }
    public static void run() throws Exception {
        int threadCount = THREADS;
        Runnable r = () -> {
            // create contention between threads for one lock
            try {
                doWork(monitor);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        };
        Thread.UncaughtExceptionHandler eh = (t, e) -> e.printStackTrace();
        try (var rs = new RecordingStream()) { //creates new recording too
            rs.enable("com.redhat.String");
            rs.onEvent("com.redhat.String", event -> {
                System.out.println("Stacktrace: "+event.getStackTrace());
            });

            rs.startAsync(); //starts the actual recording we created

            Stressor.execute(threadCount, eh, r);

            rs.awaitTermination(Duration.ofSeconds(5));


        }
    }
}
