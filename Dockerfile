FROM adoptopenjdk/openjdk11:alpine-jre
ADD build/libs/notificationService-0.0.1-SNAPSHOT-plain.jar notificationApp.jar
RUN mkdir /app
COPY build/libs/*.jar /app/notificationApp.jar
ENTRYPOINT ["java","-jar","/app/notificationApp.jar"]
EXPOSE 8091