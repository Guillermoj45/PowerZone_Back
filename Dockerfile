FROM openjdk:21-jdk-slim
LABEL authors="guill"

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper files
COPY .mvn/ .mvn/
COPY mvnw .
COPY mvnw.cmd .

# Copy the Maven build file and install dependencies
COPY pom.xml .
COPY src ./src

# Package the application
RUN ./mvnw package

# Copy the packaged jar file to the container
COPY target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 1234

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]