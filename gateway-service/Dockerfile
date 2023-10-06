FROM openjdk:8-jdk-alpine

COPY target/gateway-service-1.2.jar gateway-service-1.2.jar

EXPOSE 80

ENTRYPOINT ["java","-jar","/gateway-service-1.2.jar"]
