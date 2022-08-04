package com.redhat.jfr.tests.jmx;

import jdk.management.jfr.FlightRecorderMXBean;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

public class Tester {
    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = getLocalMBeanServerConnectionStatic(args[0]);
        System.out.println("made connection: "+ mbsc.toString());
        ObjectName objectName;
        //Works now. Needed to be accounted for in reflection config
//        objectName = new ObjectName("java.lang:type=Runtime");
//        MBeanInfo info = mbsc.getMBeanInfo(objectName);
//        System.out.println(info.toString());

        //works
//        System.out.println(mbsc.getDefaultDomain());

        //works
//        objectName = new ObjectName("jdk.management.jfr:type=FlightRecorder");
//        Object ret = mbsc.invoke(objectName,"newRecording", null, null);
//        System.out.println("invocation returned: "+ ret.toString());

        //fails
//        objectName = new ObjectName("java.lang:type=Runtime");
//        ret = mbsc.invoke(objectName,"getVmVersion", null, null);
//        System.out.println("invocation returned: "+ ret.toString());


        //this client procedure will try to obtain info from the server. Remember this is a diff Management factory than on the server side.
        FlightRecorderMXBean flightRecorderMXBean = ManagementFactory.getPlatformMXBean(mbsc, FlightRecorderMXBean.class);
        long recording = flightRecorderMXBean.newRecording();
        flightRecorderMXBean.startRecording(recording);
        Thread.sleep(500);
        System.out.println("recordings: "+flightRecorderMXBean.getRecordings().toString());
        Thread.sleep(1000);
        flightRecorderMXBean.stopRecording(recording);

    }

    public static MBeanServerConnection getLocalMBeanServerConnectionStatic(String s) {
        try {
            //for manual set up of server
//            JMXServiceURL jmxUrl =  new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/server");
            //when using automatic server setup, a generated urlPath is appended
            JMXServiceURL jmxUrl =  new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + "localhost" + ":" + "9999" +  "/jmxrmi");
//            JMXServiceURL jmxUrl =  new JMXServiceURL(s);
            Map<String, String[]> env = new HashMap<>();
            String[] credentials = {"myrole", "MYP@SSWORD"};
            env.put(JMXConnector.CREDENTIALS, credentials);
            return JMXConnectorFactory.connect(jmxUrl, env).getMBeanServerConnection();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
