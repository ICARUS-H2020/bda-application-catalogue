# ICARUS BDA Application Catalogue

## Prerequisites

- Java 8 +
- mvn 3.6.3 +
- postgres 15 +

## Configuration

Edit application.yml:

* url: #jdbc:postgresql://`<postgresHost>`:`<postgresPort>`/`<dbName>`
* username: `<user>`
* password: `<password>`

## Compile

Run `mvn clean install`

## Building Docker image

Run `docker build -t <image_name>:<image_tag> .`