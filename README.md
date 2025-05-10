# Pelada Gestão - Backend

Este projeto é uma API REST desenvolvida em Java com Spring Boot para gerenciar sorteios e jogadores de peladas (futebol amador). O backend permite cadastrar, listar, sortear e excluir jogadores e sorteios.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 2.7.18
- Spring Data JPA
- Spring Boot Starter Validation
- PostgreSQL
- Maven

## Configuração do Ambiente

### Pré-requisitos
- Java 17 instalado
- Maven instalado
- PostgreSQL instalado e em execução

### Configuração do Banco de Dados

No arquivo `src/main/resources/application.properties`, configure as propriedades de acesso ao banco de dados PostgreSQL:

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

## Endpoints Principais

- `POST /api/sorteios` - Criar sorteio
- `GET /api/sorteios` - Listar sorteios
- `DELETE /api/sorteios/{id}` - Excluir sorteio
- `GET /api/sorteios/sortear/{id}` - Sortear times
- `GET /api/sorteios/resultado/{id}` - Ver resultado do sorteio

