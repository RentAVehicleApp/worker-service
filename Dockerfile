FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom files
COPY pom.xml .
COPY worker-service-app/pom.xml worker-service-app/
COPY worker-service-model/pom.xml worker-service-model/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY worker-service-app/src worker-service-app/src
COPY worker-service-model/src worker-service-model/src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built artifact
COPY --from=builder /app/worker-service-app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application with Railway environment
ENTRYPOINT ["java", "-Dspring.profiles.active=railway", "-jar", "app.jar"]