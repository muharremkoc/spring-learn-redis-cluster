package com.example.springlearnrediscluster.config;

import io.lettuce.core.ReadFrom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;

@Configuration
@EnableCaching
public class RedisClusterConfig {

    @Value("${spring.data.redis.cluster.nodes}")
    private String clusterNodes;

    @Bean
    LettuceConnectionFactory redisConnectionFactory(RedisClusterConfiguration redisConfiguration) {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .commandTimeout(Duration.ofSeconds(120))
                .build();
        return new LettuceConnectionFactory(redisConfiguration, clientConfig);
    }


    private List<String> getClusterNodes() {
        String[] hostAndPorts = StringUtils.commaDelimitedListToStringArray(clusterNodes);
        List<String> clusterNodes = new ArrayList<>();
        for (String hostAndPort : hostAndPorts) {
            int lastScIndex = hostAndPort.lastIndexOf(":");
            if (lastScIndex == -1) continue;

            try {
                String host = hostAndPort.substring(0, lastScIndex);
                Integer port = Integer.parseInt(hostAndPort.substring(lastScIndex+1));
                clusterNodes.add(String.format("%s:%s",host,port));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return clusterNodes;
    }
    @Bean
    RedisClusterConfiguration redisConfiguration() {
        List<String> clusterNodes = Arrays.asList("redis1:6379", "redis2:6379", "redis3:6379", "redis4:6379", "redis5:6379", "redis6:6379");
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(getClusterNodes());
        redisClusterConfiguration.setMaxRedirects(5);
        return redisClusterConfiguration;
    }

    @Bean
    public RedisTemplate<String, Object> template(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        System.out.println("Redis Cluster template bean");
        return template;
    }

}