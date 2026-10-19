# Maven Build Configuration

## 1. Overview

The `pom.xml` file defines the build configuration for the
`GRPCMicroService` project.

Apache Maven manages the project's dependencies, compiles Java source
files, generates Java classes from the Protocol Buffers definition,
executes tests, and packages the application.

The project uses the following main technologies and components:

- Java 21
- Micronaut Platform 4.10.21
- Micronaut gRPC 4.13.0
- gRPC Java 1.75.0
- Protocol Buffers 3.25.8
- JUnit 5.14.0
- Apache Maven

The POM centralizes version properties and imports dependency
management BOMs to maintain a consistent set of framework dependencies.

## 2. Project Coordinates

The project declares the following Maven coordinates:

```xml
<groupId>org.frekost.micro</groupId>
<artifactId>grpc-micro-service</artifactId>
<version>1.0.0</version>
<packaging>jar</packaging>
```

These values identify the project within the Maven ecosystem.

- `groupId`: identifies the project group or organization.
- `artifactId`: identifies the project artifact.
- `version`: specifies the current project version.
- `packaging`: specifies that Maven produces a JAR artifact.

The resulting artifact is normally named:

`grpc-micro-service-1.0.0.jar`

The artifact is produced under the `target/` directory when the
package phase completes successfully.

## 3. Build Properties

The POM defines properties for source encoding, Java compilation,
framework versions, protocol libraries, testing, and Maven plugins.

The principal properties are:

```xml
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

<maven.compiler.release>21</maven.compiler.release>

<micronaut.platform.version>4.10.21</micronaut.platform.version>
<micronaut.grpc.version>4.13.0</micronaut.grpc.version>

<grpc.version>1.75.0</grpc.version>
<protobuf.version>3.25.8</protobuf.version>

<micronaut.test.version>4.10.3</micronaut.test.version>
<junit.version>5.14.0</junit.version>

<maven.compiler.plugin.version>3.13.0</maven.compiler.plugin.version>
<maven.surefire.plugin.version>3.5.4</maven.surefire.plugin.version>
```

These properties centralize important configuration values and
make version changes easier to maintain.

### 3.1 Source Encoding

The `project.build.sourceEncoding` property specifies UTF-8 as the
source encoding.

Using an explicit encoding helps ensure that source files are
processed consistently across development environments.

### 3.2 Java 21 Configuration

The project targets Java 21.

The POM configures the Java compiler using:

```xml
<maven.compiler.release>21</maven.compiler.release>
```

The `maven-compiler-plugin` uses this property through its
`release` configuration:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>${maven.compiler.plugin.version}</version>
    <configuration>
        <release>${maven.compiler.release}</release>
        <parameters>true</parameters>
    </configuration>
</plugin>
```

The `release` option configures the Java language rules, generated
class-file version, and Java platform APIs available to the compiler.

The `parameters` option enables the generation of method parameter
metadata, which can be useful to framework features that inspect
parameter names.

The compiler plugin version is managed through:

`${maven.compiler.plugin.version}`

Its configured value is `3.13.0`.

A JDK compatible with Java 21 must be available to Maven. The
environment can be checked with:

```bash
java -version
mvn -version
```

The second command reports the Java version Maven uses to run the
build.

## 4. Dependency Management

The POM uses Maven's `<dependencyManagement>` section to centralize
versions for Micronaut and Micronaut gRPC dependencies.

It imports two BOMs:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.micronaut.platform</groupId>
            <artifactId>micronaut-platform</artifactId>
            <version>${micronaut.platform.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <dependency>
            <groupId>io.micronaut.grpc</groupId>
            <artifactId>micronaut-grpc-bom</artifactId>
            <version>${micronaut.grpc.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 4.1 Micronaut Platform BOM

The Micronaut Platform BOM is imported using:

`${micronaut.platform.version}`

The configured version is `4.10.21`.

The BOM manages versions for the Micronaut modules covered by
that platform release.

This allows dependencies such as `micronaut-core` and
`micronaut-runtime` to omit explicit version declarations when
their versions are managed by the imported BOM.

### 4.2 Micronaut gRPC BOM

The Micronaut gRPC BOM is imported using:

`${micronaut.grpc.version}`

The configured version is `4.13.0`.

It manages versions for the Micronaut gRPC modules covered by
that BOM.

Using dependency management reduces the need to specify versions
individually for each managed dependency.

A BOM import does not automatically add all of its managed
dependencies to the application. Dependencies must still be
declared in the `<dependencies>` section when the application
needs them.

## 5. Application Dependencies

The `<dependencies>` section declares the libraries used by the
application and its tests.

### 5.1 Annotation APIs

The project declares:

- `jakarta.annotation:jakarta.annotation-api`
- `javax.annotation:javax.annotation-api:1.3.2`

The Jakarta annotation API supports code using the `jakarta.*`
namespace.

The older `javax.annotation-api` dependency provides compatibility
for generated gRPC code that refers to `javax.annotation.Generated`.

These are different namespaces and serve different compatibility
requirements.

### 5.2 Micronaut Core

The project declares the following Micronaut dependencies:

- `micronaut-core`
- `micronaut-runtime`
- `micronaut-inject`
- `micronaut-inject-java`
- `micronaut-context`
- `micronaut-validation`

The `micronaut-inject-java` dependency uses `provided` scope because
it supports compile-time annotation processing rather than serving
as a normal application runtime dependency.

These modules provide framework functionality such as:

- Core framework APIs.
- Application startup and runtime support.
- Dependency injection.
- Application context management.
- Validation support.

The application uses Micronaut to manage components such as
`HelloClient`, `HelloClientRunner`, and `HelloServiceImpl`.

The versions of managed dependencies are determined through the
imported Micronaut Platform BOM unless explicitly overridden.

### 5.3 Micronaut gRPC Integration

The project declares two gRPC integration dependencies:

- `micronaut-grpc-server-runtime`
- `micronaut-grpc-client-runtime`

The server runtime provides Micronaut integration for the gRPC
server. It supports the `@GrpcService` component implemented by
`HelloServiceImpl`.

The client runtime provides Micronaut integration for gRPC clients.
It supports the configured `greeter` channel injected into
`HelloClient` through `@GrpcChannel("greeter")`.

Their versions are managed through the imported Micronaut gRPC BOM.

### 5.4 gRPC Libraries

The project declares the following gRPC dependencies:

- `grpc-netty`
- `grpc-protobuf`
- `grpc-stub`

Their version is controlled by:

`${grpc.version}`

The configured version is `1.75.0`.

Their principal roles are:

- `grpc-netty`: provides a Netty-based gRPC transport implementation.
- `grpc-protobuf`: integrates gRPC with Protocol Buffers messages
  and serialization.
- `grpc-stub`: provides the gRPC stub APIs used by clients and
  generated service classes.

The application uses a blocking stub to make synchronous calls
to the greeting service.

### 5.5 Logging

The project declares:

`org.slf4j:slf4j-simple:2.0.17`

This dependency provides a simple SLF4J logging implementation.

The actual logging behavior depends on the runtime classpath and
the logging configuration used by the application.

### 5.6 Testing Dependencies

The project declares the following test dependencies:

- `micronaut-test-junit5`
- `junit-jupiter-api`
- `junit-jupiter-engine`

These dependencies use test scope.

The Micronaut testing dependency uses:

`${micronaut.test.version}`

The configured version is `4.10.3`.

The JUnit Jupiter dependencies use:

`${junit.version}`

The configured version is `5.14.0`.

Their roles are:

- `micronaut-test-junit5`: integrates Micronaut with JUnit 5.
- `junit-jupiter-api`: provides test annotations and assertions.
- `junit-jupiter-engine`: provides the JUnit Jupiter test execution
  engine.

The project uses these dependencies in:

- `HelloServiceTest`
- `GrpcErrorHandlingTest`

The first test verifies the expected greeting for a valid name.
The second covers selected invalid-input and error-handling scenarios.

## 6. Operating System Detection

The build declares the following Maven extension:

```xml
<extension>
    <groupId>kr.motd.maven</groupId>
    <artifactId>os-maven-plugin</artifactId>
    <version>1.7.0</version>
</extension>
```

The `os-maven-plugin` detects operating system and architecture
information and exposes properties that can be used by other
build plugins.

The Protocol Buffers code-generation configuration uses:

`${os.detected.classifier}`

This property selects executable artifacts compatible with the
detected operating system and architecture.

For example, the classifier in the development environment was
reported as:

`linux-x86_64`

The value can differ on other operating systems and architectures.

## 7. Protocol Buffers and gRPC Code Generation

The project uses `protobuf-maven-plugin` version `0.6.1` to generate
Java source files from the Protocol Buffers definition.

The plugin is configured with two executable artifacts:

```xml
<protocArtifact>
    com.google.protobuf:protoc:${protobuf.version}:exe:${os.detected.classifier}
</protocArtifact>

<pluginId>grpc-java</pluginId>

<pluginArtifact>
    io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}
</pluginArtifact>
```

### 7.1 Protocol Buffers Compiler

The `protocArtifact` property identifies the Protocol Buffers
compiler executable.

Its version is controlled by `${protobuf.version}`, which is set
to `3.25.8`.

The compiler processes:

`src/main/proto/helloworld.proto`

It generates Java classes representing the Protocol Buffers
messages, including:

- `HelloRequest`
- `HelloReply`

### 7.2 gRPC Java Code Generator

The `pluginArtifact` property identifies the gRPC Java
code-generation executable.

Its version is controlled by `${grpc.version}`, which is set
to `1.75.0`.

The `grpc-java` generator creates gRPC-specific Java classes,
including the service base class and client stubs.

The application uses generated classes from:

`org.frekost.micro.services.greeter`

Examples include:

- `GreeterGrpc`
- `HelloRequest`
- `HelloReply`

The generated Java package is determined by the options declared
in `helloworld.proto`.

### 7.3 Configured Goals

The plugin declares two goals:

```xml
<goals>
    <goal>compile</goal>
    <goal>compile-custom</goal>
</goals>
```

The `compile` goal generates Java sources from Protocol Buffers
message definitions.

The `compile-custom` goal invokes the configured custom generator,
which in this project is the gRPC Java generator.

Generated sources are build outputs. They should not normally be
edited manually because subsequent builds can regenerate them.

## 8. Maven Compiler Plugin

The project uses:

`org.apache.maven.plugins:maven-compiler-plugin:3.13.0`

The plugin compiles the application's Java sources and uses the
`release` setting to target Java 21.

The relevant configuration is:

```xml
<configuration>
    <release>${maven.compiler.release}</release>
    <parameters>true</parameters>
</configuration>
```

The `release` value comes from the `maven.compiler.release`
property, set to `21`.

The plugin participates in the normal Maven compilation lifecycle.

## 9. Maven Surefire Plugin

The project uses Maven Surefire version `3.5.4`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${maven.surefire.plugin.version}</version>

    <configuration>
        <useModulePath>false</useModulePath>
    </configuration>
</plugin>
```

Surefire runs tests during the Maven test phase.

The `useModulePath` setting is disabled, so Surefire does not use
the Java module path to run the tests.

The test dependencies provide the JUnit Jupiter API and execution
engine.

## 10. Maven Build Lifecycle

Run Maven commands from the `GRPCMicroService` project directory.

Maven executes lifecycle phases in order. Invoking a later phase
also runs the preceding phases in the same lifecycle.

### 10.1 Generate Sources

```bash
mvn clean generate-sources
```

This command removes previous build output and executes the
lifecycle through `generate-sources`.

The configured Protocol Buffers plugin generates Java message
classes and gRPC service classes.

### 10.2 Compile the Project

```bash
mvn compile
```

This command executes the lifecycle through the compile phase.

It includes the earlier lifecycle phases and compiles the main
Java sources, including generated sources registered by the
code-generation plugin.

### 10.3 Run Tests

```bash
mvn test
```

This command executes the lifecycle through the test phase.

It compiles the required sources and runs tests selected by
Maven Surefire.

### 10.4 Package the Application

```bash
mvn package
```

This command executes the lifecycle through the package phase.

For a JAR project, Maven normally creates the application artifact
under `target/`.

### 10.5 Clean and Package

```bash
mvn clean package
```

This command removes previous build output and performs a new
package build.

### 10.6 Full Verification Build

The project's preferred build command is:

```bash
mvn clean compile verify
```

This command cleans previous build output and executes the requested
Maven lifecycle phases.

The `verify` phase occurs after compilation and testing phases in
the standard Maven lifecycle. As a result, the command also runs
the earlier phases needed to reach `verify`.

This is the preferred command for checking that the project
compiles and passes the configured verification steps.

## 11. Generated Files and Source Control

Protocol Buffers and gRPC Java classes are generated during the
build.

Generated sources and compiled classes are normally placed under
the Maven `target/` directory.

The `target/` directory contains build output rather than the
primary maintained application source code.

The main files defining the service contract and implementation are:

- `src/main/proto/helloworld.proto`
- `src/main/java/org/frekost/micro/services/grpc/greeter/HelloServiceImpl.java`
- `src/main/java/org/frekost/micro/client/HelloClient.java`

Changes to the service contract should be made in the Protocol
Buffers definition, followed by source generation and compilation.

Generated classes should not be treated as the authoritative
source of the service contract.

## 12. Version Compatibility Considerations

The POM explicitly defines versions for Java compilation, Micronaut,
Micronaut gRPC, gRPC, Protocol Buffers, testing dependencies, and
Maven plugins.

When changing versions, verify compatibility between:

- Micronaut gRPC integration libraries and the Micronaut Platform.
- The Protocol Buffers compiler and the Java runtime library.
- The gRPC Java generator and the gRPC runtime libraries.
- The Java compiler configuration and the installed JDK 21.
- The Micronaut testing integration and the JUnit Jupiter version.

A successful source-generation build confirms that the configured
generation step completed. It does not, by itself, prove that the
application starts correctly or that all tests pass.

Likewise, a successful compilation does not establish that the
gRPC client can connect to the server at runtime.

## 13. Summary

The `pom.xml` centralizes the build configuration for
`GRPCMicroService`.

It defines the project coordinates, Java 21 compiler settings,
Micronaut BOM imports, application dependencies, testing libraries,
operating system detection, and Protocol Buffers/gRPC source
generation.

Maven provides a repeatable build process that generates the
required Java sources, compiles the application, executes tests,
and performs the configured verification steps.