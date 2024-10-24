# FROM openjdk:21-jdk-slim
FROM chainguard/jre:latest

WORKDIR /app

COPY ./app/build/libs/app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]