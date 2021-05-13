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

package com.redhat.jfr.events;

import jdk.jfr.Label;
import jdk.jfr.Event;
import jdk.jfr.BooleanFlag;
import jdk.jfr.DataAmount;
import jdk.jfr.Frequency;
import jdk.jfr.MemoryAddress;
import jdk.jfr.Percentage;
import jdk.jfr.Timespan;
import jdk.jfr.Timestamp;
import jdk.jfr.Unsigned;

public class DataTypesEvent extends Event {
    @Label("Class Value 1")
    public Class<?> classValue1;

    @Label("Thread Value 1")
    public Thread threadValue1;

    @Label("String Value 1")
    public String stringValue1;

    @Label("Class Value 2")
    public Class<?> classValue2;

    @Label("Thread Value 2")
    public Thread threadValue2;

    @Label("String Value 2")
    public String stringValue2;

    @Label("Byte Value")
    public byte byteValue;

    @Label("Short Value")
    public short shortValue;

    @Label("Int Value")
    public int intValue;

    @Label("Long Value")
    public long longValue;

    @Label("Float Value")
    public float floatValue;

    @Label("Double Value")
    public double doubleValue;

    @Label("Character Value")
    public char characterValue;

    @Label("Boolean Value")
    public boolean booleanValue;

    @Label("BooleanFlag Annotated Value")
    @BooleanFlag
    public boolean booleanFlagValue;

    @Label("DataAmount Annotated Value")
    @DataAmount(DataAmount.BYTES)
    public int dataAmountValue;

    @Label("Frequency Annotated Value")
    @Frequency
    public int frequencyValue;

    @Label("MemoryAddress Annotated Value")
    @MemoryAddress
    public int memoryAddressValue;

    @Label("Percentage Annotated Value")
    @Percentage
    public double percentageValue;

    @Label("Timespan Annotated Value")
    @Timespan
    public int timespanValue;

    @Label("Timestamp Annotated Value")
    @Timestamp(Timestamp.MILLISECONDS_SINCE_EPOCH)
    public long timestampValue;

    @Label("Unsigned Annotated Value")
    @Unsigned
    public int unsignedValue;
}
