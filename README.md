# tripshare-api-spring (/projects)
spring implementation of tripshare api projects endpoints

Quick spring api to handle CRUD operations of projects activities.  The overall project was created as a learning opportunity to get into REACT programming and once ready will be fully published to github.

This microservice is one of many with others in Python (Flask), NodeJS, etc handle things like image uploads, profile management and Token Authorization with Azure B2C being the token issuer and authentication implementation.

This project deploys to Azure App Services via GitHub Actions whenever a pull request is merged to main.

This may also be run locally by running the SpringBoot App directly.

The following run time environment attributes are expected:

JWT_VERIFY_AUTH - base 64 encoded <username>:<password> used to do external JWT JWKS verification of Azure Delivered Token.

JWT_VERIFY_ENDPOINT - endpoint hosting https://github.com/richard-a-mpls/spring-jwk-token-validator which will verify and return the user identity.

SPRING_DATA_MONGODB_URI - URL for mongo instance.  Run locally in test and Mongo Atlas for production (Free).