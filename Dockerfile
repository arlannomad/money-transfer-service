FROM openjdk:17-jdk
EXPOSE 8181
ADD target/money-transfer-service-0.0.1-SNAPSHOT.jar money-transfer-service.jar
ENTRYPOINT ["java", "-jar", "money-transfer-service.jar"]