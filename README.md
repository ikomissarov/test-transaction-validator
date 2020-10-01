# How to build and run locally:
1. `./gradlew clean build`
2. `docker-compose up -d`
3. `java -jar build/libs/test-transaction-validator-0.0.1-SNAPSHOT.jar`

# How to build and run docker image:
1. `docker build -t ikomissarov/test-transaction-validator .`
2. `docker run --rm --net=host -p 8081:8081 -t ikomissarov/test-transaction-validator`

# API:
1. POST `localhost:8081/transactions/validate`

# Swagger:
1. `localhost:8081/v2/api-docs`
2. `localhost:8081/swagger-ui.html`
