# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build

# Set up the working directory
WORKDIR /app

# Copy the pom.xml file into the working directory
COPY pom.xml .

# Download dependencies (optional - for faster builds)
RUN mvn dependency:go-offline

# Copy the source code into the working directory
COPY src /app/src

# Build the application
RUN mvn clean package

# Runtime stage
FROM openjdk:17-slim

# Set up the working directory
WORKDIR /app

# Copy the JAR file and configuration file(s) from the build stage
COPY --from=build /app/target/clinic-appointment-system.jar /app/app.jar
COPY --from=build /app/src/main/resources/application.properties /app/application.properties

# Set the ENTRYPOINT for your container
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
