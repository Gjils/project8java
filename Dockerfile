FROM gradle:8.13-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build --info

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]