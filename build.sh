#!/bin/bash

# Exit on any error
set -e

# Configuration
APP_NAME="mindhealth"
ECR_REPOSITORY="your-ecr-repository"
AWS_REGION="your-aws-region"
AWS_ACCOUNT_ID="your-aws-account-id"

#!/bin/bash

# Exit on any error
set -e

# Configuration
APP_NAME="mindhealth"
IMAGE_TAG="latest"

# Build the application (Gradle)
echo "Building application (Gradle)..."
./gradlew clean bootJar -x test

# Build Docker image
echo "Building Docker image..."
docker build -t $APP_NAME:$IMAGE_TAG .

if [[ -n "$ECR_REPOSITORY" && -n "$AWS_REGION" && -n "$AWS_ACCOUNT_ID" ]]; then
  # Tag the image for ECR
  docker tag $APP_NAME:$IMAGE_TAG $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPOSITORY:$IMAGE_TAG

  # Login to ECR and push (requires AWS CLI configured)
  echo "Logging into ECR..."
  aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

  echo "Pushing to ECR..."
  docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPOSITORY:$IMAGE_TAG
  echo "Build and push completed successfully!"
else
  echo "Built local image $APP_NAME:$IMAGE_TAG (skip ECR push; env not set)"
fi
