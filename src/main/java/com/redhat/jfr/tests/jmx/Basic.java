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

