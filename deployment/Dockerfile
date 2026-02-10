FROM gradle:8.10-jdk17 AS build
WORKDIR /workspace

# Copiamos todo el repo (multi-módulo)
COPY . .

# Construye el jar ejecutable del módulo app-service
RUN gradle :app-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el jar generado (nombre depende de tu bootJar config)
COPY --from=build /workspace/applications/app-service/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
