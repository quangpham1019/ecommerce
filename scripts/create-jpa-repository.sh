#!/bin/bash

# Function to prompt user for input
prompt_input() {
    local prompt_message=$1
    local default_value=$2
    read -p "$prompt_message [$default_value]: " user_input
    echo "${user_input:-$default_value}"
}

# Prompt for project name and package structure
PROJECT_PACKAGE=$(prompt_input "Enter your project package name" "org/ecommerce")
CONTEXT_NAME=$(prompt_input "Enter your context name" "my_context")

# Set the input directory based on the package and context
input_directory="src/main/java/$PROJECT_PACKAGE/$CONTEXT_NAME/domain/model"

# Set the output directory based on the package and context
output_directory="src/main/java/$PROJECT_PACKAGE/$CONTEXT_NAME/infrastructure/repository/jpa"

# Create the output directory if it doesn't exist
mkdir -p "$output_directory"

# Loop through all .java files in the input directory
for java_file in "$input_directory"/*.java; do
  # Check if the file exists and is a regular file
  if [ -f "$java_file" ]; then
    # Extract the class name from the Java file (assuming class is public and it's the first match)
    class_name=$(grep -o 'public class [A-Za-z0-9]*' "$java_file" | sed 's/public class //')

    # Extract the ID field type (assuming the field is named 'id')
    id_type=$(grep -o 'private [A-Za-z0-9]* id;' "$java_file" | sed 's/private \(.*\) id;/\1/')

    # Debugging output to check extracted values
    echo "Class Name: $class_name"
    echo "ID Type: $id_type"

    # If class name and id type are found, create the repository interface
    if [ -n "$class_name" ] && [ -n "$id_type" ]; then
      # Generate the repository class name by appending "Repository"
      repository_class="${class_name}Repository"
      repository_file_path="${output_directory}/${repository_class}.java"

      # Check if the repository file already exists
      if [ -f "$repository_file_path" ]; then
        echo "Repository interface '${repository_class}' already exists. Skipping creation."
      else
              # Create the repository class content
              repository_content="package org.ecommerce.$CONTEXT_NAME.infrastructure.repository.jpa;

import org.ecommerce.$CONTEXT_NAME.domain.model.${class_name};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${repository_class} extends JpaRepository<${class_name}, ${id_type}> {}"

              # Output the content to a file in the output directory
              echo -e "$repository_content" > "${repository_file_path}"

              echo "Repository interface '${output_directory}/${repository_class}.java' has been created."
      fi
    else
      echo "Class name or ID field not found in file: $java_file"
    fi
  fi
done
