# 🏥 Histórico Saúde - Tech Challenge Fase 3

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![GraphQL](https://img.shields.io/badge/-GraphQL-E10098?style=for-the-badge&logo=graphql&logoColor=white)

Em um ambiente hospitalar, é essencial contar com sistemas que garantam o agendamento eficaz de consultas, o gerenciamento do histórico de pacientes e o envio de lembretes automáticos para garantir a presença dos pacientes. O **Histórico Saúde** nasce dessa necessidade de criar uma solução robusta e segura, projetada para ambientes dinâmicos e de comunicação assíncrona.

O objetivo é desenvolver um backend simplificado e modular, com foco em segurança, garantindo escalabilidade e boas práticas de autenticação e comunicação entre serviços.

### Objetivo do Projeto

O projeto tem como objetivo criar um sistema de gestão hospitalar compartilhado que atenda médicos, enfermeiros e pacientes.

O sistema garante acesso controlado com funcionalidades específicas para cada perfil:
*   **Médicos:** Podem visualizar e editar o histórico de consultas.
*   **Enfermeiros:** Podem registrar consultas e acessar o histórico.
*   **Pacientes:** Podem visualizar apenas as suas consultas.

A solução foi desenvolvida utilizando **Spring Boot**, comunicação assíncrona via **Message Broker** e consultas flexíveis via **GraphQL**, executando em ambiente Docker.

## Tecnologias Utilizadas

-   **Java 21** & **Spring Boot 3**
-   **PostgreSQL 17** (Banco de dados relacional)
-   **Docker & Docker Compose** (Orquestração de containers)
-   **Spring Security & JWT** (Autenticação e Autorização)
-   **RabbitMQ** ou **Kafka** (Comunicação Assíncrona entre serviços)
-   **GraphQL** (Consultas flexíveis de histórico)
-   **Flyway** (Migração de banco de dados)
-   **Lombok** (Redução de código boilerplate)

## Arquitetura de Serviços

O sistema foi separado em serviços lógicos para garantir a modularidade exigida:

1.  **Serviço de Agendamento:** Responsável pela criação e edição das consultas (REST). Ao criar/editar, envia uma mensagem ao broker.
2.  **Serviço de Notificações:** Consumidor assíncrono que processa mensagens e simula o envio de lembretes aos pacientes.
3.  **Serviço de Histórico:** Armazena dados e disponibiliza o histórico via GraphQL.

## Configuração e Execução

### Variáveis de Ambiente

O sistema utiliza variáveis para conexão com banco e broker:

-   `JWT_SECRET`: Chave para assinatura de tokens.
-   `DATABASE_URL`: URL do PostgreSQL.
-   `BROKER_HOST`: Endereço do RabbitMQ/Kafka.

### Executando com Docker Compose

Para rodar a aplicação e a infraestrutura (Banco + Broker):

1.  Suba os containers:
    ```bash
    docker compose up
    ```
2.  Acesse a aplicação: `http://localhost:8080`

## Endpoints Principais

### Autenticação (Spring Security)
O acesso é protegido via Token JWT.

-   `POST /auth/login` - Autenticação de usuários (Médico, Enfermeiro, Paciente).

### Agendamento (REST)
Gerenciado por enfermeiros e médicos.

-   `POST /consultas` - Registra uma nova consulta. *Dispara evento de notificação.*
-   `PUT /consultas/{id}` - Modifica uma consulta existente. *Dispara evento de notificação.*
-   `DELETE /consultas/{id}` - Cancela um agendamento.

### Histórico e Prontuário (GraphQL)
Implementação de consultas flexíveis para listar atendimentos.

-   `POST /graphql` - Endpoint único para queries.

Exemplo de Query GraphQL:
```graphql
query {
  historicoPaciente(idPaciente: 1) {
    id
    dataConsulta
    medicoResponsavel
    diagnostico
  }
}
```

## Sistema de Autorização

O sistema implementa controle de acesso baseado em roles (perfis):

-   **MÉDICO:** Acesso total ao histórico (leitura/escrita) e visualização de agendamentos.
-   **ENFERMEIRO:** Permissão para criar/editar agendamentos e visualizar histórico.
-   **PACIENTE:** Acesso restrito apenas à visualização de suas próprias consultas futuras e passadas.

## Estrutura do Banco de Dados

O sistema utiliza PostgreSQL com as seguintes entidades principais adaptadas para o contexto:

-   **Usuario:** Dados de login, senha e Role (Médico, Enfermeiro, Paciente).
-   **Consulta:** Data, horário, status, médico e paciente vinculados.
-   **Prontuario/Historico:** Registros clínicos associados às consultas.

## Arquitetura do Código (Clean Architecture)

Seguindo o padrão de organização para garantir manutenibilidade e testabilidade:

```
src/main/java/com/historicosaude/
├── application/       # Casos de uso e DTOs
├── domain/            # Entidades e Regras de Negócio
├── infra/             # Configurações (Security, RabbitMQ, GraphQL)
│   ├── controller/    # Endpoints REST
│   ├── graphql/       # Resolvers GraphQL
│   ├── messaging/     # Producers/Consumers
│   └── persistence/   # Repositórios JPA
```

## Documentação da API

### Collection para Teste

Para validar os fluxos (Login -> Agendar -> Consultar Histórico), utilize a Collection do Postman incluída no repositório.

**Como usar:**
1.  Importe o arquivo `.json` da collection no Postman.
2.  Execute as requisições na ordem sugerida para validar o fluxo de autenticação e agendamento.
```

***

### ⚠️ Lembrete Importante
O documento acima menciona uma "Collection do Postman". Como este é um novo projeto, você precisará exportar a sua coleção de testes do Postman (formato `.json`) e colocá-la no seu repositório Git, pois isso é um dos **entregáveis obrigatórios** da fase 3.