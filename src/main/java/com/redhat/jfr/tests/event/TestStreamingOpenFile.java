package com.redhat.jfr.tests.event;

import java.nio.file.Path;

import com.redhat.jfr.utils.Stressor;
import jdk.jfr.consumer.EventStream;

public class TestStreamingOpenFile {
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
            Thread.sleep(MILLIS);
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
//        try (var rs = new RecordingStream()) {
        try (var es = EventStream.openFile(Path.of("/home/rtoyonag/repos/jfr-tests/TestStreaming.jfr"))) {
            es.onEvent("jdk.JavaMonitorEnter", event -> {
//                System.out.println(event.getClass("monitorClass"));
                System.out.println(event.getDuration("duration"));
                System.out.println(event.getThread("previousOwner"));
            });


//            rs.startAsync();
            Stressor.execute(threadCount, eh, r);
//            rs.awaitTermination(Duration.ofSeconds(10));
            es.startAsync();
            es.awaitTermination();

        }
    }
}
