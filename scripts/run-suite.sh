#!/bin/bash

function build() {
    mx build
}

function image() {
    mx native-image --no-fallback -H:+AllowVMInspection -cp $1 $2
}

function run() {
    ./$1 -XX:+FlightRecorder -XX:StartFlightRecording=filename=$2.jfr
}

classpath="./jfr-tests/target/classes"
tests=("TestCategoryEventsClean" "TestConcurrentEventsClean" "TestCustomAnnotationsEventClean" "TestDataTypesEventClean" "TestEnabledAndRegisteredEventsClean" "TestInheritanceEventClean" "TestMultipleEventsClean" "TestPeriodicEventClean")

cmd=$1

jfrprefix=$2
packageprefix=com.redhat.jfr.tests.event

if [ "$cmd" = "all" ] || [ "$cmd" = "build" ]; then
    build
fi

if [ "$cmd" = "all" ] || [ "$cmd" = "image" ]; then
    for t in ${tests[@]}; do
        image $classpath $packageprefix.$t
    done
fi
if [ "$cmd" = "all" ] || [ "$cmd" = "run" ]; then
    for t in ${tests[@]}; do
        v=$(echo "$t" | tr '[:upper:]' '[:lower:]')
        run $packageprefix.$v $jfrprefix.$v
    done
fi