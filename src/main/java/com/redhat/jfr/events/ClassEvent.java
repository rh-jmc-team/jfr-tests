package com.redhat.jfr.events;

import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.StackTrace;

@Label("String Event")
@Description("An event with a string payload")
@StackTrace(false)
public class ClassEvent extends Event {

    @Label("Class")
    public Class klass;
}
