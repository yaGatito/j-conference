FROM adoptopenjdk/openjdk11
MAINTAINER Mykyta Lazovskyi
COPY target/jConference.jar jConference.jar
ENTRYPOINT ["java","-jar","jConference.jar"]