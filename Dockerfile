FROM openjdk:17-jdk
WORKDIR /app
ADD target/money-transfer-service.jar money-transfer-service.jar
ENTRYPOINT ["java", "-jar", "money-transfer-service.jar"]