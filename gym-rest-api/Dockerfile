FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

#FROM openjdk:17-jdk-slim
FROM bellsoft/liberica-openjre-alpine-musl:23
WORKDIR /app
COPY --from=build /app/target/gym-1.0.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 8080
