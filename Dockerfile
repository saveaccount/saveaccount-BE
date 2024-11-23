# Use a JDK 17 base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file to the container
COPY ./build/libs/savemoney-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]
