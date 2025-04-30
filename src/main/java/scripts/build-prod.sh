#!/bin/bash
# Build the application
./mvnw clean package

# Build Docker image with production profile
docker build --build-arg SPRING_PROFILE=prod -t smart-classroom-app:prod .