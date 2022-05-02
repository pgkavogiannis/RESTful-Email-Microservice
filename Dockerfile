FROM openjdk:8-jdk-alpine

ENV APP_HOME=/app
ENV ARTIFACT_NAME=email-microservice.war
ENV DEBUG_PORT=7001

EXPOSE 7000
EXPOSE $DEBUG_PORT

RUN mkdir $APP_HOME

WORKDIR $APP_HOME

ADD ./build/libs/$ARTIFACT_NAME .

ENTRYPOINT java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$DEBUG_PORT $ARTIFACT_NAME