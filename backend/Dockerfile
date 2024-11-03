# 빌드 단계
FROM amazoncorretto:21-alpine AS build
WORKDIR /workspace
COPY gradlew /workspace/gradlew
COPY gradle /workspace/gradle
COPY app/build.gradle /workspace/build.gradle
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon || true
COPY app/src /workspace/src
RUN ./gradlew bootJar --no-daemon

# 런타임 단계
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
RUN adduser -D appuser
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
