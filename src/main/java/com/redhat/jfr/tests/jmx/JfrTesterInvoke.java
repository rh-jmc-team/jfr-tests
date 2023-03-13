/*
 * Copyright (c) 2022, Red Hat, Inc.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of the Red Hat GraalVM Testing Suite (the suite).
 *
 * The suite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * The suite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the suite.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.redhat.jfr.tests.jmx;


import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/*
 * This is a JMX client. It will connect to a JMX server running on localhost:<port-you-specify>.
 * Then it will set JFR recording settings to enable jdk.JavaMonitorEnter events and record for 3s.
 * Then it will stream the recording and output to a file.
 */
public class JfrTesterInvoke {
    public static void main(String args[]) throws Exception {
        MBeanServerConnection mbsc = JmxUtils.getLocalMBeanServerConnectionStatic(args);
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


        File f = new File("stream_" + recording + ".jfr");
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
