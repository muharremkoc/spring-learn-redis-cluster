# spring-learn-redis-cluster


This Project's Goal, Redis-Cluster used with Spring-Boot

## Installation

 - First,Run docker-compose.yaml Without Application

    -  docker-compose up -d redis1 redis2 redis3 redis4 redis5 redis6
    
    ![image](https://user-images.githubusercontent.com/80245013/225623055-8b542467-97c1-4444-a51c-4b830b7ceb58.png)


 - Second,Find ip addresses of redis containers
    
   - docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container_name_or_id
   
   ![image](https://user-images.githubusercontent.com/80245013/225623322-426c91b4-0608-4435-9b9b-fcfb46b9ee0c.png)


- Third,Enter the container with exec command,

    - docker exec -it redis1 bash

 - Fourth,Create Redis Cluster

    -   redis-cli --cluster create "redis1 IP Address":6379 "redis2 IP Address":6379 "redis3 IP Address":6379 "redis4 IP Address":6379 "redis5 IP Address":6379 "redis6 IP Address":6379 --cluster-replicas 1

  - Fifth,define Redis's dependencies in pom.xml
```java
  <!--Cache-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
            <version>${redis.version}</version> 3.0.0
        </dependency>

        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>${jedis.version}</version> 3.9.0
        </dependency>
```

 - Sixth,open application.properties and configure redis settings
```java
spring.data.redis.cluster.nodes = redis1:6379,redis2:6379,redis3:6379, redis4:6379, redis5:6379, redis6:6379
spring.data.redis.cluster.max-redirects = 3
```

- Seventh,Open RedisClusterConfig and edit the settings here according to application.properties
(Note:getClusterNodes() method is Optional)


![image](https://user-images.githubusercontent.com/80245013/225623720-4e026f00-474b-42fa-9fef-8f54f815adc7.png)



- Eighth,open Dockerfile and configure Owner settings,

![image](https://user-images.githubusercontent.com/80245013/225623526-b93e422c-cd10-4326-9eec-b3f36e405dbe.png)



  - Added application build in docker-compose.yaml and run docker-compose up -d --build
  
  
![image](https://user-images.githubusercontent.com/80245013/225623583-93076049-36a5-440b-ba56-47161105a9b7.png)


[Muharrem Koç](https://github.com/muharremkoc)
