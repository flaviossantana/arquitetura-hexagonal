# Arquitetura Hexagonal com Spring Boot

Projeto prático de **Arquitetura Hexagonal (Ports & Adapters)** com Java e Spring, feito para mostrar como manter regra de negócio no centro e detalhes técnicos nas bordas.

Em vez de acoplar tudo ao framework, aqui a ideia é simples: o domínio manda no jogo, e banco, HTTP e mensageria se adaptam a ele.

## Descrição do Projeto

Implementação prática da Arquitetura Hexagonal usando **Java 21, Spring Boot, MongoDB, Kafka, OpenFeign e MapStruct** para construir um serviço de clientes com:

- API REST para CRUD
- Busca de endereço por ZIP code via client HTTP externo
- Publicação de CPF para validação assíncrona
- Consumo do resultado da validação para atualizar o cliente

Objetivo: demonstrar um sistema **testável, desacoplado, com baixo impacto de mudanças de infraestrutura** e com regras de negócio preservadas.

## Stack Tecnológica

| Tecnologia | Versão | Papel no projeto |
| --- | --- | --- |
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5.11 | Infraestrutura da aplicação |
| Spring Cloud OpenFeign | BOM 2025.0.0 | Client HTTP declarativo |
| Spring Data MongoDB | via Boot | Persistência |
| Spring Kafka | via Boot | Producer/Consumer Kafka |
| MapStruct | 1.5.3.Final | Mapeamento entre camadas |
| Lombok | via Gradle | Redução de boilerplate |
| Jakarta Validation | via Boot | Validação de request |
| ArchUnit | 1.4.0 | Testes de arquitetura |
| JUnit 5 | via Boot | Testes automatizados |

## O que esse projeto cobre (features)

- CRUD de clientes via `/api/v1/customers`
- Enriquecimento de cliente com endereço antes de persistir
- Publicação de CPF em tópico Kafka para validação
- Consumer Kafka para receber CPF validado e atualizar cliente
- Tratamento global de erro de cliente não encontrado (`404`)
- Logging com `correlationID` em cada requisição REST
- Mapeamento explícito entre fronteiras (request/domain/entity/message)

## Arquitetura em 1 minuto

### Core (regra de negócio)

- `application/core/domain`: entidades puras (`Customer`, `Address`)
- `application/core/usecase`: casos de uso (`Insert`, `Update`, `Find`, `Delete`)

### Ports (contratos)

- `application/ports/in`: contratos que os adapters de entrada chamam
- `application/ports/out`: contratos que os casos de uso usam para falar com mundo externo

### Adapters (implementações)

- `adapters/in/controller`: REST API
- `adapters/in/consumer`: Kafka consumer
- `adapters/out/repository`: MongoDB
- `adapters/out/client`: integração HTTP externa
- `adapters/out`: envio Kafka

### Config (composition root)

- `config/*Config`: instancia os use cases e injeta adapters concretos
- Use cases são classes Java puras, sem anotação Spring

## Prós e Contras da Arquitetura Hexagonal (na prática)

### Prós

- Regra de negócio isolada de framework e infraestrutura
- Testabilidade alta (especialmente no core e regras arquiteturais)
- Troca de tecnologia com menor impacto (ex.: banco ou broker)
- Fronteiras explícitas por portas facilitam manutenção
- Organização previsível em times grandes

### Contras

- Mais arquivos/interfaces no início (curva de adoção)
- Boilerplate maior em projetos pequenos
- Exige disciplina de equipe para manter fronteiras
- Mau uso pode virar “arquitetura de slides” sem ganho real
- Depuração às vezes passa por mais camadas que o necessário

Resumo pragmático: para sistema simples, pode parecer “overengineering”; para sistema que cresce e integra com muita coisa, paga o investimento.

## Convenções adotadas

- Casos de uso nunca dependem de classes de `adapters`
- Domínio não depende de Spring, adapters ou ports
- Adapter de entrada não acessa adapter de saída diretamente
- Sufixos e pacotes padronizados (`*UseCase`, `*InputPort`, `*OutputPort`, `*Adapter`, `*Mapper`, `*Config`, etc.)
- Controllers e Consumers restritos aos pacotes corretos
- Mappers centralizados em pacotes `mapper`

Essas regras não estão só “combinadas”: estão automatizadas com ArchUnit.

## Testes

### Testes de arquitetura (ArchUnit)

- `LayeredArchitectureTest`: protege as camadas e dependências permitidas
- `DependencyRulesTest`: impede acoplamentos indevidos entre core/adapters
- `NamingConventionTest`: garante convenções de nomes e pacotes
- `SpringRulesTest`: evita uso indevido de Spring no domínio/core

### Testes de contexto Spring

- `HexagonalApplicationTests`: sobe o contexto da aplicação

## Tratamento de erros

- `CustomerNotFoundException` (core) representa ausência de cliente
- `ApiExceptionHandler` converte exceção para resposta HTTP padronizada
- `StandardError` padroniza payload com:
  - `timestamp`
  - `status`
  - `message`
  - `path`

Retorno esperado para cliente não encontrado: **HTTP 404**.

## Endpoints REST

Base: `api/v1/customers`

| Método | Endpoint | Descrição | Status |
| --- | --- | --- | --- |
| POST | `/` | Cria cliente | `200 OK` |
| GET | `/{id}` | Busca cliente por id | `200 OK` / `404` |
| PUT | `/{id}` | Atualiza cliente | `204 No Content` |
| DELETE | `/{id}` | Remove cliente | `204 No Content` |

Exemplo de request:

```json
{
  "name": "Joao Silva",
  "cpf": "12345678901",
  "zipCode": "01001000"
}
```

Validações atuais de entrada (`CustomerRequest`):

- `name`: `@NotBlank`
- `cpf`: `@NotBlank`
- `zipCode`: `@NotBlank`

## Mensageria (Kafka)

- Tópico de envio de CPF: `hexagonal.message.producer.topic.cpf.validation`
- Tópico de retorno de CPF validado: `hexagonal.message.producer.topic.cpf.validated`
- Consumer group: `spring.kafka.consumer.group-id`

Fluxo:

1. API recebe cliente
2. Caso de uso busca endereço e persiste
3. CPF é publicado para validação
4. Consumer recebe CPF validado
5. Caso de uso atualiza cliente no MongoDB

## Configuração

Arquivo: `src/main/resources/application.properties`

Principais chaves:

- `hexagonal.client.address.response.url`
- `spring.kafka.bootstrap-servers`
- `spring.kafka.consumer.group-id`
- `hexagonal.message.producer.topic.cpf.validation`
- `hexagonal.message.producer.topic.cpf.validated`
- `spring.data.mongodb.host`
- `spring.data.mongodb.port`
- `spring.data.mongodb.username`
- `spring.data.mongodb.password`
- `spring.data.mongodb.database`
- `spring.data.mongodb.authentication-database`

## Como subir o ambiente local

Pré-requisitos:

- JDK 21
- Docker/Docker Compose

Infra local (Kafka + Zookeeper + Kafdrop + Mongo + Mongo Express):

```bash
cd docker-local
docker compose up -d
```

App:

```bash
./gradlew clean build
./gradlew bootRun
```

Testes:

```bash
./gradlew test
```

## Estrutura resumida

```text
src/main/java/com/udemy/hexagonal
├── application
│   ├── core
│   │   ├── domain
│   │   ├── exceptions
│   │   └── usecase
│   └── ports
│       ├── in
│       └── out
├── adapters
│   ├── in
│   │   ├── consumer
│   │   └── controller
│   └── out
│       ├── client
│       └── repository
└── config
```

## Fechamento

Esse projeto é um laboratório real para treinar **separação de responsabilidades**, **evolução segura** e **arquitetura orientada a domínio** sem ficar preso ao framework.

Se a meta é aprender Hexagonal de verdade (com vantagens e dores reais), este repositório entrega.
