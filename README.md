# Email Microservice

- [Email Microservice](#email-microservice)
  - [Programming Stack](#programming-stack)
  - [Running the application](#running-the-application)
    - [Docker](#docker)
  - [Testing the application](#testing-the-application)
  - [REST API Documentation](#rest-api-documentation)
    - [Swagger UI](#swagger-ui)
    - [OpenApi v3 Specification](#openapi-v3-specification)

- An independent microservice which completes incoming mail requests asynchronously thanks to Kotlin Coroutines

## Programming Stack

- Kotlin
- Gradle
- SpringBoot

## Running the application

- Execute the command using one of the available profiles:
  - dev
  - prod
- For example, the following command will start the application in debug mode based on application-dev.properties

  ```shell
  gradle buildWithProfile -PbuildProfile=dev bootRun
  ```

### Docker

- Application can also run isolated inside a docker container. After building the application, simply execute on of the
  following commands:
  - With docker compose

    ```shell
    docker-compose up -d
    ```

  - With [Gradle Docker plugin](https://github.com/palantir/gradle-docker)

    ```shell
    gradle docker # for building the docker image
    gradle dockerComposeUp # for creating and running a container based on the created docker image
    ```

## Testing the application

- This project was developed using [IntelliJ Ultimate,](https://www.jetbrains.com/lp/intellij-frameworks/) so it
contains some specific functionality it supports such as http-tests using the bundled HTTP client of the IDE. The rest
of the tests are running either on demand, either on project build.
- For test purposes, [MailTrap](https://mailtrap.io/) is being used for sending emails

## REST API Documentation

### Swagger UI

- An endpoints collections of the project alongside documentation and examples can be found
  here: <http://localhost:7000/swagger-ui/index.html>

### OpenApi v3 Specification

- As JSON
  - <http://localhost:7000/v3/api-docs>
  - The OpenAPI specification of the project can be also stored locally under `/build/docs` by running the following
    command:

    ```shell
    gradle clean generateOpenApiDocs
    ```

- As yaml
  - <http://localhost:7000/v3/api-docs.yaml>
