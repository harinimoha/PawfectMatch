# ── Stage 1: Build ──────────────────────────────────────────────────────────
# Uses Maven + Java 17 to compile your Spring Boot app into a .jar
FROM maven:3.9-eclipse-temurin-17 AS build

# Set a working directory inside the container (like a temp folder)
WORKDIR /app

# Copy your pom.xml first (so Maven can download dependencies)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Now copy the rest of your source code
COPY src ./src

# Build the app, skipping tests (faster for Docker builds)
RUN mvn package -DskipTests -q

# ── Stage 2: Run ────────────────────────────────────────────────────────────
# Smaller image — just needs Java to run the .jar, not Maven
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built .jar from Stage 1 into this smaller image
COPY --from=build /app/target/*.jar app.jar

# Tell Docker this container listens on port 8080
EXPOSE 8080

# The command that runs when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]