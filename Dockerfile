FROM eclipse-temurin:17-jdk-alpine
COPY ./build/libs/*SNAPSHOT.war project.war
ENTRYPOINT ["java", "-jar", "project.war"]