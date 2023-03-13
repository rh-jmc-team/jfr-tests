package com.redhat.jfr.tests.event;

import com.redhat.jfr.utils.Stressor;
import jdk.jfr.consumer.RecordingStream;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

public class TestStreamingRecordingStream_flushCallback {
    private static final int THREADS = 3;
    private static final int MILLIS = 60;
    static boolean dumped = false;
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
            rs.enable("jdk.JavaMonitorEnter").withThreshold(Duration.ofMillis(10));
            rs.onEvent("jdk.JavaMonitorEnter", event -> {
                System.out.println(event.getDuration("duration"));
                System.out.println(event.getThread("eventThread").getJavaName());
                System.out.println(event.getThread("previousOwner").getJavaName());
                System.out.println(event.getClass("monitorClass").getName());
            });

            Runnable dumpRunnable = () -> {
                try {
                    //only dump after first flush
                    if (!dumped){
//                    rs.dump(Path.of("/home/rtoyonag/IdeaProjects/graal/substratevm/stream_dump_1_flush.jfr"));
                        Thread.sleep(3000);
                        Stressor.execute(threadCount, eh, r);
                        dumped = true;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            };
            rs.onFlush(dumpRunnable);
            rs.startAsync(); //starts the actual recording we created

            Stressor.execute(threadCount, eh, r);
            System.out.println("Sleeping now...");
            Thread.sleep(8000);
            rs.dump(Path.of("/home/rtoyonag/IdeaProjects/graal/substratevm/stream_dump_manyflush.jfr")); //this snapshot should work
            rs.awaitTermination(Duration.ofSeconds(5));


        }
    }
}
