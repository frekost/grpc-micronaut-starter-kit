# 01 - Project Overview

## 1. Introduction

`GRPCMicroService` is a Java application built with Micronaut that demonstrates how to develop and test gRPC services using Protocol Buffers.

The project implements a simple Greeter service. A client sends a name to the service, and the service returns a greeting message.

For example, a request containing the name `Micronaut` produces:

```text
Hello, Micronaut!
```

The service validates incoming requests. If the name is empty or contains only whitespace, the server returns a gRPC `INVALID_ARGUMENT` error.

In addition to basic gRPC communication, the project demonstrates centralized error handling, application-specific exceptions, client configuration, and integration testing.

The project is intended as a starting point for developers who want to understand the main components of a Micronaut-based gRPC application.

## 2. Project Goals

The main goals are to:

- Demonstrate how to implement a gRPC service using Micronaut.
- Define a service contract using Protocol Buffers.
- Generate Java classes from a `.proto` definition.
- Implement a gRPC client that invokes the service.
- Configure the gRPC server and client channel.
- Centralize the handling of supported server-side exceptions.
- Map application exceptions to appropriate gRPC status codes.
- Test successful requests and error-handling behavior.
- Provide a clear project structure that can be extended for other services.

The project separates the communication contract, service implementation, client, error-handling components, application configuration, and tests.

This separation makes the responsibilities of individual components easier to understand and maintain.

## 3. Technologies

The project uses the following technologies:

| Technology | Purpose |
|---|---|
| Java 21 | Programming language and target Java version |
| Micronaut | Application framework and dependency injection |
| gRPC Java | Remote procedure call communication |
| Protocol Buffers | Service contract and message definitions |
| Maven | Dependency management and build automation |
| JUnit 5 | Automated testing |
| Micronaut Test | Integration of Micronaut with the test environment |

The exact dependency and plugin versions are defined in the root `pom.xml`.

The Maven configuration includes a Protocol Buffers plugin that generates Java source files from the protocol definition.

## 4. High-Level Architecture

The project contains a gRPC service and a client within the same Micronaut application.

The service exposes a remote procedure named `SayHello`. The client invokes this procedure through a generated gRPC stub.

The main components are:

- **Protocol definition:** declares the service, request message, and response message.
- **gRPC service implementation:** validates incoming requests and creates greeting responses.
- **gRPC client:** constructs requests, invokes the service, and returns the response message.
- **Client runner:** triggers an example client invocation after an initial delay.
- **Application bootstrap:** starts the Micronaut application.
- **Application configuration:** defines the gRPC server and client channel settings.
- **Error-handling interceptor:** intercepts server calls and handles supported synchronous exceptions.
- **Error mapper:** converts application exceptions into gRPC status errors.
- **Application exceptions:** represent invalid requests, missing resources, and business-rule failures.
- **Integration tests:** verify successful service responses and error-handling behavior.

The components are organized into separate packages under `org.frekost.micro`.

For details about the source tree and component responsibilities, see [02 - Project Structure](02-project-structure.md).

## 5. Communication Flow

The typical communication flow is:

1. The Micronaut application starts.
2. The gRPC server is configured to listen on port `50052`.
3. The client obtains the configured `greeter` channel.
4. The client creates a `HelloRequest` containing a name.
5. The client invokes the `SayHello` RPC.
6. The server validates the request.
7. If the request is valid, the service creates a `HelloReply`.
8. The server sends the response to the client.
9. The client reads the returned greeting message.

For example, a request containing the name `Micronaut` produces:

```text
Hello, Micronaut!
```

If the name is empty or contains only whitespace, the service raises an `InvalidRequestException`. The centralized error-handling mechanism maps this exception to the gRPC status `INVALID_ARGUMENT`.

The client receives a gRPC error rather than a greeting response for that request.

## 6. gRPC Service Contract

The service contract is defined in:

`src/main/proto/helloworld.proto`

It declares a service named `Greeter` with one unary RPC:

```protobuf
rpc SayHello (HelloRequest) returns (HelloReply) {}
```

The request contains a string field named `name`, while the response contains a string field named `message`.

The protocol definition is used to generate the Java classes required by the gRPC client and server.

The generated classes include the service base class, request and response message types, and the client stubs.

Generated source files should not be edited manually.

The protocol definition and source-generation process are described in [03 - gRPC Protocol](03-grpc-protocol.md).

## 7. Error Handling

The project provides a centralized mechanism for mapping supported application exceptions to gRPC errors.

The main components are:

| Component | Responsibility |
|---|---|
| `GrpcErrorHandlingInterceptor` | Intercepts server calls and handles supported synchronous exceptions |
| `GrpcErrorMapper` | Maps exceptions to gRPC status errors |
| `GrpcErrorCode` | Defines application-level gRPC error codes |
| `InvalidRequestException` | Represents invalid requests |
| `ResourceNotFoundException` | Represents missing resources |
| `BusinessException` | Represents business-rule failures |

The intended exception mappings include:

| Error condition | gRPC status |
|---|---|
| Invalid request | `INVALID_ARGUMENT` |
| Resource not found | `NOT_FOUND` |
| Business-rule failure | `FAILED_PRECONDITION` |
| Unexpected internal error | `INTERNAL` |

Existing gRPC status exceptions should retain their original status when handled by the mapper.

The interceptor centralizes the handling of supported synchronous exceptions, reducing the need to repeat exception-to-status conversion logic in individual services.

It does not automatically handle every asynchronous failure. Errors explicitly reported through `StreamObserver.onError()` or raised by asynchronous operations may require handling at the relevant call site.

For further details, see [09 - Testing](09-testing.md) and [11 - Running and Troubleshooting](11-running-and-troubleshooting.md).

## 8. Application Configuration

The main configuration file is:

`src/main/resources/application.properties`

The configuration includes:

- The Micronaut application name: `GRPCHelloWorld`.
- The gRPC server port: `50052`.
- The gRPC server enabled state.
- A client channel named `greeter`.
- A client address of `localhost:50052`.
- Plaintext communication for the configured client channel.

The server and client channel are configured to use the same local endpoint.

These settings describe the intended local development configuration. Successful communication also depends on the application starting correctly and the client being able to reach the configured endpoint.

Plaintext communication is appropriate for this local example. Production deployments should use suitable transport security when required.

The configuration is documented in [06 - Application Configuration](06-application-configuration.md).

## 9. Build and Testing

The project uses Maven to resolve dependencies, generate Protocol Buffers sources, compile the application, and execute tests.

Run the following command from the project root:

```bash
mvn clean compile verify
```

The test suite includes:

- `HelloServiceTest`, which checks the greeting returned for a valid request.
- `GrpcErrorHandlingTest`, which exercises successful requests and invalid input.

For example, a request containing `JUnit` should produce:

```text
Hello, JUnit!
```

An empty or whitespace-only name should result in a gRPC `INVALID_ARGUMENT` error.

The build and test configuration are described in:

- [09 - Testing](09-testing.md)
- [10 - Maven Build](10-maven-build.md)

## 10. Documentation Guide

The `documentation` directory contains the technical documentation for the project.

| Document | Description |
|---|---|
| [01 - Project Overview](01-project-overview.md) | Project goals, technologies, and high-level architecture |
| [02 - Project Structure](02-project-structure.md) | Directory layout and source file responsibilities |
| [03 - gRPC Protocol](03-grpc-protocol.md) | Protocol Buffers messages and service definition |
| [04 - Greeter Service Implementation](04-greeter-service-implementation.md) | Server-side implementation and request validation |
| [05 - Application Bootstrap](05-application-bootstrap.md) | Micronaut application startup |
| [06 - Application Configuration](06-application-configuration.md) | Server and client configuration |
| [07 - gRPC Client](07-grpc-client.md) | Client implementation and blocking stub |
| [08 - Client Runner](08-client-runner.md) | Example client invocation |
| [09 - Testing](09-testing.md) | Integration tests and error-handling verification |
| [10 - Maven Build](10-maven-build.md) | Dependencies, plugins, and build lifecycle |
| [11 - Running and Troubleshooting](11-running-and-troubleshooting.md) | Running the application and troubleshooting common issues |

A visual introduction is also available in the [Quick Start Guide](quick-start/index.html).

## 11. Scope

This documentation covers the `GRPCMicroService` project.

The separate `GRPCClient` project is outside the scope of this document.

The purpose of this overview is to introduce the application, its architecture, and its main capabilities. Detailed implementation and configuration information is provided in the subsequent documents.