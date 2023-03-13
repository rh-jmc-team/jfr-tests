
/*
This is to test the thread sleep event emission by virtual threads. You need to compile with javac and use jre 19+.
You'll need <compilerArgs>--enable-preview</compilerArgs> added to your pom.xml as well.
This test is commented out so it doesn't affect the compilation of the other tests (which you don't need jdk 19+ for) when you build with mvn.
To run you also need the --enable-preview flag included.

ie  /home/rtoyonag/IdeaProjects/jdk19/build/linux-x86_64-server-release/images/jdk/bin/java --enable-preview  -cp target/classes com.redhat.jfr.tests.event.TestThreadParkVirtual
 */


package com.redhat.jfr.tests.event;


import java.util.concurrent.locks.LockSupport;

public class TestThreadParkVirtual {


    public static void main(String args[]) throws Exception {
        long s0 = System.currentTimeMillis();
        run();
        long d0 = System.currentTimeMillis() - s0;
        System.out.println("elapsed:" + d0);
    }

    public static void run() throws Exception{
        var vThread = Thread.startVirtualThread(() -> {
                LockSupport.parkNanos(1000*1000000);
            System.out.println(Thread.currentThread().toString());
        });

        var vThread2 = Thread.startVirtualThread(() -> {
                LockSupport.parkNanos(1000*1000000);
                System.out.println(Thread.currentThread().toString());
        });
        var vThread3 = Thread.startVirtualThread(() -> {
            LockSupport.parkNanos(1000*1000000);
            System.out.println(Thread.currentThread().toString());
        });
        var vThread4 = Thread.startVirtualThread(() -> {
            LockSupport.parkNanos(1000*1000000);
            System.out.println(Thread.currentThread().toString());
        });

        vThread.join();
        vThread2.join();
        vThread3.join();
        vThread4.join();
    }
}


