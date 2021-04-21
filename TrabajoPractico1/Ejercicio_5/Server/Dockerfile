FROM openjdk:latest
WORKDIR /usr/src/
COPY Server.jar .
EXPOSE 9090
EXPOSE 6666
ENTRYPOINT ["java", "-jar", "Server.jar"]