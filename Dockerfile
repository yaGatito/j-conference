FROM adoptopenjdk/openjdk11:alpine-jre
MAINTAINER Mykyta Lazovskyi
COPY target/jConference.jar jConference.jar
ENTRYPOINT ["java","-jar","jConference.jar"]