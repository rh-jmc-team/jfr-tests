package com.redhat.jfr.tests.event;


import com.redhat.jfr.utils.Stressor;

/*
* The result of this test should be THREADS threads acquiring the lock one by one.
* The wait times should be cumulative.
* So the first thread should acquire the lock immediately, the second thread after MILLIS time and the third thread after 2*MILLIS time.*/
public class TestJavaMonitorEnterEventsContention {
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
        Stressor.execute(threadCount, eh, r);
    }
}
