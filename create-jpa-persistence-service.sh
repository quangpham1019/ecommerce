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

# other variables
BASE_CLASS_PREFIX="Common"
IMPL_SUFFIX="_JpaRepository"

# Set the input directory based on the package and context
input_directory="src/main/java/$PROJECT_PACKAGE/$CONTEXT_NAME/domain/model"

# Set the output directory based on the package and context
output_directory_impl="src/main/java/$PROJECT_PACKAGE/$CONTEXT_NAME/infrastructure/service"
output_directory_interface="src/main/java/$PROJECT_PACKAGE/$CONTEXT_NAME/infrastructure/service/interfaces"

# Create the output directory if it doesn't exist
mkdir -p "$output_directory_impl"
mkdir -p "$output_directory_interface"

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

      persistence_service_interface="${class_name}PersistenceService"
      persistence_service_impl="${persistence_service_interface}${IMPL_SUFFIX}"
      persistence_service_interface_content="package org.ecommerce.$CONTEXT_NAME.infrastructure.service.interfaces;

import org.ecommerce.$CONTEXT_NAME.domain.model.${class_name};

public interface ${persistence_service_interface} extends CommonPersistenceService<${class_name}, ${id_type}> {}"

      persistence_service_impl_content="package org.ecommerce.$CONTEXT_NAME.infrastructure.service;

import org.ecommerce.$CONTEXT_NAME.domain.model.${class_name};
import org.ecommerce.$CONTEXT_NAME.infrastructure.repository.jpa.${class_name}Repository;
import org.ecommerce.$CONTEXT_NAME.infrastructure.service.interfaces.${persistence_service_interface};
import org.springframework.stereotype.Service;

@Service
public class ${persistence_service_impl} extends ${BASE_CLASS_PREFIX}PersistenceService${IMPL_SUFFIX}<${class_name}, ${id_type}> implements ${persistence_service_interface} {

    private final ${class_name}Repository repository;

    public ${class_name}PersistenceService${IMPL_SUFFIX}(${class_name}Repository repository) {
        super(repository);
        this.repository = repository;
    }
}"

      # Output the content to a file in the output directory
      echo -e "$persistence_service_interface_content" > "${output_directory_interface}/${persistence_service_interface}.java"

      echo "Repository interface '${output_directory_interface}/${persistence_service_interface}.java' has been created."


      echo -e "${persistence_service_impl_content}" > "${output_directory_impl}/${persistence_service_impl}.java"

      echo "Repository interface '${output_directory_impl}/${persistence_service_impl}.java' has been created."
    else
      echo "Class name or ID field not found in file: $java_file"
    fi
  fi
done
