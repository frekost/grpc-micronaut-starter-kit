# GRPCMicroService
### Java gRPC Starter Kit for Micronaut

**Build your next Java gRPC service on a solid foundation.**

GRPCMicroService is a Maven-based starter project demonstrating how to build and run a gRPC microservice with Java 17, Micronaut 4, and Protocol Buffers.

It provides a practical starting point for developers who want to explore gRPC or use a structured project as the foundation for their own applications.

## 🛠 Technology Stack

- Java 17
- Micronaut 4
- gRPC Java
- Protocol Buffers
- Apache Maven
- JUnit

## ✨ What's Included

- A `Greeter` gRPC service implementation.
- Protocol Buffer definitions for the service API.
- Maven configuration for generating Java and gRPC source files.
- gRPC server and client configuration.
- A client runner demonstrating a request and response.
- An integration test.
- Technical documentation.
- Instructions for building and running the project.

📁 Project Structure

The project follows a Maven-standard layout and separates application code, gRPC protocol definitions, configuration, tests, and technical documentation.

GRPCMicroService/
├── documentation/
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
│   └── README.md
├── src/
│   ├── main/
│   │   ├── java/org/frekost/micro/
│   │   │   ├── Application.java
│   │   │   ├── client/
│   │   │   │   ├── HelloClient.java
│   │   │   │   └── HelloClientRunner.java
│   │   │   └── services/grpc/greeter/
│   │   │       └── HelloServiceImpl.java
│   │   ├── proto/
│   │   │   └── helloworld.proto
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/org/frekost/test/micro/
│       │   └── HelloServiceTest.java
│       └── resources/
├── pom.xml
├── README.md
├── LICENSE
└── THIRD-PARTY-NOTICES.txt

Key Components

- "Application.java" — Entry point for the Micronaut application.
- "HelloServiceImpl.java" — Implements the gRPC Greeter service.
- "HelloClient.java" — Contains the gRPC client logic.
- "HelloClientRunner.java" — Runs the example client as part of the application.
- "helloworld.proto" — Defines the gRPC service contract and its message types.
- "application.properties" — Contains application and gRPC configuration.
- "HelloServiceTest.java" — Tests the service behavior.
- "pom.xml" — Defines Maven dependencies, build plugins, and Protocol Buffers source generation.
- "documentation/" — Provides eleven technical guides covering architecture, implementation, configuration, testing, building, and troubleshooting.

Build and Verify

From the project root, run:

mvn clean verify

This command cleans the build output, generates the required sources, compiles the project, and runs the configured verification lifecycle, including tests.

Want the complete starter kit?

The full source project, tests, Maven configuration, and technical documentation are available here:

"Get GRPCMicroService on Gumroad" (https://frekost.gumroad.com/l/grpc2micronaut)

## 🚀 Getting Started

### Requirements

- JDK 17 or compatible.
- Apache Maven.
- Internet access for Maven dependency resolution.
- An IDE such as Eclipse or IntelliJ IDEA (optional).

### Build and Verify

After obtaining the complete project source package, open a terminal in the project directory and run:

```bash
mvn clean verify
```

This command cleans the project, generates the required sources, compiles the code, and runs the verification lifecycle, including the configured tests.

### Run the Application

Import the project into your preferred Java IDE, or use the launch instructions in the included documentation.

The sample application starts a gRPC server on port `50052` and demonstrates a request to the `Greeter` service.

Check the full project documentation for configuration details and troubleshooting.

## 📦 Get the Complete Starter Kit

**Looking for the complete source project and documentation?**

The commercial package includes the project source code, Maven build configuration, gRPC definitions, example implementation, tests, and technical documentation.

### 👉 [Get GRPCMicroService on Gumroad](https://frekost.gumroad.com/l/grpc2micronaut)

**Product:** GRPCMicroService 1.0.0  
**Publisher:** Frekost

Please review the product page and the included license before purchasing.

## 📚 Documentation

The complete documentation is included with the commercial package. It covers:

- Project structure and architecture.
- The gRPC protocol definition.
- The Greeter service implementation.
- Application startup and configuration.
- Client configuration and execution.
- Testing and Maven build instructions.
- Running the application and troubleshooting.

## 📄 License

The complete commercial package is distributed under the terms of the `LICENSE` file included with the product. Usage and modification are subject to that license.

Third-party dependencies and components may be subject to separate licenses.

## ⚠️ Important Notes

- This repository is a product information page, not the complete commercial source package.
- The complete source code is available through the product page above.
- The starter kit is intended as a foundation for development and learning. Review security, configuration, observability, and operational requirements before using it in production.

---

**Built for Java developers by Frekost.**
