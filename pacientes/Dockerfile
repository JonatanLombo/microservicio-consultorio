FROM eclipse-temurin:21-jdk-jammy
ARG JAR_FILE=target/pacientes-0.0.1.jar
COPY ${JAR_FILE} app-pacientes.jar
ENTRYPOINT ["java","-jar","app-pacientes.jar"]