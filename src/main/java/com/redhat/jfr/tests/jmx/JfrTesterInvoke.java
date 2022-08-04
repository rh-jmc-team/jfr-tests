package com.redhat.jfr.tests.jmx;

import jdk.management.jfr.FlightRecorderMXBean;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.*;
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

public class JfrTesterInvoke {
    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = getLocalMBeanServerConnectionStatic();
        System.out.println("made connection: " + mbsc.toString());


        ObjectName objectName = new ObjectName("jdk.management.jfr:type=FlightRecorder");
        long recording = (long) mbsc.invoke(objectName,"newRecording", null, null);

        mbsc.invoke(objectName, "startRecording",new Object[] {recording}, new String[] {"long"});

        // enable events
        var settings = new HashMap<String,String>();
        settings.put("jdk.JavaMonitorEnter#enabled","true");
        mbsc.invoke(objectName, "setRecordingSettings",new Object[] {recording, toTabularData(settings)}, new String[] {"long","javax.management.openmbean.TabularData"});

        Thread.sleep(3000);
        mbsc.invoke(objectName, "stopRecording",new Object[] {recording}, new String[] {"long"});

        System.out.println("stopped");
        long streamId = (long) mbsc.invoke(objectName, "openStream",new Object[] {recording, null}, new String[] {"long", "javax.management.openmbean.TabularData"});

        System.out.println("opened");


        File f = new File("/home/rtoyonag/repos/jfr-tests/stream_" + recording + ".jfr");
        try (var fos = new FileOutputStream(f); var bos = new BufferedOutputStream(fos)) {
            System.out.println("reading");
            byte[] buff;
            while (true)
            {
                buff = (byte[]) mbsc.invoke(objectName, "readStream",new Object[] {streamId}, new String[] {"long"});
                if (buff != null)
                {
                    fos.write(buff);
                } else {
                    break;
                }
            }
            mbsc.invoke(objectName, "closeStream",new Object[] {streamId}, new String[] {"long"});
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

    public static TabularData toTabularData(Map<String,String> settings) throws OpenDataException {
        TabularDataSupport tdata = new TabularDataSupport(OPTIONS_TYPE);
        for (String key : settings.keySet()) {
            String value = settings.get(key);
            if (value != null) {
                tdata.put(new CompositeDataSupport(OPTIONS_ROW_TYPE, new String[] {"key", "value"},
                        new String[] {key, value}));
            }
        }
        return tdata;
    }
    private static CompositeType createOptionsRowType() {
        String typeName = "java.util.Map<java.lang.String, java.lang.String>"; //$NON-NLS-1$
        String[] keyValue = new String[] {"key", "value"}; //$NON-NLS-1$ //$NON-NLS-2$
        OpenType<?>[] openTypes = new OpenType[] {SimpleType.STRING, SimpleType.STRING};
        try {
            return new CompositeType(typeName, typeName, keyValue, keyValue, openTypes);
        } catch (OpenDataException e) {
            // Will never happen
            return null;
        }
    }

    private static TabularType createOptionsType(CompositeType rowType) {
        try {
            return new TabularType(rowType.getTypeName(), rowType.getTypeName(), rowType, new String[] {"key"}); //$NON-NLS-1$
        } catch (OpenDataException e) {
            // Will never happen
            return null;
        }
    }
    final static TabularType OPTIONS_TYPE;
    final static CompositeType OPTIONS_ROW_TYPE;
    static {
        OPTIONS_ROW_TYPE = createOptionsRowType();
        OPTIONS_TYPE = createOptionsType(OPTIONS_ROW_TYPE);
    }
}
