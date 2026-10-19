# Running and Troubleshooting

## Overview

This document explains how to build, run, test, and troubleshoot the
`GRPCMicroService` application.

The project uses Java 21, Apache Maven, Micronaut, gRPC, and Protocol
Buffers. It exposes a greeting service and includes a client that invokes
the service through a configured gRPC channel.

The gRPC server is configured to use port `50052`.

The main activities covered in this document are:

- Checking the development environment.
- Generating Protocol Buffers and gRPC Java sources.
- Building and testing the application.
- Reviewing the server and client configuration.
- Starting the application and observing client calls.
- Understanding gRPC logging.
- Diagnosing common build, configuration, and connectivity problems.

## Prerequisites

Before running the application, make sure that the following components
are available:

- Java Development Kit (JDK) 21.
- Apache Maven.
- Access to the configured Maven repositories to resolve dependencies.
- The `GRPCMicroService` project, imported into Eclipse if Eclipse is
  used as the development environment.

Check the installed Java version:

```bash
java -version
```

Check the Maven version and the Java runtime used by Maven:

```bash
mvn -version
```

Make sure Maven runs with a JDK compatible with Java 21.

## Project Directory

Run Maven commands from the directory containing the project's
`pom.xml` file.

For example:

```bash
cd /path/to/GRPCMicroService
```

Replace the example path with the actual project location.

## Step 1: Generate Sources and Build the Project

The project uses Maven to generate Java classes from the Protocol
Buffers definition.

The source definition is:

`src/main/proto/helloworld.proto`

The generated classes include the message types and gRPC service
definitions required by the application, such as:

- `HelloRequest`
- `HelloReply`
- `GreeterGrpc`

### Generate Sources

Run:

```bash
mvn clean generate-sources
```

This command removes previous build output and executes the configured
Protocol Buffers and gRPC code-generation goals.

The generated sources are build outputs. They should not normally be
edited manually.

### Compile the Project

Run:

```bash
mvn compile
```

This command executes the Maven lifecycle through the compile phase,
including the earlier phases needed to generate and compile the
application sources.

### Run the Full Verification Build

The preferred build command for this project is:

```bash
mvn clean compile verify
```

This command cleans previous build output and executes the requested
Maven lifecycle phases, including compilation, testing, and verification.

A successful source-generation step does not by itself guarantee that
compilation, tests, and application startup will all succeed.

## Step 2: Review the Configuration

The relevant settings in
`src/main/resources/application.properties` are:

```properties
micronaut.application.name=GRPCHelloWorld

# Server
grpc.server.port=50052
grpc.server.enabled=true

# Client
grpc.channels.greeter.plaintext=true
grpc.channels.greeter.address=localhost:50052
```

These properties configure the application name, gRPC server, and
client channel.

### Server Configuration

The server is configured to use port `50052`.

The property:

```properties
grpc.server.enabled=true
```

enables the gRPC server according to the project's Micronaut gRPC
configuration.

### Client Configuration

The client channel is named `greeter`.

Its configured address is:

```properties
grpc.channels.greeter.address=localhost:50052
```

The channel uses plaintext communication rather than TLS:

```properties
grpc.channels.greeter.plaintext=true
```

This setting is suitable for the current local development setup.
Production deployments should evaluate transport security and
authentication requirements separately.

The configuration establishes the intended endpoint relationship,
but actual connectivity must still be verified at runtime.

## Step 3: Start the Application

The application entry point is:

`src/main/java/org/frekost/micro/Application.java`

The `main` method starts Micronaut:

```java
var ctx = Micronaut.run(Application.class, args);
```

The application then reads the `grpc.server.port` property and prints
its configured value.

### Starting from Eclipse

If using Eclipse, run `Application.java` as a Java application.

Before starting, make sure that:

- The project has been imported as a Maven project.
- Maven dependencies have been resolved.
- The generated Protocol Buffers and gRPC sources are available.
- The project compiles successfully.

The application can then be started using the Eclipse Java application
run configuration.

### Starting with Maven

The following command may be used if the required Micronaut Maven
plugin and goal are configured and resolvable:

```bash
mvn mn:run
```

However, the supplied `pom.xml` does not explicitly declare the
Micronaut Maven plugin. If the command is unavailable, use the Eclipse
run configuration or another supported execution workflow.

### Starting a Packaged JAR

A packaged application can be started with:

```bash
java -jar target/grpc-micro-service-1.0.0.jar
```

This command requires the generated JAR to have an executable manifest
and access to all required runtime dependencies.

The supplied POM does not explicitly configure a packaging plugin to
create an executable, self-contained JAR. Therefore, successful
packaging alone does not guarantee that this command will work.

### Expected Diagnostic Output

The application prints the configured gRPC port using a message
similar to:

```text
📘 grpc.server.port letto: 50052
```

This output confirms that the application read the property as an
integer with the expected value.

It does not, by itself, prove that the gRPC server successfully bound
to the port or that a client can connect.

## Step 4: Observe the Automatic Client Call

The project contains `HelloClientRunner`, a Micronaut singleton that
invokes the gRPC client after an initial delay.

Its scheduling annotation is:

```java
@Scheduled(initialDelay = "3s")
```

The runner calls:

```java
helloClient.sayHello("Micronaut");
```

If the scheduled invocation executes successfully, the client prints:

```text
Response from gRPC server: Hello, Micronaut!
```

The service returns a greeting generated from the supplied name.

The scheduling annotation specifies an initial delay of three seconds.
It does not specify a recurring fixed delay or fixed rate.

The exact order of console messages may vary because different parts
of the application produce output independently.

### Handling Client Errors

The runner catches exceptions thrown during the client invocation.

When an exception occurs, it prints a message similar to:

```text
gRPC call failed: <exception message>
```

The error message is written to standard error.

The runner does not rethrow the exception or print the stack trace.
For this reason, additional diagnostic commands or application logs
may be needed to investigate a failure.

## Step 5: Observe gRPC Logging

The application includes a centralized gRPC logging interceptor:

`org.frekost.micro.logging.GrpcLoggingInterceptor`

The interceptor implements `io.grpc.ServerInterceptor` and is
registered as a Micronaut bean.

It records information about incoming server calls without requiring
logging statements in every service implementation.

### Logged Information

For each incoming gRPC call, the interceptor records:

- The full gRPC method name.
- The start of the call.
- The final gRPC status.
- The elapsed time until the call closes.

The elapsed time is measured using `System.nanoTime()`.

The interceptor uses SLF4J with SLF4J Simple as the logging backend.
Log messages are written to the console.

### Logging Levels

The interceptor uses the following logging levels:

| Level | Condition |
|---|---|
| `INFO` | Call start and successful completion |
| `WARN` | Completion with a non-OK gRPC status |
| `ERROR` | Synchronous exception while starting a call |

Synchronous exceptions encountered while starting a call are logged
and rethrown.

The interceptor does not replace the application's error-handling
mechanism. Asynchronous failures and errors reported through
`StreamObserver.onError()` require appropriate handling in the
relevant code paths.

### Example Output

A successful call may produce output similar to:

```text
INFO org.frekost.micro.logging.GrpcLoggingInterceptor - gRPC call started: method=helloworld.Greeter/SayHello
INFO org.frekost.micro.logging.GrpcLoggingInterceptor - gRPC call completed: method=helloworld.Greeter/SayHello, status=OK, durationMs=12
```

The method name and duration are examples. Actual values depend on
the service definition and execution environment.

### Payload Logging

The interceptor logs method names, statuses, and durations.

It does not inspect or log request and response payloads.

Avoid adding payload logging without considering the privacy and
security implications of exposing application data in logs.

### Logging Tests

The logging interceptor test class is located at:

`src/test/java/org/frekost/test/micro/logging/GrpcLoggingInterceptorTest.java`

Run the dedicated tests with:

```bash
mvn -Dtest=GrpcLoggingInterceptorTest test
```

The tests cover:

- Successful calls.
- Calls completed with a non-OK status.
- Synchronous exceptions raised while starting a call.

These tests use simulated gRPC server calls. They do not directly
assert log output or independently verify interceptor registration
in the running Micronaut server.

Run the complete project verification with:

```bash
mvn clean compile verify
```

### Current Logging Limitations

The current implementation writes logs to the console through
SLF4J Simple.

File-based logging, log rotation, request correlation identifiers,
and structured logging are not included in this implementation.

For streaming RPCs, the recorded duration extends until the server
call closes.

## Step 6: Run the Test Suite

The project contains the following test classes:

- `HelloServiceTest`
- `GrpcErrorHandlingTest`
- `GrpcLoggingInterceptorTest`

The tests use JUnit 5, with Micronaut testing infrastructure where
applicable.

### Run All Tests

Execute:

```bash
mvn test
```

This command runs the tests selected by Maven Surefire.

### Test the Greeting Service

The test class is:

`src/test/java/org/frekost/test/micro/HelloServiceTest.java`

It invokes the client with the name `JUnit` and checks the response:

```java
Assertions.assertEquals("Hello, JUnit!", resp);
```

Run only this test class with:

```bash
mvn -Dtest=HelloServiceTest test
```

The test uses `@MicronautTest` and an injected `HelloClient`.

### Test Error Handling

The error-handling test class is:

`src/test/java/org/frekost/test/micro/GrpcErrorHandlingTest.java`

It covers valid requests and selected invalid-input scenarios.

The invalid cases include an empty name and a name containing only
whitespace. These requests are expected to produce the gRPC status
`INVALID_ARGUMENT`.

The test also checks that a valid request can succeed after an invalid
request.

Run only the error-handling tests with:

```bash
mvn -Dtest=GrpcErrorHandlingTest test
```

### Test the Logging Interceptor

The logging interceptor test class is:

`src/test/java/org/frekost/test/micro/logging/GrpcLoggingInterceptorTest.java`

Run only these tests with:

```bash
mvn -Dtest=GrpcLoggingInterceptorTest test
```

The tests cover successful calls, non-OK completion statuses, and
synchronous exceptions raised while starting a call.

### Recommended Verification Command

To clean the build output and execute the complete verification
lifecycle, use:

```bash
mvn clean compile verify
```

If a test fails, inspect the Maven output to identify the first relevant
exception and the test that produced it.

## Troubleshooting

### Problem: Maven Cannot Find the Protocol Buffers Compiler

The build may fail if Maven cannot resolve or execute the configured
`protoc` binary or gRPC Java generator.

Check that:

- Maven can access the configured dependency repositories.
- The operating system classifier is detected correctly.
- The configured Protocol Buffers compiler artifact is available.
- The configured gRPC Java generator artifact is available.
- The current operating system and architecture are supported by the
  resolved executable artifacts.

To inspect the detected classifier, run:

```bash
mvn help:evaluate \
  -Dexpression=os.detected.classifier \
  -q \
  -DforceStdout
```

For example, the classifier on a Linux x86-64 environment may be:

```text
linux-x86_64
```

The actual value depends on the operating system and architecture.

### Problem: Generated Classes Cannot Be Resolved

If Eclipse reports that classes such as `GreeterGrpc`, `HelloRequest`,
or `HelloReply` cannot be resolved, regenerate the sources:

```bash
mvn clean generate-sources
```

Then refresh the Maven project in Eclipse:

1. Right-click the `GRPCMicroService` project.
2. Select **Maven > Update Project...**.
3. Select the project.
4. Confirm the update.
5. Wait for Eclipse to finish refreshing and rebuilding.

The generated source directory must be recognized by the build
configuration for Eclipse to resolve the generated classes correctly.

If the problem persists, run:

```bash
mvn clean compile verify
```

Compare any Eclipse errors with the Maven output to determine whether
the problem is related to source generation, compilation, or the
workspace configuration.

Avoid manually editing generated Java files.

### Problem: The Client Cannot Connect to the Server

Check the following configuration values:

```properties
grpc.server.enabled=true
grpc.server.port=50052
grpc.channels.greeter.plaintext=true
grpc.channels.greeter.address=localhost:50052
```

Verify that:

- The application starts without a server initialization error.
- The server is configured to use port `50052`.
- The client targets the correct host and port.
- No other process prevents the server from binding to the port.
- The client invocation occurs after the service is available.
- The client and server use compatible transport security settings.

A connection failure can produce a gRPC status exception. The runner
catches the exception and prints its message to standard error.

### Problem: Port 50052 Is Already in Use

Another process may already be listening on the configured port.

On Linux, inspect the port with:

```bash
ss -ltnp | grep ':50052'
```

If a process is using the port, identify it before stopping anything.

Alternatively, change the configured server port and update the client
address to match.

For example, if the server is moved to port `50053`, the corresponding
settings would be:

```properties
grpc.server.port=50053
grpc.channels.greeter.address=localhost:50053
```

Restart the application after changing the configuration.

### Problem: The Application Prints the Port but the Server Is Unavailable

The `Application` class prints the value of the `grpc.server.port`
property after calling `Micronaut.run()`.

This diagnostic message confirms that the property was read. It does
not independently verify that the gRPC server is listening.

If the client cannot connect:

1. Inspect the startup logs for server initialization errors.
2. Check whether the configured port is listening.
3. Verify the client channel address.
4. Confirm that the application has completed startup.
5. Review the gRPC error message reported by the client.

A property value and a successfully listening server are two different
things and should be verified separately.

### Problem: An Invalid Request Returns an Unexpected Error

The greeting service rejects names that are empty or contain only
whitespace.

For these requests, the service throws `InvalidRequestException`:

```text
The name must not be empty
```

The gRPC error-handling interceptor maps this exception to
`INVALID_ARGUMENT`.

The application log may contain a message similar to:

```text
GrpcErrorHandlingInterceptor - gRPC request failed with status INVALID_ARGUMENT: The name must not be empty
```

If the client receives a different status, verify that:

- `GrpcErrorHandlingInterceptor` is registered and active.
- `GrpcErrorMapper` contains the expected mapping.
- The exception originates from the expected service path.
- The application is running the latest compiled code.

An invalid request should not be interpreted as a transport connection
failure. It indicates that the request reached an error-handling path.

### Problem: The Test Fails

If a test fails, inspect the Maven output for the first relevant
exception.

Check whether:

- Main and generated sources compile successfully.
- The Micronaut test context starts correctly.
- The gRPC client channel is configured.
- The service is available to the test.
- The actual response matches the expected value.
- Invalid requests produce the expected gRPC status.
- The logging interceptor tests complete successfully.

Run a specific test with additional Maven diagnostic output:

```bash
mvn -e -Dtest=HelloServiceTest test
```

For the error-handling tests:

```bash
mvn -e -Dtest=GrpcErrorHandlingTest test
```

For the logging interceptor tests:

```bash
mvn -e -Dtest=GrpcLoggingInterceptorTest test
```

The `-e` option includes exception details in Maven's output.

For a complete rebuild and verification, run:

```bash
mvn clean compile verify
```

### Problem: Eclipse Reports Maven Errors After a Configuration Change

If Eclipse displays Maven dependency or build errors after modifying
the POM:

1. Save the POM.
2. Right-click the project.
3. Select **Maven > Update Project...**.
4. Wait for dependency resolution and project refresh to finish.
5. Review the Problems view again.

If the issue persists, compare the Eclipse errors with the output from
a command-line Maven build.

This helps distinguish Maven build problems from Eclipse workspace
configuration problems.

## Useful Diagnostic Commands

Display the Java version:

```bash
java -version
```

Display the Maven version and Java runtime used by Maven:

```bash
mvn -version
```

Generate Protocol Buffers and gRPC sources:

```bash
mvn clean generate-sources
```

Compile the application:

```bash
mvn compile
```

Run all tests:

```bash
mvn test
```

Run the greeting-service test:

```bash
mvn -Dtest=HelloServiceTest test
```

Run the error-handling tests:

```bash
mvn -Dtest=GrpcErrorHandlingTest test
```

Run the logging interceptor tests:

```bash
mvn -Dtest=GrpcLoggingInterceptorTest test
```

Run the complete verification lifecycle:

```bash
mvn clean compile verify
```

Inspect whether the gRPC port is listening on Linux:

```bash
ss -ltnp | grep ':50052'
```

## Operational Notes

The current configuration is designed for local development.

Before deploying the application to a shared or production environment,
review:

- TLS and transport security.
- Authentication and authorization requirements.
- Error handling and logging.
- Client channel lifecycle management.
- Port allocation and deployment configuration.
- Health checking and service monitoring.

The client channel is configured for plaintext communication, and the
current greeting-service implementation does not address all the
operational concerns listed above.

These concerns should be evaluated separately before production use.

## Summary

The application uses Java 21, Maven, Micronaut, gRPC, and Protocol
Buffers.

The Maven build generates the Java sources required by the service and
client, compiles the application, executes the tests, and performs the
configured verification steps.

The gRPC server is configured on port `50052`, and the `greeter` client
channel targets `localhost:50052`.

The application includes a centralized gRPC logging interceptor that
records method names, completion statuses, and call durations without
logging request or response payloads.

When troubleshooting, verify source generation, compilation, test
execution, application startup, server availability, and client
connectivity separately.

A successful build or a printed configuration value alone does not
guarantee successful runtime communication.