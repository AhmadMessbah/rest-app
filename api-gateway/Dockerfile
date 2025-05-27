# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn package -DskipTests

# Run stage
FROM openjdk:17-slim
WORKDIR /app

# Create a non-root user
RUN useradd -r -u 1001 -g root appuser

# Copy the built artifact from build stage
COPY --from=build /app/target/*.jar app.jar

# Create directory for logs
RUN mkdir -p /app/logs && chown -R appuser:root /app/logs

# Switch to non-root user
USER appuser

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]