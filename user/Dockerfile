# Use an official OpenJDK runtime as a parent image
FROM openjdk:8-jre-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY target/user-1.0.jar /app

# Make port 8080 available to the world outside this container
EXPOSE 8081

# Run the JAR file when the container launches
CMD ["java", "-jar", "user-1.0.jar"]