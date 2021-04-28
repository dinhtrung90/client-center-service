package com.vts.clientcenter.kafka;

import com.vts.clientcenter.service.dto.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
class KafkaSender {
    @Autowired
    private KafkaTemplate<String, EmployeeDTO> employeeKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    void sendMessage(String message, String topicName) {
        log.info("Sending : {}", message);
        log.info("--------------------------------");

        kafkaTemplate.send(topicName, message);
    }

    //    void sendWithRoutingTemplate(String message, String topicName) {
    //        log.info("Sending : {}", message);
    //        log.info("--------------------------------");
    //
    //        routingKafkaTemplate.send(topicName, message.getBytes());
    //    }
    //
    void sendCustomMessage(EmployeeDTO user, String topicName) {
        log.info("Sending Json Serializer : {}", user);
        log.info("--------------------------------");

        employeeKafkaTemplate.send(topicName, user);
    }

    void sendMessageWithCallback(String message, String topicName) {
        log.info("Sending : {}", message);
        log.info("---------------------------------");

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

        future.addCallback(
            new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    log.info("Success Callback: [{}] delivered with offset -{}", message, result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable ex) {
                    log.warn("Failure Callback: Unable to deliver message [{}]. {}", message, ex.getMessage());
                }
            }
        );
    }
}
