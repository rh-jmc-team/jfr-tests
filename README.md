# jfr-tests
Java applications that use JFR API for testing purposes

## Compile

```
mvn clean package
```

## Run test class (com.redhat.jfr.tests.*)

```
java -cp target/classes <fully-qualified-name>
```

For example:

```
java -cp target/classes com.redhat.jfr.tests.event.TestConcurrentEvents
```
