package ChasAcademy.LibraryAPI.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.*;
import tools.jackson.databind.ObjectMapper;


import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {


    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                RedisSerializer.json()
                        )
                );
    }
}