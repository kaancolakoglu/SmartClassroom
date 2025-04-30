FROM eclipse-temurin:21-jre
ARG JAR_FILE=target/*.jar
ARG SPRING_PROFILE=prod

COPY ${JAR_FILE} application.jar

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILE}

ENTRYPOINT ["java", "-jar", "application.jar"]