FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle gradle.properties ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true
COPY src/ src/
RUN ./gradlew build -Dquarkus.package.type=uber-jar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S quarkus && adduser -S quarkus -G quarkus
COPY --from=build /app/build/*-runner.jar app.jar
USER quarkus
EXPOSE 8080 9000
ENTRYPOINT ["java", "-jar", "app.jar"]
