# CNAT User Service
This microservice handles requests related to users such as user authentication and registration.
The User Service performs validation on requests and encrypts the plain user passwords before 
storing them in the database.

## Software Architecture:
The software architecture in this microservice consists of three layers; Controller layer, service 
layer, and repository layer. Request objects are used to define the request body for each 
endpoint and responses are either a single or an array of DTOs.

The controller layer is responsible for creating the URL mapping with the required method, 
response type, and body or query parameter, and also field validation. As the API Gateway has 
performed the required security checks on the requests, this microservice does not put any form 
of authentication in place.

In contrast with API Gateway the service layer in this application acts as the middleman between 
the controller layer and the repository layer. It calls the necessary repository methods to 
perform the request and parses the response into the appropriate object for the controller layer.
The repository layer in this architecture is an interface for performing database operations. The 
statements for these operations are defined and executed in this layer.

## API:
| Method | Path  | Request                   | Response    | Description         |
|--------|-------|---------------------------|-------------|---------------------|
| POST   | /     | Body: UserRegisterRequest | Status code | Register a new user |
| POST   | /auth | Body: UserAuthRequest     | Status code | Authenticate a user |
| GET    | /     | Query param: email         | UserDTO     | Get a user          |
| DELETE | /     | Body: UserDeleteRequest   | Status code | Delete a user       |

## Deployment

Using Docker:
```bash
docker build -t cnat-user-service .
docker run --name some-cnat-user-service -dp 80:80 \
  -e CNAT_USER_SERVICE_POSTGRES_URI=your_postgres_uri \
  -e CNAT_USER_SERVICE_POSTGRES_USERNAME=your_postgres_username \
  -e CNAT_USER_SERVICE_POSTGRES_PASSWORD=your_postgres_password \
  cnat-user-service
```

Using Maven:
```bash
mvn clean package
java -jar \
  -DCNAT_USER_SERVICE_POSTGRES_URI=your_postgres_uri \
  -DCNAT_USER_SERVICE_POSTGRES_USERNAME=your_postgres_username \
  -DCNAT_USER_SERVICE_POSTGRES_PASSWORD=your_postgres_password \
  target/cnat-user-service-0.0.1-SNAPSHOT.jar
```
