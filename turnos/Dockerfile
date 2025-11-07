FROM eclipse-temurin:21-jdk-jammy
ARG JAR_FILE=target/turnos-0.0.1.jar
COPY ${JAR_FILE} app-turnos.jar
ENTRYPOINT ["java","-jar","app-turnos.jar"]