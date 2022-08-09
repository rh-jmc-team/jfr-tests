package com.redhat.jfr.tests.jmx;

import jdk.management.jfr.FlightRecorderMXBean;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

public class Tester {
    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = getLocalMBeanServerConnectionStatic(args);
        System.out.println("made connection: "+ mbsc.toString());

        FlightRecorderMXBean flightRecorderMXBean = ManagementFactory.getPlatformMXBean(mbsc, FlightRecorderMXBean.class);
        long recording = flightRecorderMXBean.newRecording();
        System.out.println("Starting recording");
        flightRecorderMXBean.startRecording(recording);
        Thread.sleep(1000);
        flightRecorderMXBean.stopRecording(recording);

    }

    public static MBeanServerConnection getLocalMBeanServerConnectionStatic(String[] s) {
        try {
            JMXServiceURL jmxUrl =  new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + "localhost" + ":" + s[0] +  "/jmxrmi");
            Map<String, Object> env = new HashMap<>();
            String[] credentials = {"myrole", "MYP@SSWORD"};
            env.put(JMXConnector.CREDENTIALS, credentials);
            env.put("com.sun.jndi.rmi.factory.socket", new SslRMIClientSocketFactory()); //must be included if protecting registry with ssl.
            return JMXConnectorFactory.connect(jmxUrl, env).getMBeanServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
