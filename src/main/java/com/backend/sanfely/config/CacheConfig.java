package com.backend.sanfely.config;

import com.backend.sanfely.catalog.dto.DishResponseDto;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {

        JsonMapper mapper = JsonMapper.builder().build();

        JavaType listType = mapper.getTypeFactory()
            .constructCollectionType(List.class, DishResponseDto.class);

        JacksonJsonRedisSerializer<List<DishResponseDto>> serializer =
            new JacksonJsonRedisSerializer<>(mapper, listType);

        return builder -> builder
            .withCacheConfiguration(
                "dishesByTraiteur",
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(10))
                    .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                            serializer
                        )
                    )
            );
    }
}