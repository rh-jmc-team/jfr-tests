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

import com.redhat.jfr.events.DataTypesEvent;

public class TestDataTypesEventClean {

    public static void main(String args[]) {
        DataTypesEvent dataTypesEvent = new DataTypesEvent();
        dataTypesEvent.classValue1 = Math.class;
        dataTypesEvent.threadValue1 = Thread.currentThread();
        dataTypesEvent.stringValue1 = "Hello, World!";
        dataTypesEvent.classValue2 = null;
        dataTypesEvent.threadValue2 = null;
        dataTypesEvent.stringValue2 = null;
        dataTypesEvent.byteValue = (byte) 1;
        dataTypesEvent.shortValue = (short) 0111;
        dataTypesEvent.intValue = 0x111;
        dataTypesEvent.longValue = Long.MAX_VALUE;
        dataTypesEvent.floatValue = Float.MIN_VALUE;
        dataTypesEvent.doubleValue = Math.PI;
        dataTypesEvent.characterValue = 'C';
        dataTypesEvent.booleanValue = true;
        dataTypesEvent.booleanFlagValue = false;
        dataTypesEvent.dataAmountValue = -1;
        dataTypesEvent.frequencyValue = 1;
        dataTypesEvent.memoryAddressValue = 1;
        dataTypesEvent.percentageValue = 101;
        dataTypesEvent.timespanValue = 1;
        dataTypesEvent.timestampValue = System.currentTimeMillis();
        dataTypesEvent.unsignedValue = -5;
        dataTypesEvent.commit();
    }
}
