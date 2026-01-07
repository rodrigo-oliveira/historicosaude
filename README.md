# 🏥 Histórico Saúde - Arquitetura de Microserviços

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![GraphQL](https://img.shields.io/badge/-GraphQL-E10098?style=for-the-badge&logo=graphql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

Em um ambiente hospitalar, é essencial contar com sistemas que garantam o agendamento eficaz de consultas, o gerenciamento do histórico de pacientes e o envio de lembretes automáticos para garantir a presença dos pacientes. O **Histórico Saúde** nasce dessa necessidade de criar uma solução robusta e segura, baseada em **arquitetura de microserviços**, projetada para ambientes dinâmicos e de comunicação assíncrona.

## Objetivo do Projeto

Desenvolver um sistema de gestão hospitalar compartilhado com **três microserviços independentes**:
- **Service A (Agendamento):** REST API com autenticação JWT
- **Service B (Notificações):** Worker que processa eventos
- **Service C (Histórico):** GraphQL para consultas de dados históricos

O sistema garante acesso controlado com funcionalidades específicas para cada perfil:
- **Médicos:** Visualizar, editar e cancelar consultas
- **Enfermeiros:** Registrar e visualizar consultas
- **Pacientes:** Visualizar apenas suas próprias consultas
- **Admin:** Gerenciar usuários, consultas e ter acesso total

## Arquitetura de Microserviços

```
┌─────────────────────────────────────────────────────────┐
│                     CLIENTE                              │
└──────────┬──────────────────────────────────┬────────────┘
           │                                  │
      REST/JWT                            GraphQL
           │                                  │
    ┌──────▼──────────┐           ┌──────────▼────────┐
    │  Service A      │           │   Service C       │
    │  Agendamento    │           │   Histórico       │
    │  (REST)         │           │   (GraphQL)       │
    │  Port: 8080     │           │   Port: 8082      │
    └────────┬────────┘           └──────────┬────────┘
             │                               │
        ┌────▼──────────┐           ┌────────▼────┐
        │  PostgreSQL   │           │ PostgreSQL  │
        │  Agendamento  │           │ Histórico   │
        │  Port: 5432   │           │ Port: 5433  │
        └───────────────┘           └─────────────┘
             │
      ┌──────▼──────────────┐
      │  RabbitMQ Broker    │
      │  AMQP: 5672         │
      │  Management: 15672  │
      └──────┬──────────────┘
             │
      ┌──────▼──────────────┐
      │  Service B          │
      │  Notificações       │
      │  (Worker)           │
      │  Port: 8081         │
      └─────────────────────┘
```

## Tecnologias Utilizadas

- **Java 21** & **Spring Boot 3.5.4**
- **PostgreSQL 15** (Banco de dados relacional)
- **RabbitMQ 3** (Message Broker)
- **Spring Security & JWT** (Autenticação e Autorização)
- **Spring GraphQL** (Queries flexíveis)
- **Docker & Docker Compose** (Orquestração de containers)
- **Flyway** (Migrações de banco de dados)
- **Lombok** (Redução de boilerplate)
-   **GraphQL** (Consultas flexíveis de histórico)
-   **Flyway** (Migração de banco de dados)
-   **Lombok** (Redução de código boilerplate)

## Arquitetura de Serviços

O sistema foi separado em serviços lógicos para garantir a modularidade exigida:

1.  **Serviço de Agendamento:** Responsável pela criação e edição das consultas (REST). Ao criar/editar, envia uma mensagem ao broker.
2.  **Serviço de Notificações:** Consumidor assíncrono que processa mensagens e simula o envio de lembretes aos pacientes.
3.  **Serviço de Histórico:** Armazena dados e disponibiliza o histórico via GraphQL.

## Configuração e Execução

### Pré-requisitos

- Docker e Docker Compose instalados
- Maven 3.9+ (para build local)
- Java 21+
- Postman (para testar APIs)

### Variáveis de Ambiente

O sistema utiliza variáveis para conexão com banco e broker:

-   `JWT_SECRET`: Chave para assinatura de tokens (padrão: `my-secret-key`)
-   `JWT_EXPIRATION_TIME`: Tempo de expiração do token em ms (padrão: `86400000` = 24h)

### Executando com Docker Compose

Para rodar a aplicação e a infraestrutura completa (Bancos de Dados + RabbitMQ):

```bash
# 1. Clone o repositório
git clone <repositorio>
cd historicosaude

# 2. Inicie todos os containers
docker compose up -d

# 3. Aguarde a inicialização dos serviços (~30 segundos)
```

**Serviços estarão disponíveis em:**
- **Agendamento Service:** http://localhost:8080
- **Notificações Service:** http://localhost:8081
- **Histórico Service:** http://localhost:8082
- **RabbitMQ Management:** http://localhost:15672 (guest/guest)
- **PostgreSQL Agendamento:** localhost:5432
- **PostgreSQL Histórico:** localhost:5433

### Executando Localmente (sem Docker)

```bash
# Build do projeto
./mvnw clean package -DskipTests

# Inicie os serviços individualmente
# Terminal 1 - Agendamento
./mvnw -pl agendamento-service spring-boot:run

# Terminal 2 - Notificações
./mvnw -pl notificacoes-service spring-boot:run

# Terminal 3 - Histórico
./mvnw -pl historico-service spring-boot:run
```

## Rotas de API

| Método | Endpoint | Descrição | Autorizações | Status |
|--------|----------|-----------|--------------|--------|
| **POST** | `/autenticacao/entrar` | Login e geração de Token JWT | Público
| **POST** | `/autenticacao/registrar` | Cadastro de novos usuários | Público
| **POST** | `/consultas` | Criar nova consulta (Dispara RabbitMQ) | ADMIN, ENFERMEIRO
| **GET** | `/consultas` | Listar todas as consultas | ADMIN, ENFERMEIRO, MEDICO
| **GET** | `/consultas/{id}` | Obter uma consulta específica | ADMIN, ENFERMEIRO, MEDICO
| **GET** | `/consultas/minhas-consultas` | Listar minhas consultas (paciente) | PACIENTE
| **PUT** | `/consultas/{id}` | Atualizar dados da consulta | ADMIN, MEDICO
| **POST** | `/consultas/{id}/cancelar` | Cancelar uma consulta | ADMIN, MEDICO
| **DELETE** | `/consultas/{id}` | Remover consulta permanentemente | ADMIN
| **GET** | `/usuarios` | Listar todos os usuários com IDs | ADMIN


## Endpoints Principais (Detalhados)

### 1. Autenticação (Service A - Agendamento)
Base URL: `http://localhost:8080/autenticacao`

**Fazer Login:**
```http
POST /autenticacao/entrar
Content-Type: application/json

{
  "login": "admin",
  "senha": "123456"
}

Response (200):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Registrar novo usuário:**
```http
POST /autenticacao/registrar
Content-Type: application/json

{
  "cpf": "12345678901",
  "nome": "João Silva",
  "email": "joao@example.com",
  "login": "joao",
  "senha": "senha123",
  "perfil": "PACIENTE"
}

Response (201):
"Usuário registrado com sucesso"
```

**Logout (Invalidar Token):**
```http
POST /autenticacao/sair
Authorization: Bearer {token}

Response (200):
{
  "mensagem": "Logout realizado com sucesso"
}
```

### 2. Agendamento de Consultas (Service A - Agendamento)
Base URL: `http://localhost:8080/consultas`

**Criar nova consulta** *(Dispara evento RabbitMQ - CONSULTA_CRIADA)*
```http
POST /consultas
Authorization: Bearer {token}
Content-Type: application/json

{
  "cpfPaciente": "12345678901",
  "nomePaciente": "João Silva",
  "emailPaciente": "joao@example.com",
  "dataConsulta": "2026-02-15T14:30:00",
  "medico": "Dr. Pedro",
  "especialidade": "Cardiologia",
  "motivo": "Consulta de rotina"
}

Response (201):
123  // ID da consulta criada

Permissões: ADMIN, ENFERMEIRO
```

**Listar todas as consultas:**
```http
GET /consultas
Authorization: Bearer {token}

Response (200):
[
  {
    "id": 1,
    "cpfPaciente": "12345678901",
    "nomePaciente": "João Silva",
    "emailPaciente": "joao@example.com",
    "dataConsulta": "2026-02-15T14:30:00",
    "medico": "Dr. Pedro",
    "especialidade": "Cardiologia",
    "motivo": "Consulta de rotina",
    "status": "AGENDADA"
  }
]

Permissões: ADMIN, ENFERMEIRO, MEDICO
```

**Listar consultas por CPF:**
```http
GET /consultas/cpf/{cpf}
Authorization: Bearer {token}

Exemplo: GET /consultas/cpf/12345678901

Response (200):
[
  {
    "id": 1,
    "cpfPaciente": "12345678901",
    "nomePaciente": "João Silva",
    "dataConsulta": "2026-02-15T14:30:00",
    "medico": "Dr. Pedro",
    "especialidade": "Cardiologia",
    "status": "AGENDADA"
  }
]

Permissões: ADMIN, PACIENTE, ENFERMEIRO
```

**Obter consulta específica:**
```http
GET /consultas/{id}
Authorization: Bearer {token}

Exemplo: GET /consultas/1

Response (200):
{
  "id": 1,
  "cpfPaciente": "12345678901",
  "nomePaciente": "João Silva",
  "dataConsulta": "2026-02-15T14:30:00",
  "medico": "Dr. Pedro",
  "especialidade": "Cardiologia",
  "status": "AGENDADA"
}

Permissões: ADMIN, ENFERMEIRO
```

**Atualizar consulta** *(Dispara evento RabbitMQ - CONSULTA_ATUALIZADA)*
```http
PUT /consultas/{id}
Authorization: Bearer {token}
Content-Type: application/json

Exemplo: PUT /consultas/1

{
  "dataConsulta": "2026-02-20T10:00:00",
  "medico": "Dra. Maria",
  "especialidade": "Dermatologia",
  "motivo": "Consulta de acompanhamento"
}

Response (204): No Content

Permissões: ADMIN, ENFERMEIRO
```

**Cancelar consulta** *(Dispara evento RabbitMQ - CONSULTA_CANCELADA)*
```http
POST /consultas/{id}/cancelar
Authorization: Bearer {token}

Exemplo: POST /consultas/1/cancelar

Response (204): No Content

Permissões: ADMIN, MEDICO, PACIENTE
```

**Deletar consulta (permanente):**
```http
DELETE /consultas/{id}
Authorization: Bearer {token}

Exemplo: DELETE /consultas/1

Response (204): No Content

Permissões: ADMIN
```

### 3. Consultas Privadas do Paciente (Service A - Agendamento)
Base URL: `http://localhost:8080/consultas/minhas-consultas`

**Listar minhas consultas** *(Paciente acessa apenas suas consultas)*
```http
GET /consultas/minhas-consultas
Authorization: Bearer {token}

Response (200):
[
  {
    "id": 1,
    "cpfPaciente": "12345678901",
    "nomePaciente": "João Silva",
    "emailPaciente": "joao@example.com",
    "dataConsulta": "2026-02-15T14:30:00",
    "medico": "Dr. Pedro",
    "especialidade": "Cardiologia",
    "motivo": "Consulta de rotina",
    "status": "AGENDADA"
  }
]

Permissões: PACIENTE
Nota: O CPF é extraído automaticamente do token JWT autenticado
```

### 4. Usuários (Service A - Agendamento)
Base URL: `http://localhost:8080/usuarios`

**Listar todos os usuários com IDs:**
```http
GET /usuarios
Authorization: Bearer {token}

Response (200):
[
  {
    "id": 1,
    "cpf": "12345678901",
    "nome": "Administrador Teste",
    "email": "admin@historicosaude.com",
    "login": "admin",
    "dataUltimaAlteracao": "2026-01-10T06:30:00+00:00",
    "perfil": "ADMIN"
  },
  {
    "id": 2,
    "cpf": "98765432109",
    "nome": "Médico Silva",
    "email": "medico@example.com",
    "login": "medico.silva",
    "dataUltimaAlteracao": "2026-01-10T06:30:00+00:00",
    "perfil": "MEDICO"
  }
]

Permissões: ADMIN
```

### 5. Histórico de Consultas (Service C - Histórico - GraphQL)
Base URL: `http://localhost:8082/graphql`

**Listar todas as consultas (com paginação):**
```graphql
query {
  todasAsConsultas(limit: 10, offset: 0) {
    id
    cpfPaciente
    nomePaciente
    emailPaciente
    dataConsulta
    medico
    especialidade
    motivo
    status
    criadoEm
    atualizadoEm
  }
}

Response:
{
  "data": {
    "todasAsConsultas": [
      {
        "id": "1",
        "cpfPaciente": "12345678901",
        "nomePaciente": "João Silva",
        "emailPaciente": "joao@example.com",
        "dataConsulta": "2026-02-15T14:30:00",
        "medico": "Dr. Pedro",
        "especialidade": "Cardiologia",
        "motivo": "Consulta de rotina",
        "status": "AGENDADA",
        "criadoEm": "2026-01-10T10:30:00",
        "atualizadoEm": "2026-01-10T10:30:00"
      }
    ]
  }
}

Permissões: ADMIN, MEDICO, ENFERMEIRO
Nota: Suporta paginação com limit e offset
```

**Listar consultas por CPF:**
```graphql
query {
  consultasPorCpf(cpf: "12345678901") {
    id
    nomePaciente
    dataConsulta
    medico
    especialidade
    motivo
    status
  }
}

Response:
{
  "data": {
    "consultasPorCpf": [
      {
        "id": "1",
        "nomePaciente": "João Silva",
        "dataConsulta": "2026-02-15T14:30:00",
        "medico": "Dr. Pedro",
        "especialidade": "Cardiologia",
        "motivo": "Consulta de rotina",
        "status": "AGENDADA"
      }
    ]
  }
}
```

**Listar consultas por médico:**
```graphql
query {
  consultasPorMedico(medico: "Dr. Pedro") {
    id
    nomePaciente
    cpfPaciente
    dataConsulta
    especialidade
    status
  }
}

Response:
{
  "data": {
    "consultasPorMedico": [
      {
        "id": "1",
        "nomePaciente": "João Silva",
        "cpfPaciente": "12345678901",
        "dataConsulta": "2026-02-15T14:30:00",
        "especialidade": "Cardiologia",
        "status": "AGENDADA"
      }
    ]
  }
}
```

**Acessar GraphQL IDE (GraphiQL):**
- URL: `http://localhost:8082/graphiql`
- Sem autenticação necessária (IDE pública)
- Para rodar queries que exigem ADMIN: adicione token no header

```
Authorization: Bearer {seu_token_jwt}
```

## Sistema de Autorização

O sistema implementa controle de acesso baseado em roles (perfis) usando Spring Security + JWT:

| Perfil | Permissões |
|--------|-----------|
| **ADMIN** | Criar/editar/deletar usuários e consultas. Acesso total. |
| **MÉDICO** | Visualizar todas as consultas. Editar consultas. Ver histórico completo no GraphQL. |
| **ENFERMEIRO** | Criar e editar agendamentos. Visualizar histórico de consultas. |
| **PACIENTE** | Visualizar apenas suas próprias consultas via `/minhas-consultas`. Cancelar consulta própria. |

**Credencial de teste (inserida no migration V2):**
```
Login: admin
Senha: 123456
Perfil: ADMIN
CPF: 12345678901
```

## Fluxo de Comunicação Assíncrona (RabbitMQ)

1. **Agendamento Service** cria/atualiza/cancela uma consulta
2. Publica um evento `ConsultaEvent` no RabbitMQ
3. **Histórico Service** consome o evento e atualiza sua base de dados
4. **Notificações Service** consome o evento e processa notificações

**Fila principal:** `consultas.notificacoes.queue`

**Tipos de eventos:**
- `CONSULTA_CRIADA`
- `CONSULTA_ATUALIZADA`
- `CONSULTA_CANCELADA`

## Collection

Para testar todos os endpoints de forma organizada:

1. **Importe a Collection do Postman** (arquivo `HistoricoSaude.postman_collection.json` incluído no repositório)
2. **Configure a variável de ambiente:** `{{base_url}}` = `http://localhost:8080`