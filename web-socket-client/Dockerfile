FROM openjdk:17-oracle
EXPOSE 9004
COPY build/libs/*.jar /app/homework.jar
ENTRYPOINT ["java", "-jar","/app/homework.jar"]