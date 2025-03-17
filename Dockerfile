# Use a base image with JDK (Java Development Kit)
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container (source and destination are specified)
COPY build/libs/accounting-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (default Spring Boot port)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar","app.jar"]
