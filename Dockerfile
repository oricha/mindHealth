FROM eclipse-temurin:21-jre

ARG APP_NAME=mindhealth
WORKDIR /app

# Copy build artifact from Gradle build context
COPY build/libs/*.jar app.jar

# Default profile can be overridden via env
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]

