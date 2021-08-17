# Pull in the  Docker image with OracleJDK 8
FROM anapsix/alpine-java:8

MAINTAINER nicola.masi@eng.it

# We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application creates working directories for
# Tomcat by default. The effect is to create a temporary file on your host under "/var/lib/docker" and link it to the
# container under "/tmp". This step is optional for the simple app that we wrote here, but can be necessary for other
# Spring Boot applications if they need to actually write in the filesystem.
VOLUME /tmp

# The project JAR file is ADDed to the container as "app.jar"
RUN mkdir /maven
COPY target/application-catalogue-0.0.2-SNAPSHOT.jar /maven
RUN mkdir /conf
COPY src/main/resources/application.yml /conf



ENTRYPOINT ["java", "-Dspring.config.location=/conf/", "-Dspring.profiles.active=dev",  "-jar","/maven/application-catalogue-0.0.2-SNAPSHOT.jar"]
