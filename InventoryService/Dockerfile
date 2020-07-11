FROM openjdk:8-jdk-alpine

MAINTAINER Rahulkumar Rakhonde

ARG JAR_FILE=target/inventory_service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} inventory_service.jar
ENTRYPOINT ["java","-jar", "inventory_service.jar"]

