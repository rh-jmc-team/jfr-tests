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
