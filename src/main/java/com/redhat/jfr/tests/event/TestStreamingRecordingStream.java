//package com.redhat.jfr.tests.event;
//
//import com.redhat.jfr.utils.Stressor;
//import jdk.jfr.consumer.RecordingStream;
//
//import java.nio.file.Path;
//import java.time.Duration;
//
//public class TestStreamingRecordingStream {
//    private static final int THREADS = 10;
//    private static final int MILLIS = 60;
//
//    static Object monitor = new Object();
//    public static void main(String args[]) throws Exception {
//        long s0 = System.currentTimeMillis();
//        run();
//        long d0 = System.currentTimeMillis() - s0;
//        System.out.println("elapsed:" + d0);
//    }
//    private static void doWork(Object obj) throws InterruptedException {
//        synchronized(obj){
//            System.out.println("Doing work");
//            Thread.sleep(MILLIS);
//        }
//
//    }
//    public static void run() throws Exception {
//        int threadCount = THREADS;
//        Runnable r = () -> {
//            // create contention between threads for one lock
//            try {
//                doWork(monitor);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//        };
//        Thread.UncaughtExceptionHandler eh = (t, e) -> e.printStackTrace();
//        try (var rs = new RecordingStream()) {
//            rs.enable("jdk.JavaMonitorEnter").withThreshold(Duration.ofMillis(10));
//            rs.onEvent("jdk.JavaMonitorEnter", event -> {
//                System.out.println(event.getDuration("duration"));
////                System.out.println(event.getThread("previousOwner"));
//            });
//
//            rs.startAsync();
//            //try dumping right away
////            rs.dump(Path.of("/home/rtoyonag/IdeaProjects/graal/substratevm/stream_dump.txt")); //dumping somehow makes it work?
//
//            Stressor.execute(threadCount, eh, r);
//            rs.awaitTermination(Duration.ofSeconds(5));
////            System.out.println("starting dump.");
//            rs.dump(Path.of("/home/rtoyonag/IdeaProjects/graal/substratevm/stream_dump.txt")); //dumping somehow makes it work?
////            rs.dump(Path.of("/home/rtoyonag/stream_dump.txt")); //dumping somehow makes it work?
//
////            while(true);
//        }
//    }
//}
