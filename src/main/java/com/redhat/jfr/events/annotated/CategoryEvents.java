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

package com.redhat.jfr.events.annotated;

import jdk.jfr.Event;
import jdk.jfr.Category;

public final class CategoryEvents {
    private CategoryEvents() {
    }

    @Category("Alpha")
    public static class AlphaEvent extends Event {
    }

    @Category("Beta")
    public static class BetaEvent extends Event {
    }

    @Category({"Alpha", "Beta"})
    public static class AlphaBetaEvent extends Event {
    }
}
