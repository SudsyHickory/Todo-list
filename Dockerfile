FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Cache dependencies
COPY mvnw pom.xml ./
COPY .mvn .mvn/

RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*SNAPSHOT.jar app.jar

ENV SERVER_PORT=80
EXPOSE 80

CMD ["java", "-jar", "app.jar"]