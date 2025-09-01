# Sistema de Alocação de Espaços Físicos

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.5
- Lombok
- Spring Security
- Spring OAuth2 Authorization
- JWT
- Spring Data JPA
- PostgreSQL / H2 Database
- bcrypt
- Maven
- Jakarta Bean Validation / Hibernate Validator
- Quartz Scheduler
- Spring Data JPA Specifications (filtros dinâmicos)
- Spring Boot Mail
- Springdoc OpenAPI (Swagger UI)
- Auditoria (via Spring AOP)

## Funcionalidades

- **Gestão de Usuários** (professores, gestores, administradores)
- **Solicitação de agendamento de Espaços Físicos**
- **Gestão de Espaço Físico**
- **Fluxo de Aprovação/Rejeição**
- **Auditoria de Ações**

### Objetivo
Permitir que diferentes perfis de usuários acessem a aplicação com permissões adequadas e realizem ações compatíveis com seu papel.

### Perfis de usuário

#### Professores
- Podem criar e gerenciar suas próprias solicitações (Requests) de uso de espaços físicos.
- Consultar histórico de aprovações relacionado às suas solicitações.

#### Gestores
- Visualizam todas as solicitações.
- Podem aprovar ou rejeitar solicitações pendentes.

#### Administradores
- Gerenciam usuários: criar, atualizar, deletar.
- Definem roles (professor, gestor, administrador).
- Consultam relatórios de auditoria e disponibilidade de espaços.
- Configuram espaços físicos e supervisionam toda a operação do sistema.


## Regras de negocios

### Usuários
- Usuários têm login e roles únicos, com senha armazenada criptografada (bcrypt).  
- É possível criar, atualizar, deletar e listar usuários; operações lançam exceções se o usuário não existir ou houver conflito de integridade.  
- Associações com roles, requests e histórico de aprovações são mantidas sem duplicações.  
- Autenticação é feita via Spring Security + JWT, permitindo recuperar o usuário logado com segurança.  
- Métodos de busca suportam paginação e filtros parciais por login.

### Espaços Físicos (PhysicalSpace)
- Cada espaço físico pode estar vinculado a várias solicitações (Request), mas deve manter integridade referencial.  
- O nome do espaço deve ser único; tentativas de cadastro ou atualização com nomes duplicados lançam exceção clara.  
- Operações de criação, atualização e deleção respeitam integridade referencial; exclusão de espaços vinculados a solicitações lança exceção.  
- Criação de um espaço define `availability = true` por padrão.  
- Atualizações permitem modificar dados do espaço e associar novas solicitações sem duplicar as existentes.  
- Métodos de busca suportam:  
  - Paginação  
  - Filtragem por tipo, capacidade, nome ou disponibilidade  
  - Retorno simplificado ou completo com solicitações associadas

### Solicitações (Requests)
- Cada solicitação é vinculada a um usuário e a um espaço físico.  
- Conflito de horários não é permitido: não pode existir mais de uma solicitação aprovada para o mesmo espaço no mesmo período.  
- Um usuário não pode ter mais de uma solicitação pendente ao mesmo tempo.  
- Somente o dono da solicitação ou um administrador pode atualizá-la.  
- Solicitações só podem ser atualizadas se estiverem no status PENDENTE.  
- Operações de criação, atualização e deleção respeitam integridade referencial; exceções claras são lançadas se houver violação.  
- Métodos de busca suportam:  
  - Paginação  
  - Filtragem por status, título, usuário ou data  
  - Ordenação por data de criação (ascendente ou descendente)

### Histórico de Aprovações (ApprovalHistory)
- Cada histórico de aprovação está vinculado a um usuário e a uma solicitação (Request).  
- Ao criar ou atualizar um histórico:  
  - O status da solicitação vinculada é automaticamente atualizado:  
    - `APPROVED` se a decisão for positiva (`decision = true`)  
    - `REJECTED` se a decisão for negativa (`decision = false`)  
  - Apenas solicitações pendentes podem ser aprovadas ou rejeitadas por meio de um histórico de aprovação.  
- Quando uma solicitação é aprovada:  
  - Todas as solicitações conflitantes (mesmo espaço físico e horário) com status pendente são automaticamente rejeitadas.  
  - É agendada uma tarefa (Quartz) relacionada ao histórico de aprovação.  
- Conflitos de horários não são permitidos:  
  - Não é possível aprovar uma solicitação se já existir outra aprovada para o mesmo espaço no mesmo período.  
- Operações de criação, atualização e deleção lançam exceções claras se houver violação de integridade ou se o recurso não existir.  
- Métodos de busca suportam:  
  - Paginação  
  - Filtragem por usuário, decisão ou observação  
  - Ordenação por data de aprovação (ascendente ou descendente)

### SchedulerService
- **Objetivo:** Automatizar a marcação de disponibilidade de recursos com base em aprovações (`ApprovalHistory`).  
- **Funcionalidade:** Para cada `ApprovalHistory`, agenda dois jobs automáticos via Quartz Scheduler:  
  - `MarkUnavailableJob` → marca o recurso como indisponível no início do período (`dateTimeStart`).  
  - `MarkAvailableJob` → marca o recurso como disponível ao final do período (`dateTimeEnd`).  
- **Identificação:** Cada job recebe um ID único derivado do `Request` associado ao `ApprovalHistory`.  
- **Execução:** Os jobs são disparados automaticamente nas datas/horas configuradas, sem necessidade de intervenção manual.

### Autenticação e Recuperação de Senha (Auth)
- Usuários podem solicitar recuperação de senha via e-mail.  
- Cada token de recuperação tem validade limitada; tokens expirados são inválidos.  
- Apenas tokens válidos podem ser usados para definir uma nova senha.  
- Senhas são armazenadas criptografadas usando `PasswordEncoder`.  
- Operações de criação e atualização respeitam integridade referencial; exceções claras são lançadas se houver violações.  
- Usuário não encontrado ou token inválido/expirado gera exceções específicas.

### Envio de E-mail (EmailService)
- Todos os e-mails enviados usam um remetente padrão.  
- E-mails são enviados via `JavaMailSender`.  
- Falha no envio de e-mail dispara exceção específica.  
- Serviço é utilizado por outros serviços, como `AuthService`, para comunicação com usuários.

### Auditoria (Audit)
- Todas as ações do sistema são registradas com usuário, ação e detalhes.  
- Métodos de consulta suportam:  
  - Paginação  
  - Filtragem por usuário ou ação  
- Mantém histórico confiável de ações para auditoria e monitoramento.

## Pré-requisitos

- Java 17+
- Maven
- PostgreSQL (para produção e desenvolvimento)

## Instalação e Configuração

## 1. Clone o repositório:
```bash
git clone https://github.com/FelipeGabrill/AlocacaoDeEspacosFisicos-SpringBoot
```

## 2. Acesse a pasta do projeto:
```bash
cd arqsoftware
```

# Configuração para dev

## Variáveis de Ambiente

### Configuração da Aplicação
| Variável | Descrição | Valor padrão |
|----------|-----------|-------------|
| `spring.application.name` | Nome da aplicação no Spring Boot | `arqsoftware` |
| `spring.profiles.active` | Perfil ativo (`dev`, `prod`, `test`) | `${APP_PROFILE:test}` |
| `spring.jpa.open-in-view` | Controla se a sessão JPA fica aberta durante a renderização | `false` |

### Segurança e Autenticação
| Variável | Descrição | Valor padrão |
|----------|-----------|-------------|
| `security.client-id` | ID do cliente para autenticação | `${CLIENT_ID:myclientid}` |
| `security.client-secret` | Segredo do cliente para autenticação | `${CLIENT_SECRET:myclientsecret}` |
| `security.jwt.duration` | Duração do token JWT em segundos | `${JWT_DURATION:86400}` |

### CORS
| Variável | Descrição | Valor padrão |
|----------|-----------|-------------|
| `cors.origins` | Lista de origens permitidas para requisições cross-origin | `${CORS_ORIGINS:http://localhost:3000, http://junction.proxy.rlwy.net:50547/}` |

### Configuração de E-mail (Gmail)
| Variável | Descrição | Valor padrão |
|----------|-----------|-------------|
| `spring.mail.host` | Servidor SMTP | `${EMAIL_HOST:smtp.gmail.com}` |
| `spring.mail.port` | Porta do servidor SMTP | `${EMAIL_PORT:587}` |
| `spring.mail.username` | Usuário de envio de e-mail | `${EMAIL_USERNAME:test@gmail.com}` |
| `spring.mail.password` | Senha do e-mail | `${EMAIL_PASSWORD:test123}` |
| `spring.mail.properties.mail.smtp.auth` | Habilita autenticação SMTP | `true` |
| `spring.mail.properties.mail.smtp.starttls.enable` | Habilita TLS para envio seguro | `true` |

### Recuperação de Senha
| Variável | Descrição | Valor padrão |
|----------|-----------|-------------|
| `email.password-recover.token.minutes` | Validade do token de recuperação de senha em minutos | `${PASSWORD_RECOVER_TOKEN_MINUTES:30}` |
| `email.password-recover.uri` | URL para redefinir a senha | `${PASSWORD_RECOVER_URI:http://localhost:5173/recover-password/}` |

### Swagger / OpenAPI
| Variável | Descrição | Valor padrão |
|----------|-----------|-------------|
| `springdoc.api-docs.path` | Caminho para acessar a documentação OpenAPI | `/api-docs` |
| `springdoc.swagger-ui.path` | Caminho para acessar a Swagger UI | `/swagger-ui.html` |
| `springdoc.swagger-ui.enabled` | Ativa ou desativa a Swagger UI | `true` |

### Banco de Dados (PostgreSQL)
| Variável | Descrição | Valor padrão |
|----------|-----------|-------------|
| `spring.datasource.url` | URL de conexão com o banco de dados | `jdbc:postgresql://localhost:5433/arqtsoftware` |
| `spring.datasource.username` | Usuário do banco | `postgres` |
| `spring.datasource.password` | Senha do banco | `1234567` |
| `spring.jpa.database-platform` | Dialeto do Hibernate para PostgreSQL | `org.hibernate.dialect.PostgreSQLDialect` |
| `spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation` | Criação de LOBs sem contexto | `true` |
| `spring.jpa.hibernate.ddl-auto` | Estratégia de criação de schema | `none` |
| `spring.jpa.properties.jakarta.persistence.schema-generation.create-source` | Fonte para criação do schema (comentada) | `metadata` |
| `spring.jpa.properties.jakarta.persistence.schema-generation.scripts.action` | Ação para geração de scripts (comentada) | `create` |
| `spring.jpa.properties.jakarta.persistence.schema-generation.scripts.create-target` | Arquivo de script gerado (comentada) | `create.sql` |
| `spring.jpa.properties.hibernate.hbm2ddl.delimiter` | Delimitador SQL (comentada) | `;` |

### Quartz (Agendador de Jobs)
| Variável | Descrição | Valor padrão |
|----------|-----------|-------------|
| `spring.quartz.job-store-type` | Tipo de armazenamento dos jobs | `jdbc` |
| `spring.quartz.jdbc.initialize-schema` | Inicializa o schema do Quartz | `always` |
| `spring.quartz.properties.org.quartz.jobStore.isClustered` | Habilita cluster de Quartz | `true` |
| `spring.quartz.properties.org.quartz.scheduler.instanceId` | ID do scheduler | `AUTO` |
| `spring.quartz.properties.org.quartz.jobStore.driverDelegateClass` | Delegado JDBC específico para PostgreSQL | `org.quartz.impl.jdbcjobstore.PostgreSQLDelegate` |
| `spring.quartz.properties.org.quartz.jobStore.tablePrefix` | Prefixo das tabelas do Quartz | `QRTZ_` |

# Execução da Aplicação

## 4. Instale as dependências:

```bash
mvn clean install
```

## 5. Execute a aplicação:

Para o ambiente de teste (`test`), utilize o seguinte comando:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

# Execução da Aplicação

Para o ambiente de desenvolvimento (`dev`), utilize o seguinte comando:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

# Perfis e Configurações

A aplicação suporta três perfis principais, cada um com suas próprias configurações:

- **Prod (Produção)**: Utiliza PostgreSQL.
- **Dev (Desenvolvimento)**: Utiliza PostgreSQL.
- **Test**: Utiliza H2 (banco em memória).

## Segurança

A autenticação é feita via Spring Security com JWT. Para acessar endpoints protegidos, inclua o token JWT no cabeçalho da requisição:

```http
Authorization: Bearer <seu-token>
```
