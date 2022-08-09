package com.redhat.jfr.tests.jmx;

import jdk.management.jfr.FlightRecorderMXBean;

import javax.management.*;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.redhat.jfr.events.StringEvent;

import jdk.management.jfr.RecordingInfo;

public class Basic {
    public static void main(String args[]) throws Exception {

        try {
            RecordingInfo r;
            ObjectName objectName = new ObjectName("com.jmx.test.basic:type=basic,name=game");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.getDefaultDomain();
            server.registerMBean(new Game(), objectName);

//            objectName = new ObjectName("java.lang:type=Runtime");
//            ObjectName objectName = new ObjectName("jdk.management.jfr:type=FlightRecorder");
//            ObjectName objectName = new ObjectName("java.lang:type=Runtime");

//            MBeanInfo info = server.getMBeanInfo(objectName);
//            MBeanAttributeInfo[] attrInfo = info.getAttributes();
//            if (attrInfo.length > 0) {
//                for (int i = 0; i < attrInfo.length; i++) {
//                   System.out.println(" ** NAME: 	" + attrInfo[i].getName() + "    DESCR: 	" + attrInfo[i].getDescription()+"    TYPE: 	" + attrInfo[i].getType());
//                }
//            } else {
//                System.out.println("couldn't find attributes");
//            }


//            Map<String, Object> env = new HashMap();
//            env.put(Context.INITIAL_CONTEXT_FACTORY,
//                    "com.sun.jndi.ldap.LdapCtxFactory");
//            env.put("jmx.remote.rmi.server.credentials.filter.pattern", String.class.getName() + ";!*");
//            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/server");
//            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
//            cs.start();

//            FlightRecorderMXBean flightRecorderMXBean = ManagementFactory.getPlatformMXBean(FlightRecorderMXBean.class);

//            server.invoke(objectName,"newRecording", null, null);
//            System.out.println(server.invoke(objectName,"getVmVersion", null, null));
//            long rec = flightRecorderMXBean.newRecording();
//            flightRecorderMXBean.startRecording(rec);
//            flightRecorderMXBean.stopRecording(rec);
        } catch (Exception e) {
            // handle exceptions
            System.out.println("Exception happened"+ e.toString());
        }

//        RuntimeMXBean runtimeMXBean = ManagementFactory.getPlatformMXBean(RuntimeMXBean.class);
//        System.out.println("PID:" + runtimeMXBean.getPid());
//        System.out.println("Uptime:" + runtimeMXBean.getUptime());


//        System.out.println("Trying to make connection");
//        MBeanServerConnection mbsc = getLocalMBeanServerConnectionStatic(args[0]);
//        System.out.println(mbsc.toString());
        while(true){
            Thread.sleep(500);
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }

    }

}

