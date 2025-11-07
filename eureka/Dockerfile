FROM eclipse-temurin:21-jdk-jammy
ARG JAR_FILE=target/eureka-0.0.1.jar
COPY ${JAR_FILE} app-eureka.jar
ENTRYPOINT ["java","-jar","app-eureka.jar"]