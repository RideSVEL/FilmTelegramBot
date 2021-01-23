FROM gradle:jdk15-hotspot AS task
WORKDIR /usr/src/app/
COPY src /usr/src/app/src
COPY build.gradle /usr/src/app
COPY settings.gradle /usr/src/app
RUN gradle build

FROM openjdk:15
WORKDIR /usr/src/app/
COPY --from=task /usr/src/app/build/libs/Bot-1.0.jar ROOT.jar
EXPOSE 5000
CMD ["java", "-jar", "ROOT.jar"]
