# GRPCMicroService
### Java gRPC Starter Kit for Micronaut

**Build your next Java gRPC service on a solid foundation.**

GRPCMicroService is a Maven-based starter project demonstrating how to build and run a gRPC microservice using Java 17, Micronaut 4, gRPC Java, and Protocol Buffers.

It provides a structured starting point for developers who want to learn gRPC or use an existing project as the foundation for their own applications.

**Spend less time setting up the project and more time building your gRPC services.**

[**Get the Complete Starter Kit**](https://frekost.gumroad.com/l/grpc2micronaut)

---

## 🛠 Technology Stack

- **Java 17** — Application development.
- **Micronaut 4** — Application framework.
- **gRPC Java** — Remote procedure calls.
- **Protocol Buffers** — Service and message definitions.
- **Apache Maven** — Dependency management and build automation.
- **JUnit** — Automated testing.

## ✨ What's Included

The complete starter kit includes:

- A working gRPC `Greeter` service implementation.
- Protocol Buffer definitions for the service API.
- Maven configuration for generating Java and gRPC source files.
- gRPC server and client configuration.
- A client implementation and example runner.
- An automated service test.
- Eleven technical documentation guides.
- Instructions for building, running, and troubleshooting the application.

## 📁 Project Structure

The project follows a Maven-standard layout, separating application code, protocol definitions, configuration, tests, and technical documentation.

```text
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
```

### Key Components

| Component | Description |
|---|---|
| `Application.java` | Bootstraps the Micronaut application. |
| `HelloServiceImpl.java` | Implements the gRPC Greeter service. |
| `HelloClient.java` | Contains the gRPC client logic. |
| `HelloClientRunner.java` | Runs the example client. |
| `helloworld.proto` | Defines the gRPC service contract and message types. |
| `application.properties` | Contains application and gRPC configuration. |
| `HelloServiceTest.java` | Tests the service behavior. |
| `pom.xml` | Defines Maven dependencies, build plugins, and Protocol Buffers source generation. |
| `documentation/` | Contains eleven technical guides covering the project and its development workflow. |

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

This command cleans the build output, generates the required sources, compiles the project, and runs the configured verification lifecycle, including tests.

### Run the Application

Import the project into your preferred Java IDE, or follow the launch instructions in the included documentation.

The sample application starts a gRPC server on port `50052` and demonstrates a client request to the `Greeter` service.

Refer to the technical documentation for configuration details and troubleshooting.

## 📚 Documentation

The included documentation consists of eleven technical guides covering:

1. Project overview.
2. Project structure.
3. gRPC protocol definitions.
4. Greeter service implementation.
5. Application bootstrap.
6. Application configuration.
7. gRPC client implementation.
8. Client runner.
9. Testing.
10. Maven build.
11. Running and troubleshooting.

These guides help developers understand the project structure and follow the main steps required to build and run the application.

## 📄 License

GRPCMicroService is distributed under the commercial license included in the purchased package.

The license permits use and modification of the project, including as a foundation for commercial applications, subject to its terms. Redistribution or resale of the original project as a standalone product is not permitted.

Third-party dependencies and components may be subject to separate licenses. Please review the included `LICENSE` file before use.

## 📦 Get the Complete Starter Kit

Looking for a structured starting point for your next Java gRPC project?

The complete commercial package includes the project source code, Maven build configuration, gRPC definitions, example implementation, tests, and technical documentation.

**Product:** GRPCMicroService 1.0.0  
**Publisher:** frekost  
**Price:** $9 USD

### 👉 [Get GRPCMicroService on Gumroad](https://frekost.gumroad.com/l/grpc2micronaut)

Please review the product description and license before purchasing.

## ⚠️ Important Notes

- This repository is a product information page and does not contain the complete commercial source package.
- The complete starter kit is available through the product page above.
- The project is intended as a development and learning foundation. Review security, configuration, observability, and operational requirements before using it in production.

---

**Built for Java developers by Frekost.**
