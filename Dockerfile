# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom files
COPY pom.xml .
COPY worker-service-app/pom.xml worker-service-app/
COPY worker-service-api/pom.xml worker-service-api/
COPY worker-service-client/pom.xml worker-service-client/
COPY worker-service-model/pom.xml worker-service-model/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY worker-service-app/src worker-service-app/src
COPY worker-service-api/src worker-service-api/src
COPY worker-service-client/src worker-service-client/src
COPY worker-service-model/src worker-service-model/src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built artifact
COPY --from=builder /app/worker-service-app/target/*.jar app.jar

# Run the application
CMD ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]