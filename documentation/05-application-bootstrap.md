# Application Bootstrap

## 1. Overview

The application entry point is defined in
`src/main/java/org/frekost/micro/Application.java`.

The `Application` class starts the Micronaut application and
reads the configured gRPC server port from the application
environment.

Its responsibilities are:

- Start the Micronaut application.
- Retrieve the Micronaut `Environment` bean.
- Read the `grpc.server.port` configuration property.
- Print the resolved port value to standard output.

## 2. Package and Imports

The class belongs to the following Java package:

```java
package org.frekost.micro;
```

It uses the following Micronaut classes:

- `io.micronaut.runtime.Micronaut`: provides the application
  startup mechanism.
- `io.micronaut.context.env.Environment`: provides access to
  the application's configuration environment.

## 3. Application Entry Point

The application starts from the standard Java `main` method:

```java
public static void main(String[] args)
```

The `args` parameter contains the command-line arguments passed
to the Java application.

These arguments are forwarded to Micronaut during startup.

## 4. Starting Micronaut

The application is started with the following statement:

```java
var ctx = Micronaut.run(Application.class, args);
```

The `Micronaut.run` method starts the application using
`Application.class` as the application class and forwards the
command-line arguments.

The returned application context is stored in `ctx`.

The context provides access to the beans managed by Micronaut.

## 5. Accessing the Application Environment

After startup, the application retrieves the `Environment` bean:

```java
var env = ctx.getBean(Environment.class);
```

The `Environment` abstraction provides access to configuration
properties available to the running application.

The code uses this bean to read the configured gRPC server port.

## 6. Reading the gRPC Server Port

The application retrieves the `grpc.server.port` property:

```java
env.getProperty("grpc.server.port", Integer.class).orElse(-1)
```

The expression requests the property as an `Integer`.

Its behavior is as follows:

- If the property is available and can be converted to an
  integer, its value is returned.
- If the property is absent, the expression returns `-1`.

The `-1` value is a fallback used by this property lookup.
It does not, by itself, configure the gRPC server to listen
on port `-1`.

Reading the property is separate from configuring the server.
The effective server configuration depends on the Micronaut
gRPC configuration and the components that consume it.

## 7. Console Output

The application prints the resolved property value using
`System.out.println`.

The diagnostic message uses the Italian text
`grpc.server.port letto`, meaning that the gRPC server port
has been read.

For example, if the property resolves to `50052`, the output is:

```text
📘 grpc.server.port letto: 50052
```

If the property is absent, the output is:

```text
📘 grpc.server.port letto: -1
```

The output is intended to help inspect the property value
available to the application after startup.

It is not part of the gRPC protocol and is not sent to clients.

## 8. Startup Sequence

The startup logic follows this sequence:

1. The Java runtime invokes `Application.main`.
2. `Micronaut.run` starts the application.
3. The application context is returned.
4. The `Environment` bean is retrieved from the context.
5. The `grpc.server.port` property is read as an integer.
6. The resolved value, or `-1` if the property is absent, is
   printed to standard output.

## 9. Implementation Notes

### 9.1 Diagnostic Output

The class uses `System.out.println` to display the configured
port property.

This is useful for simple diagnostics during development.
For production applications, a logging framework can provide
better control over log levels and output destinations.

### 9.2 Configuration and Runtime Behavior

Reading a configuration property does not itself configure
the gRPC server.

The effective server behavior depends on the Micronaut gRPC
configuration and the components that consume the relevant
properties.

The server configuration is documented separately in
`application.properties` and the application configuration
documentation.

### 9.3 Application Context

The application retrieves the `Environment` bean from the
context returned by `Micronaut.run`.

This provides access to the configuration environment
associated with the running Micronaut application.

## 10. Summary

`Application` is the Java entry point for the Micronaut
application.

It starts Micronaut, retrieves the application environment,
and prints the resolved value of `grpc.server.port`.

The port lookup provides diagnostic information. The actual
gRPC server configuration is defined separately and should
be verified against the application's configuration files.