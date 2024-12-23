FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml pom.xml
COPY src src
RUN mvn clean package -DskipTests=true

FROM bellsoft/liberica-openjre-alpine-musl:23
WORKDIR /app
COPY --from=build /app/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
