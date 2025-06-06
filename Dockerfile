# Build stage
FROM gradle:8.6-jdk21 AS builder
WORKDIR /build

COPY . .
RUN gradle build --info --no-daemon && ls -l /build/build/libs

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /build/build/libs/*.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar