FROM gradle:8.7-jdk21 AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .

RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/

EXPOSE 8080
CMD ["java", "-jar", "authenticator-0.0.1-SNAPSHOT.jar"]