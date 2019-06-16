FROM maven:3.6-jdk-8-alpine as build
VOLUME $HOME/.m2:/root/.m2

RUN mkdir --parents /usr/src/app
WORKDIR /usr/src/app

ADD pom.xml /usr/src/app
RUN mvn verify clean --fail-never

ADD . /usr/src/app
RUN mvn clean install

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/app/target/jobcenter-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8200
ENTRYPOINT ["/usr/bin/java", "-jar", "/app/app.jar"]
