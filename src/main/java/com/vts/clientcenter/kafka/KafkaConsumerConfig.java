package com.vts.clientcenter.kafka;

import com.vts.clientcenter.config.KafkaProperties;
import liquibase.pro.packaged.T;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConsumerConfig {

    private static final String CLIENT_CENTER_CONSUME_GROUP_ID = "CLIENT_CENTER_CONSUME_GROUP_ID";
    private final KafkaProperties kafkaProperties;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, T> employeeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, T> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(employeeConsumerFactory());
        return factory;
    }

    public ConsumerFactory<String, T> employeeConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CLIENT_CENTER_CONSUME_GROUP_ID);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>());
    }
}
