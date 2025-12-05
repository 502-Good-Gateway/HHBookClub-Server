# 1단계: Gradle로 스프링 부트 jar 빌드
FROM gradle:8.10-jdk21-alpine AS build
WORKDIR /app

# 로컬 프로젝트 소스를 Docker 컨테이너 내부로 복사
COPY . .

# jar 빌드 (bootJar 실행)
RUN gradle bootJar --no-daemon


# 2단계: 실행 전용 이미지 (가벼운 JRE만 포함)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 위에서 빌드한 jar를 가져옴
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# 컨테이너 시작 시 jar 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
