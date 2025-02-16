#!/bin/bash

# Function to prompt user for input
prompt_input() {
    local prompt_message=$1
    local default_value=$2
    read -p "$prompt_message [$default_value]: " user_input
    echo "${user_input:-$default_value}"
}

# Prompt for project name and package structure
CONTEXT_NAME=$(prompt_input "Enter your context name" "my_context")
PROJECT_PACKAGE=$(prompt_input "Enter your project package name" "org/ecommerce")
ROOT_DIR="src/main/java/$PROJECT_PACKAGE"

# Create the root project directory
mkdir -p "$ROOT_DIR/$CONTEXT_NAME"

# Optionally create additional directories for common DDD layers (e.g., controllers, services, repositories)
mkdir -p "$ROOT_DIR/$CONTEXT_NAME/api/controller"

mkdir -p "$ROOT_DIR/$CONTEXT_NAME/application/dto"
mkdir -p "$ROOT_DIR/$CONTEXT_NAME/application/service"

mkdir -p "$ROOT_DIR/$CONTEXT_NAME/domain/event"
mkdir -p "$ROOT_DIR/$CONTEXT_NAME/domain/exception"
mkdir -p "$ROOT_DIR/$CONTEXT_NAME/domain/model"
mkdir -p "$ROOT_DIR/$CONTEXT_NAME/domain/repository"

mkdir -p "$ROOT_DIR/$CONTEXT_NAME/infrastructure/config"
mkdir -p "$ROOT_DIR/$CONTEXT_NAME/infrastructure/repository"
mkdir -p "$ROOT_DIR/$CONTEXT_NAME/infrastructure/security"

# Provide feedback
echo "Directory structure created for context '$CONTEXT_NAME' of project package '$PROJECT_PACKAGE'."
