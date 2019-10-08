FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/test-service
ADD @project.build.finalName@.jar /usr/local/test/test-service/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh

