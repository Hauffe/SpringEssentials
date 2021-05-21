FROM openjdk:16-jdk-alpine
MAINTAINER Alexandre Hauffe <alexandre.hauffe@avenuecode.com>

COPY target/*.jar springEssentials-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/springEssentials-0.0.1-SNAPSHOT.jar"]