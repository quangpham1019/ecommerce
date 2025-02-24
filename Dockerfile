# Step 1: Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim as build

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy files into the container
COPY target/ecommerce-0.0.1-SNAPSHOT.jar ecommerce.jar
COPY src/main/resources config/

# Step 4: Expose the port your application will run on
EXPOSE 8080

# Step 5: Define environment variables as needed
# Tell Docker to use the application-docker.properties as the config file, located at "config/..."
ENV SPRING_CONFIG_LOCATION=config/application-docker.properties

# Step 6: Define the command to run the application
ENTRYPOINT ["java", "-jar", "ecommerce.jar"]