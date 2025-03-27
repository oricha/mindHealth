#!/bin/bash

# Exit on any error
set -e

# Configuration
APP_NAME="mindhealth"
ECR_REPOSITORY="your-ecr-repository"
AWS_REGION="your-aws-region"
AWS_ACCOUNT_ID="your-aws-account-id"

# Build the application
echo "Building application..."
./mvnw clean package -DskipTests

# Build Docker image
echo "Building Docker image..."
docker build -t $APP_NAME .

# Tag the image for ECR
docker tag $APP_NAME:latest $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPOSITORY:latest

# Login to ECR
echo "Logging into ECR..."
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# Push to ECR
echo "Pushing to ECR..."
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPOSITORY:latest

echo "Build and push completed successfully!" 