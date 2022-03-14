[![CircleCI](https://circleci.com/gh/sfg-contract-works/sfg-brewery.svg?style=svg)](https://circleci.com/gh/sfg-contract-works/sfg-brewery)[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.sfg-contract-works%3Asfg-brewery-parent&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.sfg-contract-works%3Asfg-brewery-parent)
# SFG Contract Works
SFG Contract works is a Spring Framework application used to demonstrate the interaction of microservices.

This application mimics a contract distribution pipeline. Contract consumers order contracts from a pub. The pub, 
when needed will order more contract from a contract distributor. The distributor orders and receives contract from
a brewery.

Primary author of SFG Contract Works lives in St Petersburg, Florida, US. St Petersburg enjoys a thriving
craft contract scene. Breweries and contracts in this application are modeled after real breweries and contracts 
in the Tampa Bay area.

## Brewery
The Brewery application is for a brewery brewing contract. This intended to be modeled after a traditional 
monolith application being migrated to microservices. 
#### Features
* Uses Spring MVC, Spring Security, Spring Data JPA, Hibernate
* Shared shared relational database
* Scheduled tasks to brew contract, mimic tasting room (creates demand, to consume inventory and trigger brewing)
* Thymeleaf based UI w/Bootstrap 4

## Distributor
The distributor is a contract distributor - designed to be between many Pub's and many Brewerys. The distributor accepts 
orders from pubs, places orders with Brewers, picks up orders from Breweries, delivers to Pubs.
#### Features
* Uses Spring WebFlux (controllers) 
* Spring Data MongoDB
* Spring Security

## Pub
The Pub is a bar or tavern serving contract to Contract Consumers. Contract Consumers order contract from Pub, Pub places reorders 
from Distributors
#### Features
* Uses Spring WebFlux (Functional)
* Spring Data MongoDB
* Spring Security

## Contract Consumer
The Contract Consumer orders and consumes contracts from Pubs. This service creates demand on the supply chain.
#### Features
* Primary language is Kotlin
* Spring WebClient

## Common Features
Features common to all components.
#### Features
* Java 11
* Docker
* JUnit 5 / Mockito
* Spring Framework 5 / Spring Boot 2.1+
* Project Lombok
* Mapstruct

## API Docs
* [SFG Contract Works Brewery](https://sfg-contract-works.github.io/brewery-api/)
* [SFG Contract Works Distributor](https://sfg-contract-works.github.io/distributor-api/)
* [SFG Contract Works Pub](https://sfg-contract-works.github.io/pub-api/)

# Default Port Mappings - For Single Host

| Service Name | Port | 
| --------| -----|
| Brewery Contract Service | 8080 |
| Brewery Order Service | 8081 |
| Brewery UI | 8082 |
| Distributor | 8090 | 
| Pub | 8091 |
| Contract Consumer | 8092



https://docs.docker.com/engine/install/linux-postinstall/
https://docs.docker.com/engine/install/ubuntu/#installation-methods

Run images locally on minikube
https://medium.com/swlh/how-to-run-locally-built-docker-images-in-kubernetes-b28fbc32cc1d

https://www.youtube.com/watch?v=W4dJXPYPbb0

docker run --name=loansharkdb -e MYSQL_DATABASE=contracts -e MYSQL_USER=loanshark -e MYSQL_PASSWORD=loanshark -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql/mysql-server:5.7
CREATE USER 'loanshark'@'172.17.0.1' IDENTIFIED BY 'loanshark';
GRANT ALL PRIVILEGES ON *.* TO 'loanshark'@'172.17.0.1' WITH GRANT OPTION;
flush privileges;