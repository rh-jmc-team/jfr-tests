package com.redhat.jfr.tests.jmx;
import javax.management.*;
import java.lang.management.ManagementFactory;
import com.redhat.jfr.events.StringEvent;

/**
 * This is a basic test meant to run as a JMX server. It creates the lazily initialized MBeanServer
 * and registers a new Mbean. Then it creates JFR String events in a loop while it waits for a JMX client to connect to it.
 */
public class Basic {
    public static void main(String args[]) throws Exception {

        try {
            ObjectName objectName = new ObjectName("com.jmx.test.basic:type=basic,name=game");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.getDefaultDomain();
            server.registerMBean(new Game(), objectName);

        } catch (Exception e) {
            System.out.println("FAILED: "+ e);
        }

        while(true){
            Thread.sleep(500);
            StringEvent event = new StringEvent();
            event.message = "Event has been generated!";
            event.commit();
        }

    }

}

