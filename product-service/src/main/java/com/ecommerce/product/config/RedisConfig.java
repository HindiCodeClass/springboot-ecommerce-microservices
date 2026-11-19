package com.ecommerce.product.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching
public class RedisConfig {

 private ObjectMapper redisObjectMapper() {
	ObjectMapper mapper = new ObjectMapper();
	mapper.registerModule(new JavaTimeModule());
	mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
	return mapper;
}
@Bean
CacheManager cacheManager(RedisConnectionFactory connectionFectory) {
	ObjectMapper objectMapper = redisObjectMapper();
	GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
	RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30))
			.disableCachingNullValues()
			.prefixCacheNameWith("product-service::")
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
	Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
	cacheConfigurations.put("productCache", defaultConfig.entryTtl(Duration.ofMinutes(20)));
	cacheConfigurations.put("categoryCache", defaultConfig.entryTtl(Duration.ofHours(5)));
return RedisCacheManager.builder(connectionFectory).cacheDefaults(defaultConfig)
		.withInitialCacheConfigurations(cacheConfigurations).transactionAware()
.build();
	}
}
