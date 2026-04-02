# Arquitetura Hexagonal com Spring Boot

Este projeto é um exemplo prático de como implementar a **Arquitetura Hexagonal** (ou Ports and Adapters) utilizando Java, Spring Boot, MongoDB e Kafka. O objetivo é criar um sistema altamente testável, desacoplado e focado nas regras de negócio.

## 🚀 Tecnologias e Ferramentas

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.5.11
- **Banco de Dados:** MongoDB
- **Mensageria:** Kafka
- **Mapeamento:** MapStruct
- **Utilitários:** Lombok
- **Comunicação HTTP:** Spring Cloud OpenFeign

## 🏗️ Estrutura do Projeto

O projeto segue os princípios da Arquitetura Hexagonal, dividindo-se em:

- `application`: Contém o core do domínio e as regras de negócio.
    - `core/domain`: Entidades de domínio puras.
    - `core/usecase`: Implementação dos casos de uso.
    - `ports/in`: Interfaces de entrada (Input Ports).
    - `ports/out`: Interfaces de saída (Output Ports).
- `adapters`: Implementações técnicas que se conectam ao core.
    - `in/controller`: Adaptadores de entrada (REST API).
    - `out`: Adaptadores de saída (Persistência, Clientes HTTP, Mensageria).
- `config`: Configurações do Spring (Beans, Injeção de Dependência).

## ⚙️ Pré-requisitos

- Java 21 (JDK)
- Docker (recomendado para rodar MongoDB e Kafka)
- Gradle

## 🛠️ Instalação e Execução

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/arquitetura-hexagonal.git
   cd arquitetura-hexagonal
   ```

2. **Configurar infraestrutura (TODO):**
   > **TODO:** Adicionar um arquivo `docker-compose.yml` para facilitar a subida do MongoDB e Kafka.

3. **Compilar o projeto:**
   ```bash
   ./gradlew build
   ```

4. **Executar a aplicação:**
   ```bash
   ./gradlew bootRun
   ```

## 📡 API Endpoints

### Customers

- **POST `/api/v1/customers`**: Insere um novo cliente.
    - **Request Body:**
      ```json
      {
        "name": "João Silva",
        "cpf": "12345678901",
        "zipCode": "01001000"
      }
      ```

> **TODO:** Documentar outros endpoints (Busca, Atualização, Exclusão) conforme forem implementados.

## 🧪 Testes

Para executar os testes automatizados:

```bash
./gradlew test
```

## 📝 Variáveis de Ambiente e Configuração

As configurações principais estão em `src/main/resources/application.properties`:

- `spring.data.mongodb.uri`: URI de conexão com o MongoDB.
- `hexagonal.client.address.url`: URL do serviço externo de endereços.

## 📜 Licença

> **TODO:** Adicionar um arquivo de licença (MIT, Apache 2.0, etc.).

---
*Este projeto foi desenvolvido como parte de um estudo sobre Arquitetura Hexagonal.*
