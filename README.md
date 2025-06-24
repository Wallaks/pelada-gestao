# Pelada Gestão - Backend

Este projeto possui API RESTs desenvolvidas em Java com Spring Boot para gerenciar sorteios e jogadores de peladas (futebol amador). O backend permite cadastrar, listar, sortear e excluir jogadores e sorteios.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Boot Starter Validation
- PostgreSQL
- Maven

## Configuração do Ambiente

### Pré-requisitos
- Java 21 instalado
- Maven instalado

### Configuração do Banco de Dados

No arquivo `src/main/resources/application-prod.properties`, configure as propriedades de acesso ao banco de dados PostgreSQL:

```
spring.datasource.url=sua_url
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Build e Execução

Para compilar o projeto:

```
mvn clean install
```

Para rodar a aplicação localmente:

```
mvn spring-boot:run
```

## Autenticação

Para acessar os endpoints protegidos, é necessário obter um token JWT através do endpoint de autenticação:

- `POST /auth/login`  
  Envie suas credenciais para receber o token JWT.

Inclua o token JWT no header `Authorization` das requisições:
Authorization: Bearer seu_token_jwt

## Endpoints Principais

- `POST /api/sorteios` - Criar sorteio (requer token)
- `GET /api/sorteios` - Listar sorteios (requer token)
- `DELETE /api/sorteios/{id}` - Excluir sorteio (requer token)
- `GET /api/sorteios/sortear/{id}` - Sortear times (requer token)
- `GET /api/sorteios/resultado/{id}` - Ver resultado do sorteio (requer token)

## Observações

- O endpoint `/actuator/**` está liberado e não exige autenticação.
- Todos os demais endpoints exigem autenticação via JWT.
