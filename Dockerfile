FROM openjdk:8-jre-alpine
#RUN mvn package
COPY target/strage-spring-0.0.1-SNAPSHOT.jar /strage-server.jar
# Inform Docker that the container is listening on the specified port at runtime.
EXPOSE 8888
ENTRYPOINT ["java","-jar","strage-server.jar"]