# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
./gradlew build        # Compile and package
./gradlew bootRun      # Run the application
./gradlew test         # Run all tests
./gradlew clean build  # Full rebuild
```

- **Java 21** is required (Gradle toolchain enforces this)
- MongoDB must be running on `localhost:27017`
- Kafka must be running on `localhost:9092`

## Architecture

This project is a textbook implementation of **Hexagonal Architecture (Ports & Adapters)** with Spring Boot.

### Layer breakdown

```
com.udemy.hexagonal/
├── application/
│   ├── core/domain/       → Pure domain entities (Customer, Address) — no framework dependencies
│   ├── core/usecase/      → Business logic; implements input ports, depends only on output port interfaces
│   └── ports/
│       ├── in/            → Input port interfaces (contracts that use cases fulfill)
│       └── out/           → Output port interfaces (contracts that adapters fulfill)
├── adapters/
│   ├── in/
│   │   ├── controller/    → REST controllers, request/response DTOs, MapStruct mappers
│   │   └── consumer/      → Kafka consumer (receives CPF validation results)
│   └── out/
│       ├── repository/    → MongoDB adapters, JPA-style entities, MapStruct mappers
│       └── client/        → OpenFeign HTTP client for external address lookup
└── config/                → Spring @Bean wiring — manually instantiates use cases with their adapters
```

### Dependency injection pattern

Use cases are **not** Spring beans — they are plain Java classes instantiated in `config/` classes (e.g., `InsertCustomerConfig`, `DeleteCustomerConfig`). The config classes receive adapter beans as parameters and construct the use case manually, following the hexagonal principle that the application core is framework-agnostic.

### Data flow for customer insertion

1. `CustomerController` receives HTTP POST → maps `InsertCustomerRequest` to `Customer` domain object
2. `InsertCustomerUseCase.insert()` orchestrates:
   - Calls `FindAdressByZipCodeOutputPort` → resolved by `FindAddressZipCodeAdapter` (OpenFeign)
   - Calls `InsertCustomerOutputPort` → resolved by `InsertCustomerAdapter` (MongoDB)
   - Calls `SendCpfForValidationOutputPort` → resolved by `SendCpfValidationAdapter` (Kafka)

### External services

| Service | Config key | Default |
|---------|-----------|---------|
| MongoDB | `spring.data.mongodb.uri` | `mongodb://localhost:27017/udemy-cursos.hexagonal` |
| Kafka broker | `spring.kafka.bootstrap-servers` | `localhost:9092` |
| Address microservice | `hexagonal.client.addressResponse.url` | `http://localhost:8081` |
| CPF validation topic | `hexagonal.message.producer.topic.cpf.validation` | `topic-cpf-validation` |

### Object mapping

MapStruct is used at compile time for all mappings between layers (domain ↔ entity, domain ↔ DTO). Mappers live alongside their adapters or controllers. Lombok is used on domain objects, entities, and DTOs.
