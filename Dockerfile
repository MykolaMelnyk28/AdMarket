FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:17-slim

WORKDIR /app

COPY --from=builder /app/src src
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080:8080

CMD ["java", "-jar", "app.jar"]