FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app

# Cache dependencies
COPY mvnw pom.xml ./
COPY .mvn .mvn/

RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*SNAPSHOT.jar app.jar

ENV SERVER_PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]