# PawfectMatch Backend

## Overview

This repository contains the Spring Boot backend for the PawfectMatch application.

The backend currently provides:

- Spring Boot REST backend infrastructure
- In-memory H2 database
- Automatic database initialization on startup
- Seed test data for repeatable API testing
- Local development setup using Maven Wrapper

This setup ensures that every developer can run the backend locally with the same database state, allowing consistent API testing using tools such as Insomnia or Postman.

---

# Project Structure

```
PawfectMatch
│
└── backend
    │
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── com/pawfectmatch/backend
    │   │   │
    │   │   └── resources
    │   │       ├── application.properties
    │   │       ├── schema.sql
    │   │       └── data.sql
    │   │
    │   └── test
    │
    ├── pom.xml
    ├── mvnw
    ├── mvnw.cmd
    └── .mvn
```

### Important Files

| File | Purpose |
|-----|------|
| `pom.xml` | Maven project configuration |
| `application.properties` | Spring Boot configuration |
| `schema.sql` | Defines database tables |
| `data.sql` | Inserts repeatable test data |
| `mvnw / mvnw.cmd` | Maven wrapper to run the project |

---

# Technologies Used

- Spring Boot
- Spring Web
- Spring Data JPA
- H2 In-Memory Database
- Maven

---

# Local Development Setup

Follow these steps to run the backend locally.

---

# 1 Install Java

Install **JDK 17**.

Recommended distribution:

Eclipse Temurin JDK 17

Verify installation:

```
java -version
javac -version
```

Both commands should show version **17**.

---

# 2 Clone the Repository

```
git clone <repo-url>
```

Navigate to the backend directory:

```
cd PawfectMatch/backend
```

---

# 3 Run the Backend

### Mac / Linux

```
./mvnw spring-boot:run
```

### Windows

```
mvnw.cmd spring-boot:run
```

If the server starts successfully you should see something similar to:

```
Started BackendApplication in X seconds
Tomcat started on port(s): 8080
```

---

# 4 Access the Application

Open a browser and navigate to:

```
http://localhost:8080
```

A blank or error page is normal if no endpoints are defined yet.

---

# H2 Database

The backend uses an **H2 in-memory database** for development.

This database is recreated every time the application starts, ensuring all developers have the same database state.

---

# Access the H2 Console

Open:

```
http://localhost:8080/h2-console
```

Use the following credentials:

```
JDBC URL: jdbc:h2:mem:pawfectdb
User: sa
Password: (leave blank)
```

After connecting you can run SQL queries such as:

```
SELECT * FROM pets;
```

---

# Database Initialization

Two SQL files automatically initialize the database on startup.

### schema.sql

Defines the database structure.

Example:

```
CREATE TABLE pets (
  id INT PRIMARY KEY,
  name VARCHAR(100),
  species VARCHAR(50),
  breed VARCHAR(100),
  age INT
);
```

### data.sql

Inserts repeatable test data.

Example:

```
INSERT INTO pets (id, name, species, breed, age) VALUES
(1, 'Milo', 'Dog', 'Golden Retriever', 3),
(2, 'Luna', 'Cat', 'Domestic Shorthair', 2),
(3, 'Charlie', 'Dog', 'Poodle', 4);
```

Because these files run on every startup:

- the database is reset
- the same records are inserted
- API tests are repeatable

---

# Testing with Insomnia or Postman

Once endpoints are implemented, developers can test APIs locally.

Example request:

```
GET http://localhost:8080/pets
```

Because the database resets on startup, this request will always return the same data.

---

# Development Workflow

When working on a feature:

1. Create a feature branch

```
git checkout -b feature/<feature-name>
```

2. Make changes

3. Commit changes

```
git add .
git commit -m "Describe change"
```

4. Push branch

```
git push origin feature/<feature-name>
```

5. Open a Pull Request into `main`.

---

# Current Backend Capabilities

- Spring Boot backend infrastructure
- Runs locally with Maven wrapper
- H2 database configured
- Automatic database initialization
- Repeatable test data for API testing

---

# Future Development

Planned backend features include:

- REST API endpoints
- User authentication
- Persistent database (PostgreSQL or MySQL)
- Business logic services
- Integration with frontend