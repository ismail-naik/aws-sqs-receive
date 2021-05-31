FROM openjdk:8
ADD target/aws-sqs-reading-0.0.1-SNAPSHOT.jar aws-sqs-reading.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","aws-sqs-reading.jar"]