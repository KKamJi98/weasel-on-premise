#### 빌드 단계 ####
FROM openjdk:21-jdk-slim AS build
WORKDIR /workspace
# Gradle Wrapper 및 필요한 파일 복사
COPY gradlew /workspace/gradlew
COPY gradle /workspace/gradle
COPY app/build.gradle /workspace/build.gradle
# 실행 권한 부여
RUN chmod +x gradlew
# 의존성 캐시를 활용하기 위해 Gradle 빌드 실행 (테스트 제외)
RUN ./gradlew build -x test --no-daemon || true
COPY app/src /workspace/src
# 애플리케이션 빌드
RUN ./gradlew bootJar --no-daemon

#### 런타임 단계 ####
FROM openjdk:21-slim
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /workspace/build/libs/*.jar app.jar

# 비루트 사용자 생성 및 권한 설정
RUN useradd -m appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
