# Build stage
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy gradle wrapper and make it executable
COPY gradlew gradlew
COPY gradle gradle
RUN chmod +x gradlew

# Copy build files
COPY build.gradle settings.gradle ./

# Copy source code
COPY src src

# Build the application, skipping tests for faster build
RUN ./gradlew build -x test

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar ./

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["sh", "-c", "java -jar file_services-0.0.1-SNAPSHOT.jar"]