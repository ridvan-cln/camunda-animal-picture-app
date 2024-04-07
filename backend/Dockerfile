FROM openjdk:21-jdk-slim

# install maven
RUN apt-get update && \
    apt-get install -y maven

# copy pom.xml and source code to the container
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# package our application code
RUN mvn clean package -DskipTests

# run app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# if you want to start the app using an application.properties file located
#ENTRYPOINT ["java","-jar","/app.jar", "--spring.config.location=file:/application.properties"]

# if you've already overriden configuration settings via environment variables
ENTRYPOINT ["java","-jar","/app.jar"]