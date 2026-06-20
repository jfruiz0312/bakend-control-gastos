# --- Etapa 1: Compilación de la aplicación ---
FROM maven:3.8.8-eclipse-temurin-17-alpine AS build

# Definir el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml para descargar las dependencias primero (aprovecha la caché de Docker)
COPY pom.xml .

# Descargar las dependencias del proyecto sin compilar el código fuente todavía
RUN mvn dependency:go-offline -B

# Copiar el código fuente de tu aplicación app.management
COPY src ./src

# Compilar y empaquetar la aplicación omitiendo los tests para acelerar el despliegue
RUN mvn clean package -DskipTests

# --- Etapa 2: Entorno de ejecución para producción ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar únicamente el archivo .jar generado en la etapa anterior
# Reemplaza 'management-0.0.1-SNAPSHOT.jar' por el nombre real de tu archivo generado si es diferente
COPY --from=build /app/target/*.jar app.management.jar

# Exponer el puerto por defecto de Spring Boot (Render mapeará este puerto automáticamente)
EXPOSE 8080

# Comando para ejecutar la aplicación optimizando el uso de memoria en contenedores
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.management.jar"]
