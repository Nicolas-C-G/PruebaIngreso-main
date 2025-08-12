# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM gradle:8.8-jdk21 AS build
WORKDIR /workspace
COPY . .
# Build the Spring Boot fat jar
RUN gradle clean bootJar --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
ENV TZ=America/Santiago
WORKDIR /app
# Copy the jar produced by Spring Boot
COPY --from=build /workspace/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
