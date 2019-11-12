FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/test-service
ADD test-service-0.0.1-SNAPSHOT.jar /usr/local/test/test-service/app.jar

ENTRYPOINT ["java","-jar", "/usr/local/test/test-service/app.jar"]

