package com.redhat.jfr.tests.jmx;

import jdk.management.jfr.FlightRecorderMXBean;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

public class JfrTester {
    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = getLocalMBeanServerConnectionStatic();
        System.out.println("made connection: " + mbsc.toString());

        FlightRecorderMXBean flightRecorderMXBean = ManagementFactory.getPlatformMXBean(mbsc, FlightRecorderMXBean.class);
        long recording = flightRecorderMXBean.newRecording();
        flightRecorderMXBean.startRecording(recording);

        // enable events
        var settings = new HashMap<String,String>();
        settings.put("jdk.JavaMonitorEnter#enabled","true");
        flightRecorderMXBean.setRecordingSettings(recording, settings);

        Thread.sleep(3000);
        flightRecorderMXBean.stopRecording(recording);

        System.out.println("stopped");
        long streamId = flightRecorderMXBean.openStream(recording,null);
        System.out.println("opened");
        File f = new File("/home/rtoyonag/repos/jfr-tests/stream_" + recording + ".jfr");
        try (var fos = new FileOutputStream(f); var bos = new BufferedOutputStream(fos)) {
            System.out.println("reading");
//            while (true) {
//                byte[] data = flightRecorderMXBean.readStream(streamId);
//                if (data == null) {
//                    bos.flush();
//                    System.out.println("DONE");
//                    flightRecorderMXBean.closeStream(streamId);
//                    fos.flush();
//                    fos.close();
//                    return;
//                }
//                bos.write(data);
//            }

            byte[] buff;
            while (true)
            {
                buff = flightRecorderMXBean.readStream(streamId);
                if (buff != null)
                {
                    fos.write(buff);
                } else {
                    break;
                }
            }
            flightRecorderMXBean.closeStream(streamId);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            System.out.println("FAILED: "+ e.toString());
        }
    }
    public static MBeanServerConnection getLocalMBeanServerConnectionStatic() {
        try {
            JMXServiceURL jmxUrl =  new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + "localhost" + ":" + "9999" +  "/jmxrmi");
            Map<String, String[]> env = new HashMap<>();
            String[] credentials = {"myrole", "MYP@SSWORD"};
            env.put(JMXConnector.CREDENTIALS, credentials);
            return JMXConnectorFactory.connect(jmxUrl, env).getMBeanServerConnection();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
