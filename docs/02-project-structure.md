# 02 - Project Structure

## 1. Introduction

This document describes the directory layout of the `GRPCMicroService` project and explains the purpose of its main files and packages.

The project follows the conventional Maven directory structure, separating application source code, Protocol Buffers definitions, resources, tests, and build configuration.

The Java package structure separates application startup, client functionality, server-side service implementation, application exceptions, centralized gRPC error handling, and gRPC call logging.

## 2. Directory Layout

The relevant project structure is shown below:

```text
GRPCMicroService/
├── docs/
│   ├── 01-project-overview.md
│   ├── 02-project-structure.md
│   ├── 03-grpc-protocol.md
│   ├── 04-greeter-service-implementation.md
│   ├── 05-application-bootstrap.md
│   ├── 06-application-configuration.md
│   ├── 07-grpc-client.md
│   ├── 08-client-runner.md
│   ├── 09-testing.md
│   ├── 10-maven-build.md
│   ├── 11-running-and-troubleshooting.md
│   ├── README.md
│   └── quick-start/
│       ├── index.html
│       └── images/
│           ├── application.png
│           ├── eclipse-import.png
│           └── verify.png
├── effective-pom.xml
├── LICENSE
├── pom.xml
├── README.md
├── THIRD-PARTY-NOTICES.txt
└── src/
    ├── main/
    │   ├── java/
    │   │   └── org/frekost/micro/
    │   │       ├── Application.java
    │   │       ├── client/
    │   │       │   ├── HelloClient.java
    │   │       │   └── HelloClientRunner.java
    │   │       ├── grpc/
    │   │       │   ├── error/
    │   │       │   │   ├── GrpcErrorCode.java
    │   │       │   │   ├── GrpcErrorHandlingInterceptor.java
    │   │       │   │   └── GrpcErrorMapper.java
    │   │       │   └── exception/
    │   │       │       ├── BusinessException.java
    │   │       │       ├── InvalidRequestException.java
    │   │       │       └── ResourceNotFoundException.java
    │   │       ├── logging/
    │   │       │   └── GrpcLoggingInterceptor.java
    │   │       └── services/
    │   │           └── grpc/
    │   │               └── greeter/
    │   │                   └── HelloServiceImpl.java
    │   ├── proto/
    │   │   └── helloworld.proto
    │   └── resources/
    │       └── application.properties
    └── test/
        ├── java/
        │   └── org/frekost/test/micro/
        │       ├── HelloServiceTest.java
        │       ├── error/
        │       │   └── GrpcErrorHandlingTest.java
        │       └── logging/
        │           └── GrpcLoggingInterceptorTest.java
        └── resources/
```

The tree shows the main project files and the relevant source directories. Maven may create additional directories and generated files during the build.

The `effective-pom.xml` file contains an effective Maven configuration, which incorporates the project's POM and applicable inherited configuration. It is not the primary build configuration; `pom.xml` remains the main file to maintain.

## 3. Root Files and Directories

### 3.1 `pom.xml`

The Maven Project Object Model (POM) defines the project metadata, dependencies, build plugins, and build configuration.

It configures the Java compiler, application dependencies, Protocol Buffers source generation, and test execution.

The exact Java version, dependency versions, and plugin settings should be verified against the current POM.

See [10 - Maven Build](10-maven-build.md) for details.

### 3.2 `README.md`

The root README provides the project's main entry point.

It introduces the application, lists the basic requirements, provides the quick-start instructions, and links to the technical documentation.

### 3.3 `docs/`

This directory contains the technical documentation and visual Quick Start guide.

Its `README.md` acts as the documentation index and links to the individual chapters.

### 3.4 `LICENSE` and `THIRD-PARTY-NOTICES.txt`

`LICENSE` contains the project's license information.

`THIRD-PARTY-NOTICES.txt` provides notices concerning third-party components, as applicable to the project.

### 3.5 `effective-pom.xml`

This file represents the effective Maven model used to inspect the resolved project configuration.

It can help diagnose dependency and plugin configuration issues. It should not be confused with the primary `pom.xml`.

## 4. Application Source Code

Application source files are stored under:

`src/main/java/`

The main Java package is:

`org.frekost.micro`

### 4.1 `Application.java`

**Package:** `org.frekost.micro`

This class contains the application's `main` method and starts Micronaut.

It serves as the application entry point.

See [05 - Application Bootstrap](05-application-bootstrap.md).

### 4.2 Client Package

**Package:** `org.frekost.micro.client`

This package contains the gRPC client and its example runner.

#### `HelloClient.java`

This class implements the gRPC client.

Its main responsibilities are:

- Receiving the configured `greeter` channel through dependency injection.
- Creating a blocking gRPC stub.
- Building a `HelloRequest`.
- Invoking the remote `SayHello` procedure.
- Returning the response message.

The class implements `AutoCloseable` and defines a `close()` method that shuts down its channel when the channel has not already been shut down.

Channel ownership and lifecycle should be considered when reusing the client in other applications.

See [07 - gRPC Client](07-grpc-client.md).

#### `HelloClientRunner.java`

This class provides an automatically triggered example client invocation.

It uses Micronaut's scheduling support to invoke the service after an initial delay of three seconds. The example call uses the name `Micronaut` and prints the returned response.

The runner also handles exceptions from the example invocation so that a failed call can be reported.

See [08 - Client Runner](08-client-runner.md).

### 4.3 gRPC Error Package

**Package:** `org.frekost.micro.grpc.error`

This package contains the components responsible for centralized gRPC error handling.

#### `GrpcErrorHandlingInterceptor.java`

This class implements a gRPC server interceptor.

Its responsibilities include:

- Intercepting incoming server calls.
- Handling supported synchronous exceptions raised during request processing.
- Mapping exceptions to gRPC status errors.
- Logging failures according to their severity.
- Closing the affected server call with the appropriate status.

Centralizing this behavior helps avoid repeating the same error-handling logic across service implementations.

The interceptor does not automatically handle every asynchronous failure or an error explicitly reported through `StreamObserver.onError()`. Those cases may require handling at the relevant call site.

#### `GrpcErrorMapper.java`

This class maps application exceptions to gRPC status errors.

The intended mappings include:

| Exception or error condition | gRPC status |
|---|---|
| `InvalidRequestException` | `INVALID_ARGUMENT` |
| `ResourceNotFoundException` | `NOT_FOUND` |
| `BusinessException` | `FAILED_PRECONDITION` |
| Unexpected exception | `INTERNAL` |

Existing gRPC status exceptions should preserve their original status when handled by the mapper.

The actual mapping rules are defined by the implementation of `GrpcErrorMapper`.

#### `GrpcErrorCode.java`

This class defines application-level gRPC error codes.

Its purpose is to centralize error-code definitions used by the application's error-handling logic.

For further information, see [09 - Testing](09-testing.md).

### 4.4 Application Exception Package

**Package:** `org.frekost.micro.grpc.exception`

This package contains application-specific exception classes.

#### `InvalidRequestException.java`

Represents an invalid request, such as a request containing an empty or whitespace-only name.

The error mapper converts this exception into the gRPC status `INVALID_ARGUMENT`.

#### `ResourceNotFoundException.java`

Represents a request for a resource that cannot be found.

The error mapper associates this exception with `NOT_FOUND`.

#### `BusinessException.java`

Represents an application business-rule failure.

The error mapper associates this exception with `FAILED_PRECONDITION`.

These exceptions separate application-level failure conditions from the gRPC status codes used to communicate errors to remote clients.

### 4.5 Logging Package

**Package:** `org.frekost.micro.logging`

This package contains the interceptor responsible for centralized logging of incoming gRPC server calls.

#### `GrpcLoggingInterceptor.java`

This class implements a gRPC `ServerInterceptor`.

Its responsibilities include:

- Recording the start of an incoming gRPC call.
- Recording the final gRPC status when the call closes.
- Measuring the elapsed time of the call.
- Logging successful calls at `INFO` level.
- Logging calls completed with a non-OK status at `WARN` level.
- Logging synchronous exceptions raised while starting a call at `ERROR` level and rethrowing them.

The interceptor uses SLF4J for logging. The project uses SLF4J Simple as its logging backend.

The interceptor records method names, completion statuses, and execution duration. It does not log request or response payloads.

For example, log messages may look like this:

```text
gRPC call started: method=helloworld.Greeter/SayHello
gRPC call completed: method=helloworld.Greeter/SayHello, status=OK, durationMs=12
```

The duration is measured using `System.nanoTime()`.

This interceptor provides operational visibility into gRPC calls without logging their message contents.

See [11 - Running and Troubleshooting](11-running-and-troubleshooting.md) for further information.

### 4.6 Greeter Service Implementation

**Package:** `org.frekost.micro.services.grpc.greeter`

#### `HelloServiceImpl.java`

This class implements the server-side `Greeter` service.

It extends the generated `GreeterGrpc.GreeterImplBase` class and overrides the `sayHello()` method.

Its responsibilities include:

- Reading the name from the incoming request.
- Validating the name.
- Creating a greeting response for valid requests.
- Sending the response through the gRPC `StreamObserver`.

For a valid name such as `Micronaut`, the service returns:

```text
Hello, Micronaut!
```

If the name is empty or contains only whitespace, the service throws an `InvalidRequestException`. The centralized error-handling mechanism maps this exception to `INVALID_ARGUMENT`.

The `@GrpcService` annotation registers the implementation as a Micronaut gRPC service.

See [04 - Greeter Service Implementation](04-greeter-service-implementation.md).

## 5. Protocol Definition

### `src/main/proto/helloworld.proto`

This file defines the gRPC service contract using Protocol Buffers.

It declares:

- The `Greeter` service.
- The unary `SayHello` RPC.
- The `HelloRequest` message.
- The `HelloReply` message.
- The Java package used for generated classes.

The protocol definition is the source from which the relevant Java message and service classes are generated.

Changes to the service contract should be made in the `.proto` file rather than in generated Java sources.

See [03 - gRPC Protocol](03-grpc-protocol.md).

## 6. Application Resources

### `src/main/resources/application.properties`

This file contains the application's configuration properties.

The current configuration includes:

- The Micronaut application name.
- Whether the gRPC server is enabled.
- The gRPC server port.
- The `greeter` client channel address.
- The plaintext setting for that client channel.

The local configuration uses port `50052` for the server, and the client channel targets `localhost:50052`.

Plaintext communication is suitable for the local example. Production deployments should use an appropriate transport-security configuration when required.

See [06 - Application Configuration](06-application-configuration.md).

## 7. Test Source Code

Test source files are stored under:

`src/test/java/`

The test package is:

`org.frekost.test.micro`

The tests cover service behavior, centralized gRPC error handling, and logging.

### 7.1 `HelloServiceTest.java`

**Path:**

`src/test/java/org/frekost/test/micro/HelloServiceTest.java`

This class contains an integration test using JUnit 5 and Micronaut Test.

The test injects `HelloClient`, invokes `sayHello("JUnit")`, and asserts that the returned message is:

```text
Hello, JUnit!
```

It verifies the successful interaction between the client and the service in the Micronaut test environment.

Run this test with:

```bash
mvn -Dtest=HelloServiceTest test
```

### 7.2 `GrpcErrorHandlingTest.java`

**Path:**

`src/test/java/org/frekost/test/micro/error/GrpcErrorHandlingTest.java`

This class contains tests for successful requests and gRPC error handling.

The tests exercise scenarios such as:

- Requests with valid names.
- Requests with empty names.
- Requests with whitespace-only names.
- Names containing spaces.
- Successful requests following invalid requests.

The tests help verify that invalid input produces the expected gRPC error and that the service remains usable after an invalid request.

Run this test with:

```bash
mvn -Dtest=GrpcErrorHandlingTest test
```

See [09 - Testing](09-testing.md).

### 7.3 `GrpcLoggingInterceptorTest.java`

**Path:**

`src/test/java/org/frekost/test/micro/logging/GrpcLoggingInterceptorTest.java`

This class contains tests for the gRPC logging interceptor.

It belongs to the logging test package and complements the service and error-handling tests.

Run this test with:

```bash
mvn -Dtest=GrpcLoggingInterceptorTest test
```

See [09 - Testing](09-testing.md).

## 8. Generated Sources and Build Output

The project uses Maven plugins to generate Java source files from the Protocol Buffers definition.

The generated classes include the message types, service base class, and gRPC client stubs used by the application.

Generated files are build artifacts and should not be treated as manually maintained source code.

After running a Maven build, generated files and compiled classes may be found under:

```text
target/
```

The exact generated directory layout depends on the Maven plugin configuration and build lifecycle.

Do not edit generated Java files directly. Modify `helloworld.proto` instead and regenerate the sources.

See [10 - Maven Build](10-maven-build.md).

## 9. Configuration and Execution

The project separates configuration from Java implementation:

- Java classes define application behavior.
- The `.proto` file defines the communication contract.
- `application.properties` configures the server and client channel.
- `GrpcErrorHandlingInterceptor` and `GrpcErrorMapper` implement centralized error handling.
- Application exception classes represent specific failure conditions.
- `GrpcLoggingInterceptor` records gRPC call starts, completion statuses, and elapsed time.
- `pom.xml` defines dependencies and build operations.
- Test sources verify service behavior, error handling, and logging.

This separation makes the project easier to maintain and helps identify where a change should be made.

## 10. Summary

The `GRPCMicroService` project follows a conventional Maven layout and separates its main responsibilities into distinct areas.

| Area | Location | Responsibility |
|---|---|---|
| Build configuration | `pom.xml` | Dependencies, plugins, and build lifecycle |
| Application startup | `src/main/java/org/frekost/micro/` | Starts Micronaut |
| Client | `src/main/java/org/frekost/micro/client/` | Calls the gRPC service |
| Error handling | `src/main/java/org/frekost/micro/grpc/error/` | Intercepts supported exceptions and maps errors |
| Application exceptions | `src/main/java/org/frekost/micro/grpc/exception/` | Represents application-level failure conditions |
| Logging | `src/main/java/org/frekost/micro/logging/` | Logs gRPC call starts, statuses, and durations |
| Service implementation | `src/main/java/org/frekost/micro/services/grpc/greeter/` | Handles incoming RPC requests |
| Protocol contract | `src/main/proto/` | Defines messages and RPCs |
| Application configuration | `src/main/resources/` | Configures the server and client |
| Tests | `src/test/java/` | Verifies service behavior, error handling, and logging |
| Documentation | `docs/` | Explains the project and its implementation |