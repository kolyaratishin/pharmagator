FROM openjdk:17-alpine

COPY api/build/libs/api-0.0.1-SNAPSHOT.jar pharmagator.jar

CMD ["java", "-jar", "pharmagator.jar"]
