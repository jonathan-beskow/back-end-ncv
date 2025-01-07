# Use a imagem base do OpenJDK 21
FROM openjdk:21-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    libfreetype6 \
    libfontconfig \
    && rm -rf /var/lib/apt/lists/*

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
