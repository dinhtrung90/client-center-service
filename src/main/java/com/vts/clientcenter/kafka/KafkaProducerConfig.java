package com.vts.clientcenter.kafka;

import com.vts.clientcenter.config.KafkaProperties;
import com.vts.clientcenter.service.dto.EmployeeDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@Slf4j
public class KafkaProducerConfig {
    private final KafkaProperties kafkaProperties;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
        kafkaTemplate.setProducerListener(
            new ProducerListener<String, String>() {

                @Override
                public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
                    log.info("ACK from ProducerListener message: {} offset:  {}", producerRecord.value(), recordMetadata.offset());
                }

                @Override
                public void onError(ProducerRecord<String, String> producerRecord, Exception exception) {
                    log.debug("error bean Kafkatemplate: {}", exception.getMessage());
                }
            }
        );
        return kafkaTemplate;
    }

    @Bean
    public ProducerFactory<String, EmployeeDTO> employeeProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerObjectConfigs());
    }

    @Bean
    Map<String, Object> producerObjectConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, EmployeeDTO> employeeKafkaTemplate() {
        return new KafkaTemplate<>(employeeProducerFactory());
    }
}
