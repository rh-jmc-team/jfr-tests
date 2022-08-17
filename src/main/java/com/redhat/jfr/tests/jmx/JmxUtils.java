package com.redhat.jfr.tests.jmx;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JmxUtils {
    public static MBeanServerConnection getLocalMBeanServerConnectionStatic(String[] s ) {
        try {
            JMXServiceURL jmxUrl =  new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + "localhost" + ":" + s[0] +  "/jmxrmi");
            Map<String, Object> env = new HashMap<>();
            String[] credentials = {"myrole", "MYP@SSWORD"}; // hardcoded for now
            env.put(JMXConnector.CREDENTIALS, credentials);
            if (s[1].equals("ssl")) {
                env.put("com.sun.jndi.rmi.factory.socket", new SslRMIClientSocketFactory()); //must be included if protecting registry with ssl.
            }
            JMXConnector connector = JMXConnectorFactory.connect(jmxUrl, env);
            System.out.println("Got connector");
            return connector.getMBeanServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
