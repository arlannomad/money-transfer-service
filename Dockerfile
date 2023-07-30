FROM openjdk:17-jdk
EXPOSE 8181

WORKDIR /app
ADD target/money-transfer-service.jar money-transfer-service.jar
ENTRYPOINT ["java", "-jar", "money-transfer-service.jar"]