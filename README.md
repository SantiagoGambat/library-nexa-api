# Library Nexa API

API REST para la gestión de una biblioteca. Permite administrar usuarios, libros, ejemplares y préstamos.

## Tecnologías

- Java 17
- Spring Boot 3.5.4
- Spring Data JPA
- PostgreSQL 17
- Lombok
- MapStruct
- Maven
- Docker
- Docker Compose

## Estructura

    backend/
    ├── src/
    ├── database/
    │   └── dumps/
    │       └── datos_prueba.dump
    ├── Dockerfile
    ├── docker-compose.yml
    ├── .env.example
    ├── .gitignore
    ├── pom.xml
    └── README.md

## Requisitos

- Docker
- Docker Compose

No es necesario instalar Java ni Maven, ya que la aplicación se construye dentro de Docker.

## Configuración

Crear el archivo `.env` a partir del archivo de ejemplo:

    cp .env.example .env

Ejemplo de variables:

    #APP PORT
    API_PORT=8080

    #DATABASE
    DB_PORT=5432
    DB_NAME=library
    DB_USER=postgres
    DB_PASSWORD=postgres

    #URL DEL FRONTEND
    CORS_ALLOWED_ORIGIN=http://localhost:5173 

## Ejecución

Desde la raíz del proyecto:

    cp .env.example .env

    #El comando de abajo ya (INICIALIZA O RESTAURA) LOS DATOS DE PRUEBA DE LA BASE DE DATOS
    docker compose up -d --build
    docker compose ps

La API estará disponible en:

    http://localhost:8080

Para detener los servicios:

    docker compose down

## Base de datos

PostgreSQL se ejecuta mediante la imagen oficial `postgres:17-alpine`.

    Host: localhost
    Port: 5432
    Database: library
    User: postgres
    Password: postgres

La API se conecta internamente mediante:

    jdbc:postgresql://database:5432/library

Los datos se mantienen mediante el volumen Docker `postgres_data`.

## Datos de prueba

El respaldo de la base de datos se encuentra en: 

    database/dumps/library_backup.dump
    #Estos datos ya son inicializados con docker 

Este archivo contiene los datos de prueba necesarios para validar la API.

## API

La aplicación expone endpoints REST para:

- Usuarios
- Libros
- Ejemplares
- Préstamos

Base URL:

    http://localhost:8080

Endpoint Base:

    BASE_URL/api/v1

## Comandos útiles

Ver logs:

    docker compose logs -f

Detener los servicios:

    docker compose stop

Eliminar contenedores:

    docker compose down

Eliminar contenedores y datos:

    docker compose down -v

## Arquitectura

    Docker Compose
          │
          ├── Spring Boot API :8080
          │         │
          │         │ JDBC
          │         ▼
          └── PostgreSQL :5432

La API y PostgreSQL se ejecutan como servicios independientes dentro de Docker Compose y se comunican mediante la red interna de Docker.
