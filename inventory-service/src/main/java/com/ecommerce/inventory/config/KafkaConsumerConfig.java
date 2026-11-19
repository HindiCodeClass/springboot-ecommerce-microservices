
package com.ecommerce.inventory.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;



@Configuration
public class KafkaConsumerConfig {
@Bean
DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
	DeadLetterPublishingRecoverer recoverer =  new DeadLetterPublishingRecoverer(kafkaTemplate, (record, ex) -> new TopicPartition(record.topic()+".DTL", record.partition()));
	ExponentialBackOff backoff =  new ExponentialBackOff();
	backoff.setInitialInterval(1000L);
	backoff.setMultiplier(2.0);
	backoff.setMaxInterval(10000L);
	backoff.setMaxAttempts(5);
	DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backoff);
	return errorHandler;
}
}
