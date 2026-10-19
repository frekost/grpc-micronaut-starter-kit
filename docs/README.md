# GRPCMicroService Documentation

## 1. Overview

This directory contains the technical documentation for the `GRPCMicroService` project.

The project is a Java 21 application built with Micronaut, gRPC, and Protocol Buffers. It demonstrates how to define a gRPC service, implement the server, invoke the service through a client, handle application errors, log server calls, and test the expected behavior.

The project also provides a foundation for developing reusable Micronaut gRPC services, with centralized error handling, structured call logging, and automated tests.

The documentation is organized into numbered chapters, progressing from the project overview to implementation details, testing, build configuration, execution, and troubleshooting.

For a brief introduction to the project, see the [root README](../README.md).

## 2. Documentation Index

### Introduction

| Document | Description |
|---|---|
| [01 - Project Overview](01-project-overview.md) | Project goals, technologies, and high-level architecture |
| [02 - Project Structure](02-project-structure.md) | Directory layout and responsibilities of the main files |

### Protocol and Implementation

| Document | Description |
|---|---|
| [03 - gRPC Protocol](03-grpc-protocol.md) | Protocol Buffers definitions, messages, and RPC contract |
| [04 - Greeter Service Implementation](04-greeter-service-implementation.md) | Server-side implementation, request validation, and greeting generation |
| [05 - Application Bootstrap](05-application-bootstrap.md) | Micronaut application startup and initialization |
| [06 - Application Configuration](06-application-configuration.md) | gRPC server and client channel configuration |
| [07 - gRPC Client](07-grpc-client.md) | Client implementation, blocking stub, and channel handling |
| [08 - Client Runner](08-client-runner.md) | Example client invocation after application startup |

### Testing, Error Handling, and Execution

| Document | Description |
|---|---|
| [09 - Testing](09-testing.md) | Service tests, error-handling tests, logging interceptor tests, and test execution |
| [10 - Maven Build](10-maven-build.md) | Dependencies, plugins, Protocol Buffers generation, and build lifecycle |
| [11 - Running and Troubleshooting](11-running-and-troubleshooting.md) | Build and execution instructions, logging output, configuration checks, and troubleshooting |

### Quick Start

A visual quick-start guide is available at the [Quick Start Guide](https://frekost.github.io/grpc-micronaut-starter-kit/quick-start/).

It complements the technical documentation with introductory instructions and screenshots.

## 3. Recommended Reading Order

For readers who are new to the project, the recommended order is:

1. Read the project overview and understand its goals.
2. Review the directory structure and identify the main components.
3. Study the Protocol Buffers service contract.
4. Examine the Greeter service implementation and request validation.
5. Understand how Micronaut starts the application.
6. Review the server and client configuration.
7. Examine the gRPC client and client runner.
8. Study the centralized error-handling mechanism and its tests.
9. Review the Maven build configuration.
10. Follow the application execution instructions and troubleshooting guidance, including the logging output.

## 4. Technology Stack

The project uses the following technologies:

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| Micronaut | Application framework and dependency injection |
| gRPC Java | Remote procedure calls |
| Protocol Buffers | Service and message definitions |
| Maven | Dependency management and build automation |
| JUnit 5 | Automated testing |
| Micronaut Test | Integration testing support |
| SLF4J and SLF4J Simple | Logging API and logging backend |

The exact dependency and plugin versions are defined in the root `pom.xml`.

The Maven configuration includes Protocol Buffers and gRPC source generation, allowing Java classes to be generated from the `.proto` service definition.

## 5. Main Components

The application consists of the following main components.

### Protocol and Service

- **Protocol definition:** declares the `Greeter` service, `HelloRequest`, and `HelloReply`.
- **Service implementation:** handles `SayHello` requests, validates the supplied name, and returns greeting messages.
- **gRPC client:** invokes the service through a blocking stub.
- **Client runner:** performs an example invocation after an initial delay.
- **Application bootstrap:** starts the Micronaut application.
- **Application configuration:** defines the gRPC server and client channel settings.

### Error Handling

The project includes a centralized mechanism for handling supported exceptions raised while processing gRPC server calls.

- **`GrpcErrorHandlingInterceptor`:** intercepts server calls and handles supported synchronous exceptions.
- **`GrpcErrorMapper`:** maps application exceptions to gRPC status errors.
- **`GrpcErrorCode`:** defines application-level gRPC error codes.
- **`InvalidRequestException`:** represents invalid requests.
- **`ResourceNotFoundException`:** represents requests for missing resources.
- **`BusinessException`:** represents business-rule failures.

The intended error mappings include:

| Error condition | gRPC status |
|---|---|
| Invalid request | `INVALID_ARGUMENT` |
| Resource not found | `NOT_FOUND` |
| Business-rule failure | `FAILED_PRECONDITION` |
| Unexpected internal error | `INTERNAL` |

The interceptor handles supported synchronous exceptions. Errors occurring in asynchronous operations or explicitly reported through `StreamObserver.onError()` may require handling at the relevant call site.

### Logging

`GrpcLoggingInterceptor` provides centralized logging for incoming gRPC server calls.

It records method names, call completion statuses, and elapsed time using SLF4J with SLF4J Simple as the logging backend. Messages are written to the console.

Successful calls are logged at `INFO` level, calls completed with a non-OK status at `WARN` level, and synchronous exceptions raised while starting a call at `ERROR` level.

Request and response payloads are not logged.

See [11 - Running and Troubleshooting](11-running-and-troubleshooting.md) for logging examples and execution instructions.

### Testing

The test suite covers the main service behavior, error handling, and gRPC logging.

- **`HelloServiceTest`:** verifies the successful Greeter response.
- **`GrpcErrorHandlingTest`:** verifies request validation and error-handling behavior.
- **`GrpcLoggingInterceptorTest`:** tests logging interceptor behavior for successful calls, non-OK completion statuses, and synchronous exceptions raised while starting a call.

The test files are located at the following paths:

```text
src/test/java/org/frekost/test/micro/HelloServiceTest.java
src/test/java/org/frekost/test/micro/error/GrpcErrorHandlingTest.java
src/test/java/org/frekost/test/micro/logging/GrpcLoggingInterceptorTest.java
```

These tests verify the behaviors covered by their respective test cases. The logging interceptor tests do not, by themselves, verify the actual console output or the interceptor's registration in a running Micronaut application.

For test execution instructions, see [09 - Testing](09-testing.md).

## 6. Configuration

The main configuration file is:

`src/main/resources/application.properties`

The current configuration includes:

| Property | Purpose |
|---|---|
| `micronaut.application.name` | Defines the application name |
| `grpc.server.port` | Configures the gRPC server port |
| `grpc.server.enabled` | Enables or disables the gRPC server |
| `grpc.channels.greeter.address` | Defines the target address for the `greeter` client channel |
| `grpc.channels.greeter.plaintext` | Configures plaintext communication for the client channel |

The local development configuration uses port `50052` for the server and `localhost:50052` for the client channel.

Plaintext communication is suitable for a local development example. Production deployments should use an appropriate transport-security configuration when required.

For further details, see [06 - Application Configuration](06-application-configuration.md).

## 7. Build and Test

Run Maven commands from the `GRPCMicroService` project root, where `pom.xml` is located.

### Generate Protocol Buffers and gRPC Sources

```bash
mvn clean generate-sources
```

This command runs the configured source-generation goals.

### Compile the Project

```bash
mvn compile
```

This command compiles the application and generated Java sources.

### Run Tests

```bash
mvn test
```

This command runs the tests configured for the Maven project.

To run an individual test class, use the corresponding command:

```bash
mvn -Dtest=HelloServiceTest test
mvn -Dtest=GrpcErrorHandlingTest test
mvn -Dtest=GrpcLoggingInterceptorTest test
```

### Verify the Project

The recommended project verification command is:

```bash
mvn clean compile verify
```

This command cleans the previous build output and executes the requested Maven lifecycle phases, including compilation, tests, and applicable verification checks configured in the project.

For details about dependencies, plugins, source generation, and the Maven lifecycle, see [10 - Maven Build](10-maven-build.md).

## 8. Running the Application

The application entry point is:

`org.frekost.micro.Application`

The application can be started from an IDE, such as Eclipse, by running `Application.java` as a Java application.

The `HelloClientRunner` bean performs an example gRPC invocation after an initial delay. The example uses the name `Micronaut`.

A successful invocation returns the greeting:

```text
Hello, Micronaut!
```

The logging interceptor records the gRPC method and the completion status and duration. A successful call produces log messages similar to the following:

```text
gRPC call started: method=helloworld.Greeter/SayHello
gRPC call completed: method=helloworld.Greeter/SayHello, status=OK, durationMs=12
```

The duration shown above is an example; the actual value depends on runtime conditions.

Successful compilation and packaging do not guarantee that the application will start correctly in every environment. The server must start successfully, and the client channel must point to the correct endpoint.

For execution instructions and troubleshooting, see [11 - Running and Troubleshooting](11-running-and-troubleshooting.md).

## 9. Project Scope

This documentation covers the `GRPCMicroService` project only.

The separate `GRPCClient` project is outside the scope of this documentation.

## 10. Documentation Maintenance

When changing the application, keep the relevant documentation synchronized with the implementation.

- Update `helloworld.proto` when modifying the gRPC service contract or message definitions.
- Update the service implementation when changing server-side behavior or request validation.
- Update the client when changing request construction or invocation behavior.
- Update `GrpcErrorMapper` and the relevant tests when changing exception-to-status mappings.
- Update `GrpcErrorHandlingInterceptor` documentation when changing its exception-handling behavior.
- Update `GrpcLoggingInterceptor` documentation and tests when changing logging behavior, status levels, or recorded metadata.
- Update `application.properties` documentation when changing server or client channel settings.
- Update the Maven build documentation when changing dependencies, plugins, or source-generation configuration.
- Update the relevant chapters whenever implementation details or build instructions change.
- Regenerate Protocol Buffers and gRPC sources after modifying the `.proto` definition.

Generated source files should not be edited manually.

After changing the application or its configuration, run the relevant tests and verify the build before considering the documentation update complete.