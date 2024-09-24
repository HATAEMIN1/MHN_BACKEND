FROM eclipse-temurin:17-jdk-alpine
COPY ./build/libs/*SNAPSHOT.jar project.jar
COPY upload/default.jpg upload/default.jpg
ENTRYPOINT ["java", "-jar", "project.jar"]
