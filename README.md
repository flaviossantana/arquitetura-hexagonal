# 🔷 Arquitetura Hexagonal com Spring Boot

Implementação prática da **Arquitetura Hexagonal** (Ports & Adapters) utilizando Java, Spring Boot, MongoDB e Kafka. O objetivo é demonstrar um sistema altamente testável, desacoplado e focado nas regras de negócio.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| ☕ Java | 21 | Linguagem |
| 🍃 Spring Boot | 3.5.11 | Framework base |
| ☁️ Spring Cloud | 2025.0.0 | OpenFeign (HTTP client declarativo) |
| 🍃 MongoDB | — | Persistência de documentos |
| 📨 Apache Kafka | — | Mensageria assíncrona |
| 🗺️ MapStruct | 1.5.3.Final | Mapeamento entre camadas (compile-time) |
| 🦏 Lombok | — | Redução de boilerplate |
| ✅ Jakarta Validation | — | Validação de entrada |

---

## 🏗️ Arquitetura

O projeto implementa a **Arquitetura Hexagonal** onde o núcleo da aplicação (domínio e casos de uso) é completamente isolado de detalhes técnicos (banco de dados, HTTP, mensageria). A comunicação entre as camadas ocorre exclusivamente por meio de interfaces — os **Ports**.

```
┌─────────────────────────────────────────────────────────┐
│                  🔌 ADAPTERS (in)                       │
│   🌐 REST Controller        📥 Kafka Consumer           │
└────────────────┬───────────────────┬────────────────────┘
                 │  Input Ports      │
         ┌───────▼───────────────────▼───────┐
         │      💎 APPLICATION CORE          │
         │  ┌─────────────────────────────┐  │
         │  │       ⚙️ Use Cases          │  │
         │  └─────────────────────────────┘  │
         │  ┌─────────────────────────────┐  │
         │  │         🧩 Domain           │  │
         │  │    (Customer, Address)      │  │
         │  └─────────────────────────────┘  │
         └───────┬───────────────────┬───────┘
                 │  Output Ports     │
┌────────────────▼───────────────────▼────────────────────┐
│                  🔌 ADAPTERS (out)                       │
│  🗄️ MongoDB Repository  🌍 OpenFeign  📤 Kafka Producer  │
└─────────────────────────────────────────────────────────┘
```

### 💉 Princípio de injeção de dependência

Os **casos de uso são classes Java puras** — não são beans Spring. Eles são instanciados manualmente nas classes de configuração (`config/`), que recebem os adapters como dependências Spring e os injetam nos use cases. Isso garante que o core da aplicação não dependa de nenhum framework.

---

## 📁 Estrutura do Projeto

```
src/main/java/com/udemy/hexagonal/
│
├── application/
│   ├── core/
│   │   ├── domain/              # 🧩 Entidades de domínio puras (sem anotações de framework)
│   │   │   ├── Customer.java
│   │   │   └── Address.java
│   │   └── usecase/             # ⚙️ Implementações dos casos de uso
│   │       ├── InsertCustomerUseCase.java
│   │       ├── UpdateCustomerUseCase.java
│   │       ├── FindCustomerByIdUseCase.java
│   │       └── DeleteCustomerUseCase.java
│   └── ports/
│       ├── in/                  # 🔵 Contratos que os use cases implementam
│       │   ├── InsertCustomerInputPort.java
│       │   ├── UpdateCustomerInputPort.java
│       │   ├── FindCustomerByIdInputPort.java
│       │   └── DeleteCustomerInputPort.java
│       └── out/                 # 🟠 Contratos que os adapters implementam
│           ├── InsertCustomerOutputPort.java
│           ├── UpdateCustomerOutputPort.java
│           ├── FindCustomerByIdOutputPort.java
│           ├── DeleteCustomerOutputPort.java
│           ├── FindAdressByZipCodeOutputPort.java
│           └── SendCpfForValidationOutputPort.java
│
├── adapters/
│   ├── in/
│   │   ├── controller/          # 🌐 REST API + DTOs (CustomerRequest, CustomerResponse)
│   │   │   ├── mapper/          # 🗺️ CustomerMapper (MapStruct)
│   │   │   └── CustomerController.java
│   │   └── consumer/            # 📥 Kafka consumer de CPF validado
│   │       ├── mapper/          # 🗺️ CustomerMessageMapper (MapStruct)
│   │       └── ReceiveValidatedCpfConsumer.java
│   └── out/
│       ├── repository/          # 🗄️ Adapters MongoDB + CustomerEntity
│       │   └── mapper/          # 🗺️ CustomerEntityMapper (MapStruct)
│       ├── client/              # 🌍 OpenFeign client para busca de endereço
│       │   └── mapper/          # 🗺️ AddressResponseMapper (MapStruct)
│       └── SendCpfValidationAdapter.java  # 📤 Kafka producer
│
└── config/                      # 🔧 Spring @Bean — instancia use cases manualmente
    ├── InsertCustomerConfig.java
    ├── UpdateCustomerConfig.java
    ├── FindCustomerByIdConfig.java
    ├── DeleteCustomerConfig.java
    ├── KafkaProducerConfig.java
    └── KafkaConsumerConfig.java
```

---

## 🔄 Fluxo de Dados — Inserção de Cliente

1. 🌐 **HTTP POST** `/api/v1/customers` → `CustomerController`
2. 🗺️ `CustomerController` mapeia `CustomerRequest` → `Customer` (domain) via MapStruct
3. ⚙️ `InsertCustomerUseCase.insert()` orquestra:
   - 🌍 Busca endereço via `FindAdressByZipCodeOutputPort` → `FindAddressZipCodeAdapter` (OpenFeign → serviço externo)
   - 🗄️ Persiste cliente via `InsertCustomerOutputPort` → `InsertCustomerAdapter` (MongoDB)
   - 📤 Envia CPF para validação via `SendCpfForValidationOutputPort` → `SendCpfValidationAdapter` (Kafka → `topic-cpf-validation`)
4. 🔍 Serviço externo de validação publica resultado em `topic-cpf-validated`
5. 📥 `ReceiveValidatedCpfConsumer` consome a mensagem e chama `UpdateCustomerUseCase` para atualizar o campo `isCpfValid`

---

## 📡 API REST

**Base URL:** `/api/v1/customers`

| Método | Endpoint | Body | Resposta | Descrição |
|--------|----------|------|----------|-----------|
| 🟢 POST | `/` | `CustomerRequest` | 200 OK | Cria novo cliente |
| 🔵 GET | `/{id}` | — | `CustomerResponse` | Busca cliente por ID |
| 🟡 PUT | `/{id}` | `CustomerRequest` | 204 No Content | Atualiza cliente |
| 🔴 DELETE | `/{id}` | — | 204 No Content | Remove cliente |

**📥 CustomerRequest:**
```json
{
  "name": "João Silva",
  "cpf": "12345678901",
  "zipCode": "01001000"
}
```

**📤 CustomerResponse:**
```json
{
  "name": "João Silva",
  "cpf": "12345678901",
  "address": {
    "street": "Praça da Sé",
    "city": "São Paulo",
    "state": "SP"
  },
  "isCpfValid": true
}
```

---

## 📐 Padrões e Convenções

- 🗺️ **Um mapper por fronteira de camada:** cada transição (HTTP → domínio, domínio → entidade, Kafka → domínio) tem seu próprio mapper MapStruct com `componentModel = "spring"`
- 🔵 **Portas de entrada (in):** interfaces implementadas pelos use cases; definem o contrato para os adapters de entrada
- 🟠 **Portas de saída (out):** interfaces implementadas pelos adapters; os use cases dependem apenas dessas interfaces, nunca de implementações concretas
- 🔧 **Config como composition root:** as classes em `config/` são o único lugar onde implementações concretas se encontram; fora delas, tudo depende de interfaces
- ✅ **Validação na borda:** `@Valid` aplicado nos request bodies do controller; o domínio não valida formato de entrada
- 🏷️ **Nomes de tópicos Kafka via properties:** os nomes de tópicos nunca são hardcoded, sempre injetados via `@Value`

---

## ⚙️ Configuração

`src/main/resources/application.properties`:

```properties
# 🌍 Serviço externo de endereços
hexagonal.client.addressResponse.url=http://localhost:8081

# 📨 Kafka
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=hexagonal-consumer
hexagonal.message.producer.topic.cpf.validation=topic-cpf-validation
hexagonal.message.producer.topic.cpf.validated=topic-cpf-validated

# 🗄️ MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/udemy-cursos.hexagonal
```

---

## 🛠️ Pré-requisitos e Execução

- ☕ Java 21 (JDK)
- 🗄️ MongoDB rodando em `localhost:27017`
- 📨 Kafka rodando em `localhost:9092`

```bash
# 📦 Build
./gradlew build

# ▶️ Executar
./gradlew bootRun

# 🧪 Testes
./gradlew test
```

---

*🎓 Projeto desenvolvido como estudo prático de Arquitetura Hexagonal.*
