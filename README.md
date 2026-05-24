# 📚 Library API a Spring Boot API

A simple Library Management REST API built with **Spring Boot (Gradle)**, using **HashiCorp Vault** for secrets management and **Redis** for caching.

Only infrastructure services (Vault + Redis) run in Docker. The application is run locally via Gradle.

---

##  Features

-  REST API for managing books, users, and loans
-  Secure configuration using HashiCorp Vault
-  Caching with Redis for improved performance

---

## Tech Stack

- Java 25
- - Gradle
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Data Redis
- Spring Cloud Vault
- H2
- Redis
- HashiCorp Vault
- Docker

---

## Infrastructure (Docker)

-Docker is used only to run supporting services:
-Remember to run docker-compose up -d

-Create database secrets in Vault 

##  Running the Project

### Start infrastructure services

Run Vault and Redis:

docker-compose up -d

### Enter Secrets

Place database credentials in vault (optional but part of security)

docker exec -it vault sh
vault kv put secret/library-api libraryapi.spring.datasource.username="Your admin" libraryapi.spring.datasource.password="Your password"

### Run the application
./gradlew bootRun

Access it over port 8080:  Create a user and login to recieve a JWT token for authenticating your requests
