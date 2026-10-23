[![Java Maven Build Test](https://github.com/deepaksorthiya/spring-boot-testing-web-complete/actions/workflows/maven-build.yml/badge.svg)](https://github.com/deepaksorthiya/spring-boot-testing-web-complete/actions/workflows/maven-build.yml)

---

### ** Spring Boot Web Mvc Testing - Integration and Unit Tests **

---

# Getting Started

## Requirements:

```
Git: 2.51+
Spring Boot: 4.1.1
Maven: 3.9+
Java: 25
Docker with Compose: required for PostgreSQL and full-context/integration tests
```

## Clone Github Repository

```bash
git clone https://github.com/deepaksorthiya/spring-boot-testing-web-complete
cd spring-boot-testing-web-complete
```

# PostgreSQL Post and PostComment example

The persistence example lives in `com.example.blog` and exposes `/api/posts`.
The original `/posts/{id}` endpoint remains an example of calling an external API.

Start PostgreSQL and the application:

```bash
docker compose up -d --wait postgres
bash mvnw spring-boot:run
```

Compose binds PostgreSQL to localhost on port 5432 and keeps its data in the
`post-data` volume. The sample development database, username, and password are
`posts`. Override the connection with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.
If you change `POSTGRES_PORT` for Compose, also update the port in `DB_URL`.
PostgreSQL initialization variables apply when the data volume is first created.
`docker compose stop` stops the database while retaining its data.

Flyway creates the tables and foreign key; Hibernate validates the schema.
`Post` owns a lazy, bidirectional `@OneToMany` collection of `PostComment` entities.
Removing a comment uses orphan removal; deleting a post cascades to its comments.
Transactions resolve lazy relationships before returning DTOs, and Open Session
in View is disabled. Responses do not serialize JPA entities or parent references.

| Method | Endpoint | Behavior |
| --- | --- | --- |
| POST | `/api/posts` | Create a post; returns 201 and `Location` |
| GET | `/api/posts?page=0&size=20` | List post summaries, ordered by ID; size 1–100 |
| GET | `/api/posts/{postId}` | Read a post with its comments |
| PUT | `/api/posts/{postId}` | Replace title/content, preserving comments |
| DELETE | `/api/posts/{postId}` | Delete a post and its comments; returns 204 |
| POST | `/api/posts/{postId}/comments` | Add a comment; returns 201 and `Location` |
| GET | `/api/posts/{postId}/comments/{commentId}` | Read a comment belonging to this post |
| PUT | `/api/posts/{postId}/comments/{commentId}` | Replace a comment's author/content |
| DELETE | `/api/posts/{postId}/comments/{commentId}` | Delete a comment; returns 204 |

Post requests contain `title` (1–200 characters) and `content` (1–10000).
Comment requests contain `author` (1–100) and `content` (1–2000).
All fields must contain non-whitespace text. Invalid requests return 400;
missing resources and comments addressed through another post return 404.
Errors use Problem Detail responses. This sample API has no authentication;
the parent ID check enforces the relationship, not user authorization.

```bash
curl -i -X POST http://localhost:8080/api/posts \
  -H 'Content-Type: application/json' \
  -d '{"title":"Learning JPA","content":"A post with comments"}'

# Replace 1 with the post ID returned above.
curl -i -X POST http://localhost:8080/api/posts/1/comments \
  -H 'Content-Type: application/json' \
  -d '{"author":"Ada","content":"Useful example"}'

curl http://localhost:8080/api/posts/1
```

The post list returns `content` plus `page` metadata (`number`, `size`,
`totalElements`, `totalPages`). Individual post responses include `comments`.

## Tests for the persistence example

```bash
# Isolated domain/service unit tests; no Docker required.
bash mvnw -Dtest=PostTest,BlogServiceTest test

# MVC slice: routing, JSON, validation, errors, pagination; no Docker required.
bash mvnw -Dtest=BlogControllerTest test

# Complete suite, including real PostgreSQL persistence and HTTP integration tests.
bash mvnw clean verify
```

`PostRepositoryIT` checks relationships after flushing and clearing the persistence
context, orphan removal, and cascade deletion. `BlogApiIT` checks the CRUD lifecycle
over HTTP and prevents reading or modifying a comment through another post.
Database tests start disposable PostgreSQL 17 containers through Testcontainers,
run the Flyway migration, and never use the developer database. Docker must be
available; the tests do not silently skip database checks. Existing full-context
tests also use this shared test configuration now that the application uses JPA.

# Run All Tests

## Running Surefire Tests

This phase includes unit tests, slices, and the legacy full-context tests whose
names end in `Test` or `Tests`; the latter require Docker. Tests named `*IT` run
under Failsafe during `verify`.

```bash
./mvnw clean test
```

## Running Unit Tests and Integration Tests:

```bash
./mvnw clean verify
```

## Running Failsafe Tests Only

```bash
./mvnw clean verify -DskipUnitTests=true
```

## Running Surefire Tests but not Failsafe Tests:

```bash
./mvnw clean verify -DskipITs=true
```

## Skip Both Unit and Integration Test

```bash
./mvnw clean verify -DskipTests
```

OR

```bash
./mvnw clean verify -Dmaven.test.skip=true
```

# Other Ways

## Running Surefire directly

```bash
./mvnw  compiler:testCompile resources:testResources  surefire:test
```

## Running Failsafe directly

```bash
./mvnw compiler:testCompile resources:testResources failsafe:integration-test failsafe:verify
```

## Maven Plugins Docs

* [Maven Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html#includes)
* [Maven Failsafe](https://maven.apache.org/surefire/maven-failsafe-plugin/integration-test-mojo.html#includes)

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot//io/validation.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

## Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

