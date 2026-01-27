#!/bin/bash

# Exit on error
set -e

# Force standard Docker socket to avoid user environment issues (e.g. Rancher Desktop)
export DOCKER_HOST=unix:///var/run/docker.sock

echo "========================================="
echo "  QuickHammer Local Build Script"
echo "========================================="

# Function to run maven in docker
mvn_docker() {
    echo "Running Maven in Docker..."
    docker run --rm \
        -v "$(pwd)":/app \
        -v "$HOME/.m2":/root/.m2 \
        -w /app \
        maven:3.9.6-eclipse-temurin-21 \
        mvn "$@"
}

# 1. Build common-events
echo "Building common-events..."
cd common-events
mvn_docker clean install -DskipTests
cd ..
echo "common-events built successfully."

# Function to build service
build_service() {
    SERVICE_NAME=$1
    IMAGE_NAME="thetharz/quickhammer-${SERVICE_NAME}:latest"
    
    echo "-----------------------------------------"
    echo "Building ${SERVICE_NAME}..."
    
    cd ${SERVICE_NAME}
    
    # Build JAR
    echo "Compiling JAR..."
    mvn_docker clean package -DskipTests
    
    # Build Docker Image
    echo "Building Docker Image: ${IMAGE_NAME}"
    docker build -f Dockerfile.local -t ${IMAGE_NAME} .
    
    cd ..
    echo "${SERVICE_NAME} built successfully."
}

# 2. Build all services
#build_service "eureka-server"
# build_service "api-gateway"
# build_service "auth-service"
# build_service "user-service"
build_service "job-service"
# build_service "bid-service"
build_service "notification-service"
# build_service "payment-service"

echo "========================================="
echo "  All services built successfully!"
echo "========================================="
echo "To start services, run: docker compose up -d"
