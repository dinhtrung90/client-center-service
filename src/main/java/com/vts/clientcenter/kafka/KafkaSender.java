package com.vts.clientcenter.kafka;

import com.vts.clientcenter.config.KafkaProperties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaSender<T> {

    private final Logger log = LoggerFactory.getLogger(KafkaSender.class);

    private KafkaTemplate<String, T> kafkaTemplate;

    @PostConstruct
    void init() {
        kafkaTemplate = defaultKafkaTemplate();
    }

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public void sendCustomMessage(T payload, String topicName) {
        log.info("Sending Json Serializer : {}", payload);
        log.info("--------------------------------");
        kafkaTemplate.send(topicName, payload);
    }

    public void sendMessageWithCallback(T message, String topicName) {
        log.info("Sending : {}", message);
        log.info("---------------------------------");

        ListenableFuture<SendResult<String, T>> future = kafkaTemplate.send(topicName, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.warn("Failure Callback: Unable to deliver message [{}]. {}", message, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                log.info("Success Callback: [{}] delivered with offset -{}", message,
                    result.getRecordMetadata().offset());
            }
        });
    }


    public KafkaTemplate<String, T> defaultKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private ProducerFactory<String,T> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerObjectConfigs());
    }

    private Map<String, Object> producerObjectConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootStrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

}
