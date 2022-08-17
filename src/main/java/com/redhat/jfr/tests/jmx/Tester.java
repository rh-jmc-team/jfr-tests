package com.redhat.jfr.tests.jmx;

import jdk.management.jfr.FlightRecorderMXBean;

import javax.management.MBeanServerConnection;
import java.lang.management.ManagementFactory;


public class Tester {
    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = JmxUtils.getLocalMBeanServerConnectionStatic(args);
        System.out.println("made connection: "+ mbsc.toString());

        FlightRecorderMXBean flightRecorderMXBean = ManagementFactory.getPlatformMXBean(mbsc, FlightRecorderMXBean.class);
        long recording = flightRecorderMXBean.newRecording();
        System.out.println("Starting recording");
        flightRecorderMXBean.startRecording(recording);
        Thread.sleep(1000);
        flightRecorderMXBean.stopRecording(recording);
    }

}
