# --- Etapa 1: Compilación de la aplicación ---
FROM maven:3.8.8-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiar el pom.xml primero para aprovechar la caché
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar y empaquetar (omitir tests para agilizar)
RUN mvn clean package -DskipTests

# --- Etapa 2: Entorno de ejecución para producción ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el .jar generado desde la etapa 'build'
COPY --from=build /app/target/*.jar app.management.jar

# El puerto se expone (solo informativo, no obligatorio)
EXPOSE 8080

# Usamos shell para expandir la variable PORT (Render o Railway la inyectan)
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=prod -Dserver.port=${PORT:-8080} -Dserver.address=0.0.0.0 -jar app.management.jar"]
