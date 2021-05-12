package com.vts.clientcenter.kafka; //package com.vts.notification.kafka;

import com.vts.clientcenter.config.KafkaProperties;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class KafkaProducerConfig {
    private final Logger log = LoggerFactory.getLogger(KafkaProducerConfig.class);

    @Value("${app.kafka-brokers}")
    private String bootstrapServers;

    public KafkaProducerConfig() {}

    @Bean
    ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<String, Object>(producerFactory());
        kafkaTemplate.setProducerListener(
            new ProducerListener<String, Object>() {

                @Override
                public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
                    log.info("ACK from ProducerListener message: {} offset:  {}", producerRecord.value(), recordMetadata.offset());
                }

                @Override
                public void onError(ProducerRecord<String, Object> producerRecord, Exception exception) {
                    log.debug("error bean Kafkatemplate: {}", exception.getMessage());
                }
            }
        );
        return kafkaTemplate;
    }
}
