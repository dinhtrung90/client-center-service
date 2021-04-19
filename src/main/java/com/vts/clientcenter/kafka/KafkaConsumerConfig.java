package com.vts.clientcenter.kafka;

import static com.vts.clientcenter.config.Constants.DATA_PROCESSING_GROUP_ID;

import com.vts.clientcenter.config.KafkaProperties;
import com.vts.clientcenter.service.dto.EmployeeDTO;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {
    private final KafkaProperties kafkaProperties;

    @Autowired
    private KafkaTemplate<String, String> kafkatemplate;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmployeeDTO> employeeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmployeeDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(employeeConsumerFactory());
        return factory;
    }

    public ConsumerFactory<String, EmployeeDTO> employeeConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, DATA_PROCESSING_GROUP_ID);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(EmployeeDTO.class));
    }
}
