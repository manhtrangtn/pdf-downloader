FROM openjdk:11
MAINTAINER trangdm
WORKDIR /
COPY target/*.jar pdf-downloader.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/pdf-downloader.jar"]