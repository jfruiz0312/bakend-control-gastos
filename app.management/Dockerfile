# --- Etapa 1: Compilación de la aplicación ---
FROM maven:3.8.8-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Entorno de ejecución para producción ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.management.jar

EXPOSE ${PORT:-8080}

# Usamos la forma shell para expandir variables de entorno
ENTRYPOINT java -Dspring.profiles.active=prod -Dserver.port=${PORT:-8080} -Dserver.address=0.0.0.0 -jar app.management.jar
