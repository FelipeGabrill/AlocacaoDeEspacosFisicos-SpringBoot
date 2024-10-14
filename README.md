# arqsoftware - Sistema de Alocação de Espaços Físicos

## Descrição

Sistema desenvolvido com Spring Boot, projetado para gerenciar solicitações, reservas e aprovações de espaços físicos em instituições acadêmicas.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Security
- JWT
- Spring Data JPA
- PostgreSQL / H2 Database
- bcrypt
- Maven

## Funcionalidades

- **Gestão de Usuários** (professores, gestores, administradores)
- **Solicitação de Espaço Físico**
- **Fluxo de Aprovação/Rejeição**
- **Auditoria de Ações**

## Pré-requisitos

- Java 17+
- Maven
- PostgreSQL (para produção e desenvolvimento)

## Instalação e Configuração

## 1. Clone o repositório:
```bash
git clone https://github.com/usuario/arqsoftware.git
```

## 2. Acesse a pasta do projeto:
```bash
cd arqsoftware
```

# Configuração do Banco de Dados

Para o perfil de desenvolvimento (`dev`), você precisa configurar as variáveis de ambiente com os dados do PostgreSQL. Siga as instruções abaixo para garantir que seu ambiente esteja configurado corretamente.

## Variáveis de Ambiente

Adicione as seguintes variáveis de ambiente ao seu sistema:

```properties
DB_URL=jdbc:postgresql://localhost:5433/arqtsoftware
DB_USERNAME=postgres
DB_PASSWORD=1234567
```

# Configuração do Banco de Dados para Testes

No perfil de testes (`test`), o banco de dados H2 será utilizado, sem necessidade de configuração adicional. Abaixo estão as configurações para acessar o console H2 durante a execução dos testes:

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
```

# Execução da Aplicação

## 4. Instale as dependências:

```bash
mvn clean install
```

## 5. Execute a aplicação:

Para o ambiente de produção (`prod`), utilize o seguinte comando:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

# Execução da Aplicação

Para o ambiente de desenvolvimento (`dev`), utilize o seguinte comando:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

# Perfis de Configuração

A aplicação suporta três perfis principais, cada um com suas próprias configurações:

- **Prod (Produção)**: Utiliza PostgreSQL.
- **Dev (Desenvolvimento)**: Utiliza PostgreSQL.
- **Test**: Utiliza H2 (banco em memória).

## Segurança

A autenticação é feita via Spring Security com JWT. As senhas dos usuários são criptografadas com bcrypt. Para acessar endpoints protegidos, inclua o token JWT no cabeçalho da requisição:

```http
Authorization: Bearer <seu-token>
```

# Endpoints Principais

## ApprovalHistoryController

| Método | Endpoint                      | Descrição                                      | Autorização                          |
|--------|-------------------------------|------------------------------------------------|--------------------------------------|
| GET    | `/approvalhistories/{id}`     | Busca o histórico de aprovação por ID.         | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/approvalhistories`          | Retorna uma lista paginada de históricos.      | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| POST   | `/approvalhistories`          | Insere um novo histórico de aprovação.         | ROLE_ADMIN, ROLE_MANAGER             |
| PUT    | `/approvalhistories/{id}`     | Atualiza um histórico existente.                | ROLE_ADMIN, ROLE_MANAGER             |
| DELETE | `/approvalhistories/{id}`     | Deleta um histórico existente.                  | ROLE_ADMIN, ROLE_MANAGER             |

## AuditController

| Método | Endpoint         | Descrição                     | Autorização                          |
|--------|------------------|-------------------------------|--------------------------------------|
| GET    | `/audit/logs`    | Retorna todos os logs de auditoria | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |

## PhysicalSpaceController

| Método | Endpoint                         | Descrição                                   | Autorização                          |
|--------|----------------------------------|---------------------------------------------|--------------------------------------|
| GET    | `/physicalspaces/{id}`           | Busca um espaço físico por ID.              | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/physicalspaces`                | Retorna uma lista paginada de espaços físicos. | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| POST   | `/physicalspaces`                | Insere um novo espaço físico.               | ROLE_ADMIN                            |
| PUT    | `/physicalspaces/{id}`           | Atualiza um espaço físico existente.        | ROLE_ADMIN                            |
| DELETE | `/physicalspaces/{id}`           | Deleta um espaço físico existente.          | ROLE_ADMIN                            |
| GET    | `/physicalspaces/type/{type}`    | Busca espaços físicos por tipo.             | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/physicalspaces/capacity/{capacity}` | Busca espaços físicos por capacidade.    | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/physicalspaces/name/{name}`    | Busca espaços físicos por nome.             | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/physicalspaces/availability/{availability}` | Busca espaços físicos por disponibilidade. | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |

## RequestController

| Método | Endpoint                         | Descrição                                   | Autorização                          |
|--------|----------------------------------|---------------------------------------------|--------------------------------------|
| GET    | `/requests/{id}`                 | Busca uma solicitação por ID.               | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/requests`                      | Retorna uma lista paginada de solicitações. | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| POST   | `/requests`                      | Insere uma nova solicitação.                | ROLE_ADMIN, ROLE_TEACHER             |
| PUT    | `/requests/{id}`                 | Atualiza uma solicitação existente.         | ROLE_ADMIN, ROLE_TEACHER             |
| DELETE | `/requests/{id}`                 | Deleta uma solicitação existente.           | ROLE_ADMIN, ROLE_TEACHER             |
| GET    | `/requests/asc`                  | Retorna solicitações em ordem crescente.    | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/requests/desc`                 | Retorna solicitações em ordem decrescente.  | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/requests/status/{status}`      | Retorna solicitações por status.            | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/requests/user/{userLogin}`     | Retorna solicitações por login de usuário.  | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/requests/title/{title}`         | Retorna solicitações por título.            | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |

## UserController

| Método | Endpoint                   | Descrição                          | Autorização                          |
|--------|----------------------------|------------------------------------|--------------------------------------|
| GET    | `/users/me`                | Retorna o usuário logado.         | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/users/{id}`              | Busca um usuário por ID.          | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| GET    | `/users`                   | Retorna uma lista paginada de usuários. | ROLE_ADMIN, ROLE_TEACHER, ROLE_MANAGER |
| POST   | `/users`                   | Insere um novo usuário.           | ROLE_ADMIN                            |
| PUT    | `/users/{id}`              | Atualiza um usuário existente.     | ROLE_ADMIN                            |
| DELETE | `/users/{id}`              | Deleta um usuário existente.      | ROLE_ADMIN                            |
| GET    | `/users/login/{login}`     | Retorna usuários por login.        | ROLE_ADMIN                            |
