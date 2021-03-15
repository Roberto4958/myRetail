# MyRetail

## Prerequisites
- Java 8
- IntelliJ
- Git
- Docker
- Linux Operating System

## Setup
1. Clone Repo
    - ``git clone https://github.com/Roberto4958/myRetail.git``
2. Open terminal and download mongo database image and then run container
    - ``docker pull mongo:latest``
    - ``docker run -d -p 27017:27017 --name my-retail-database mongo:latest``
3. Open project on IntelliJ
4. Run project on IntelliJ
5. Done!


## Run Web Server
 - Run the navigate to the file src/main/java/myRetail/productpricing/ProductPricingApplication.js
 - Right click file and then click "Run"
 - Done! Navigate to the URL below to see the swagger documentation
    - http://localhost:8080/my-retail/products/swagger-ui.html#/
 
## Run tests
- On the intellij command line run the following command
  - ``mvn clean test``
