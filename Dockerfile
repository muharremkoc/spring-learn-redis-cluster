FROM openjdk:17
EXPOSE 8082
COPY target/*.jar redis-cluster.jar
ENTRYPOINT ["java","-jar","/redis-cluster.jar"]