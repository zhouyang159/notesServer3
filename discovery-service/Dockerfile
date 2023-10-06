FROM openjdk:8-jdk-alpine

COPY target/discovery-service-1.2.jar discovery-service-1.2.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","/discovery-service-1.2.jar"]
