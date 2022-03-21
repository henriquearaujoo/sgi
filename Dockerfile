FROM maven:3.3-jdk-8 AS builder

WORKDIR /usr/local/sgi

COPY . /usr/local/sgi/
RUN mvn install

FROM tomcat:8.5-alpine

RUN apk add openjdk8

COPY --from=builder usr/local/sgi/target/*.war /usr/local/tomcat/webapps/

EXPOSE 8080