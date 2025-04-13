# === Stage 1: Build the application using Maven ===
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set workdir and copy source code
WORKDIR /build
COPY pom.xml .
COPY src ./src

# Package the app (creates target/ecommerce-0.0.1-SNAPSHOT.jar)
RUN mvn clean package -DskipTests

# === Stage 2: Run the application ===
# Step 1: Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim as run

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy files into the container
COPY --from=build /build/target/ecommerce-0.0.1-SNAPSHOT.jar ecommerce.jar
COPY --from=build /build/src/main/resources/application-docker.properties config/

# Step 4: Define environment variables as needed
# Tell Docker to use the application-docker.properties as the config file, located at "config/..."
ENV SPRING_CONFIG_LOCATION=config/application-docker.properties

# Step 5: Expose the port your application will run on
EXPOSE 8080

# Step 6: Define the command to run the application
ENTRYPOINT ["java", "-jar", "ecommerce.jar"]