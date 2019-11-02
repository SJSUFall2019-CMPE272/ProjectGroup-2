# rinnovation-service

## Build

```
docker build -t rinnovation/rinnovation-service .
```

## Run

```
docker run -it -p 9000:9000 --rm rinnovation/rinnovation-service
```

## Push

```
docker push rinnovation/rinnovation-service
```

[Docker Hub Repository](https://cloud.docker.com/u/rinnovation/repository/docker/rinnovation/rinnovation-service)

## References

### Play

* [Using Play in Production](https://www.playframework.com/documentation/2.6.x/Production)
* [Deploying a Play Application](https://www.playframework.com/documentation/2.6.x/Deploying)
* [Play Framework x Docker Hello World](https://medium.com/@shatil/play-framework-https-hello-world-with-docker-62963cf26daf)

### Docker Hub
 
* [Docker Hub Quickstart](https://docs.docker.com/docker-hub)

### Amazon ECS

* [What is Amazon ECS?](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/Welcome.html)
* [Deploy Docker Containers to Amazon ECS](https://aws.amazon.com/getting-started/tutorials/deploy-docker-containers)
* [Setting up with Amazon ECS](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/get-set-up-for-amazon-ecs.html)
* [Updating the service on ECS after pushing a new image to Docker Hub](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/update-service.html) (Focus specifically on *Force new deployment*)
* [Secrets Management](https://aws.amazon.com/blogs/compute/managing-secrets-for-amazon-ecs-applications-using-parameter-store-and-iam-roles-for-tasks/)

### CI/CD

* [Continuous Delivery Pipeline for Amazon ECS Using Jenkins, GitHub, and Amazon ECR](https://github.com/aws-samples/aws-cicd-docker-containers)
* [Docker Hub: Set up Automated Builds](https://docs.docker.com/docker-hub/builds)