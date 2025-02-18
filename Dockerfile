FROM openjdk:23-jdk-slim
LABEL authors="guill"

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper files
COPY .mvn/ .mvn/
COPY mvnw .
COPY mvnw.cmd .
COPY .env .

# Ensure the Maven wrapper script has execute permissions
RUN chmod +x mvnw

# Copy the Maven build file and install dependencies
COPY pom.xml .
COPY src ./src

# Package the application
RUN ./mvnw package

# Expose the port the application runs on
EXPOSE 8080:8080

# Run the application
ENTRYPOINT ["java", "-jar", "target/PowerZone_back-0.0.1-SNAPSHOT.jar"]