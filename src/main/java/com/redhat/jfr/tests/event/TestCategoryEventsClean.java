/*
 * Copyright (c) 2021, Red Hat, Inc.
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

package com.redhat.jfr.tests.event;

import static com.redhat.jfr.events.annotated.CategoryEvents.AlphaEvent;
import static com.redhat.jfr.events.annotated.CategoryEvents.BetaEvent;
import static com.redhat.jfr.events.annotated.CategoryEvents.AlphaBetaEvent;

public class TestCategoryEventsClean {

    //   * AlphaEvent     @Category("Alpha")           @Name("AlphaEvent")
    //   * BetaEvent      @Category("Beta")            @Name("BetaEvent")
    //   * AlphaBetaEvent @Category({"Alpha", "Beta"}) @Name("AlphaBetaEvent")
    // -----------------------------------------------------------
    //   |            alphaEvent                 |
    // -----------------------------------------------------------
    //   | betaEvent1 |          |          betaEvent2         |
    // -----------------------------------------------------------
    //   |     alphaBetaEvent    |
    // -----------------------------------------------------------
    public static void main(String args[]) throws Exception {
        long s0 = System.currentTimeMillis();
        run();
        long d0 = System.currentTimeMillis();
        System.out.println("elapsed:" + d0);
    }

    public static void run() throws Exception {
        AlphaEvent alphaEvent = new AlphaEvent();
        BetaEvent betaEvent1 = new BetaEvent();
        BetaEvent betaEvent2 = new BetaEvent();
        AlphaBetaEvent alphaBetaEvent = new AlphaBetaEvent();
        alphaEvent.begin();
        betaEvent1.begin();
        alphaBetaEvent.begin();
        Thread.sleep(100);
        betaEvent1.commit();
        Thread.sleep(100);
        betaEvent2.begin();
        alphaBetaEvent.commit();
        Thread.sleep(100);
        alphaEvent.commit();
        Thread.sleep(100);
        betaEvent2.commit();
    }
}
