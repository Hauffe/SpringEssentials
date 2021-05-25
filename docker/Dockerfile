FROM openjdk:16-jdk-alpine
MAINTAINER Alexandre Hauffe <alexandre.hauffe@avenuecode.com>

COPY target/*.jar springEssentials-docker.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/springEssentials-docker.jar"]