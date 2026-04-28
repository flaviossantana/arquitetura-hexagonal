# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build, Run and Test Commands

```bash
./gradlew clean build   # Full rebuild + tests
./gradlew build         # Compile and package
./gradlew bootRun       # Run the application
./gradlew test          # Run all tests
```

### Prerequisites

- Java 21 (enforced by Gradle toolchain)
- Docker + Docker Compose (for local infra)
- Local services required by the app:
  - MongoDB on `localhost:27017`
  - Kafka broker on `localhost:9092`

## Current Tech Stack

### Runtime stack

- Java 21
- Spring Boot `3.5.11`
- Spring Cloud `2025.0.0` (OpenFeign)
- Spring Web (REST)
- Spring Validation (Jakarta Validation)
- Spring Data MongoDB
- Spring Kafka
- MongoDB
- Apache Kafka
- MapStruct `1.5.3.Final`
- Lombok + `lombok-mapstruct-binding`

### Test stack

- JUnit 5 (via `spring-boot-starter-test`)
- Spring Boot Test (`@SpringBootTest`)
- Spring Kafka Test
- ArchUnit JUnit 5 (`com.tngtech.archunit:archunit-junit5:1.4.0`)

## Architecture and Implementation Pattern

This project follows Hexagonal Architecture (Ports & Adapters):

- `application/core/domain`: pure domain classes, no framework dependency
- `application/core/usecase`: business rules, implementing input ports
- `application/ports/in`: input contracts consumed by adapters in
- `application/ports/out`: output contracts consumed by use cases
- `adapters/in`: entrypoints (REST controllers, Kafka consumers)
- `adapters/out`: external integrations (Mongo repository adapters, OpenFeign, Kafka producer)
- `config`: composition root (`@Configuration` + `@Bean`)

### Dependency rule used in code

- Use cases are plain Java classes (not Spring components).
- Spring creates use case instances in `config/*Config.java`.
- Use cases depend on `ports/out` interfaces, never on adapter implementations.

## Code Conventions Used in This Project

- Naming by role:
  - `*UseCase`, `*InputPort`, `*OutputPort`
  - `*Adapter`, `*Controller`, `*Consumer`, `*Config`
  - `*Mapper`, `*Entity`, `*Request`, `*Response`
- Mapping between layers must be done through MapStruct interfaces (`@Mapper(componentModel = "spring")`).
- Input validation happens at API boundary (`@Valid` in controller methods).
- Kafka topics and integration endpoints are always configured via `application.properties` (no hardcoded topic names).
- Logging pattern uses `correlationID` in MDC to track request flow.

## Test Strategy and Test Pattern

### 1) Application smoke test

- `HexagonalApplicationTests`: validates Spring context startup (`contextLoads`).

### 2) Architecture tests (ArchUnit)

Located under `src/test/java/com/udemy/hexagonal/architecture`.

- `NamingConventionTest`:
  - Enforces class suffix -> package convention.
- `DependencyRulesTest`:
  - Prevents `usecase` from depending on `adapters`.
  - Keeps domain pure from adapter/config/port dependencies.
  - Prevents `adapters.in` from depending on `adapters.out`.
- `LayeredArchitectureTest`:
  - Enforces allowed access between Adapters In/Out, Use Cases, Ports In/Out, and Config.
- `SpringRulesTest`:
  - Prevents Spring dependencies in domain.
  - Prevents web dependencies in use case layer.

These tests are mandatory guardrails for hexagonal boundaries and should evolve with package/naming changes.

## Configuration Keys in Use

From `src/main/resources/application.properties`:

- Address service URL: `hexagonal.client.address.response.url`
- Kafka bootstrap server: `spring.kafka.bootstrap-servers`
- Kafka consumer group: `spring.kafka.consumer.group-id`
- CPF topics:
  - `hexagonal.message.producer.topic.cpf.validation`
  - `hexagonal.message.producer.topic.cpf.validated`
- MongoDB settings:
  - `spring.data.mongodb.host`
  - `spring.data.mongodb.port`
  - `spring.data.mongodb.database`
  - `spring.data.mongodb.username`
  - `spring.data.mongodb.password`
  - `spring.data.mongodb.authentication-database`

## Docker Local Environment

Local infra file: `docker-local/docker-compose.yml`

### Containers

- `zookeeper`
  - Image: `confluentinc/cp-zookeeper:latest`
  - Port: `2181`
- `kafka`
  - Image: `confluentinc/cp-kafka:7.8.5`
  - Ports: `9092` (host), `29092` (internal advertised listener)
  - Depends on `zookeeper`
- `kafdrop`
  - Image: `obsidiandynamics/kafdrop:latest`
  - Port: `9000`
  - Depends on `kafka`
- `mongo`
  - Image: `mongo`
  - Port: `27017`
  - Credentials:
    - user: `root`
    - password: `example`
- `mongo-express`
  - Image: `mongo-express`
  - Port: `8083`
  - Connected to `mongo` using `mongodb://root:example@mongo:27017/`

### Network

- Bridge network: `broker-kafka`

### Common local workflow

```bash
cd docker-local
docker compose up -d
```

Useful local endpoints after startup:

- Kafdrop UI: `http://localhost:9000`
- Mongo Express UI: `http://localhost:8083`
