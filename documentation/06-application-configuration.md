# Application Configuration

## 1. Overview

The application configuration is defined in
`src/main/resources/application.properties`.

It specifies the Micronaut application name, configures the gRPC
server, and defines a client channel named `greeter`.

The configuration is divided into three main areas:

- Application identification.
- gRPC server configuration.
- gRPC client channel configuration.

## 2. Application Name

The application name is configured as follows:

```properties
micronaut.application.name=GRPCHelloWorld
```

The value identifies the Micronaut application as `GRPCHelloWorld`.

This property provides an application name for Micronaut.
It does not determine the network address or port of the gRPC
server.

## 3. gRPC Server Configuration

The server-related properties are:

```properties
grpc.server.port=50052
grpc.server.enabled=true
```

### 3.1 Server Port

The property `grpc.server.port` is set to `50052`.

This specifies the configured port for the gRPC server.

The `Application` class also reads this property and prints
its value to standard output during startup.

Reading the property does not independently confirm that the
server has successfully bound to the configured port.

### 3.2 Server Enablement

The property `grpc.server.enabled` is set to `true`.

This setting enables the gRPC server through the corresponding
configuration supported by the Micronaut gRPC integration.

The effective server behavior depends on the installed
Micronaut gRPC components and their configuration handling.

## 4. gRPC Client Channel Configuration

The client-related properties are:

```properties
grpc.channels.greeter.plaintext=true
grpc.channels.greeter.address=localhost:50052
```

These properties configure a gRPC client channel named `greeter`.

### 4.1 Channel Name

The channel is identified by the name `greeter`.

Application components can refer to this channel name when
obtaining or injecting the corresponding gRPC client through
the Micronaut gRPC integration.

For example, `HelloClient` uses the following annotation:

```java
@GrpcChannel("greeter")
```

This connects the client component to the channel configured
under `grpc.channels.greeter`.

### 4.2 Server Address

The property `grpc.channels.greeter.address` specifies:

```text
localhost:50052
```

The client channel is configured to connect to port `50052`
on the local host.

This address corresponds to the port configured for the gRPC
server in the same properties file.

The configuration is suitable when the client and server run
in an environment where `localhost` resolves to the same
network namespace as the server.

### 4.3 Plaintext Communication

The property `grpc.channels.greeter.plaintext` is set to `true`.

This configures the `greeter` client channel to use plaintext
communication rather than TLS-protected transport.

Plaintext communication does not provide transport encryption.

This setting may be appropriate for local development and
testing. Deployment environments should be evaluated separately
according to their security requirements.

## 5. Client and Server Relationship

The configuration establishes the following relationship:

- The gRPC server is configured to use port `50052`.
- The `greeter` client channel targets `localhost:50052`.
- The client channel uses plaintext communication.
- Both configurations are defined in the same properties file.

The following diagram illustrates the configured relationship:

```mermaid
flowchart LR
    Client["gRPC Client Channel<br/>greeter"]
    Server["Micronaut gRPC Server<br/>Port 50052"]

    Client -->|"localhost:50052<br/>Plaintext"| Server
```

This diagram represents the configured connection. It does not
by itself prove that a client invocation succeeds or that the
server is reachable at runtime.

## 6. Relationship with Application.java

The `Application` class reads the following property:

```properties
grpc.server.port=50052
```

It requests the property as an integer and prints the resulting
value to standard output.

With the configuration shown in this file, the expected
diagnostic output is:

```text
📘 grpc.server.port letto: 50052
```

This output confirms the value retrieved from the application
environment. It does not independently verify that the server
has successfully bound to the configured port.

## 7. Configuration Considerations

### 7.1 Local Development

The address `localhost:50052` is suitable when the client and
server run in an environment where `localhost` resolves to the
same machine or network namespace as the server.

If the client runs in another container or on another machine,
the address may need to be changed to the appropriate hostname
or network address.

### 7.2 Transport Security

The `plaintext=true` setting configures the client channel
without TLS transport encryption.

For environments requiring encrypted communication, the client
and server security configuration should be reviewed together.

### 7.3 Configuration Verification

The properties document the intended server and client settings.

Runtime verification is still required to establish that the
server starts successfully, listens on the expected port, and
accepts connections from the client.

## 8. Summary

The `application.properties` file configures the Micronaut
application as `GRPCHelloWorld`.

It enables the gRPC server on port `50052` and defines a client
channel named `greeter` that targets `localhost:50052` using
plaintext communication.

These settings describe the configured local client-server
relationship for the gRPC application.