[![Java Maven Build Test](https://github.com/deepaksorthiya/spring-boot-testing-web-complete/actions/workflows/maven-build.yml/badge.svg)](https://github.com/deepaksorthiya/spring-boot-testing-web-complete/actions/workflows/maven-build.yml)

---

### ** Spring Boot Web Mvc Testing - Integration and Unit Tests **

---

# Getting Started

## Requirements:

```
Git: 2.51+
Spring Boot: 4.0.1
Maven: 3.9+
Java: 25
```

## Clone Github Repository

```bash
git clone https://github.com/deepaksorthiya/spring-boot-testing-web-complete
cd spring-boot-testing-web-complete
```

# Run Tests

## Running only Unit Tests

```bash
./mvnw clean test
```

## Running Unit Tests and Integration Tests:

```bash
./mvnw clean verify
```

## Running Integration Tests but not Unit Test

```bash
./mvnw clean verify -DskipUnitTests=true
```

## Running Unit Tests but not Integration Tests:

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

## Running unit tests only (it uses maven surefire plugin)

```bash
./mvnw  compiler:testCompile resources:testResources  surefire:test
```

## Running integration tests only (it uses maven-failsafe-plugin)

```bash
./mvnw  compiler:testCompile resources:testResources  failsafe:integration-test
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

