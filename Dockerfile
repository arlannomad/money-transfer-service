FROM openjdk:17-jdk
WORKDIR /app
COPY target/money-transfer-service-0.0.1-SNAPSHOT.jar /app/money-transfer-service.jar
EXPOSE 8181
CMD ["java", "-jar", "money-transfer-service.jar"]