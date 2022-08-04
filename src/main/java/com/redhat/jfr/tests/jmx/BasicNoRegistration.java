package com.redhat.jfr.tests.jmx;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class BasicNoRegistration {
    public static void main(String args[]) throws Exception {
        Game g= new Game();

        while(true) {
            g.setPlayerName("test");
            g.getPlayerName();
            Thread.sleep(100);
        }

    }

}

