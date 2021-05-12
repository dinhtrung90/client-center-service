package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.kafka.KafkaSender;
import com.vts.clientcenter.service.dto.UserDTO;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Employer}.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {
    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private final KafkaSender<UserDTO> producer;

    public MessageResource(KafkaSender<UserDTO> producer) {
        this.producer = producer;
    }

    @PostMapping("/publish/{topicName}")
    public ResponseEntity<Boolean> sendMessageToTopic(@PathVariable String topicName, @Valid @RequestBody UserDTO dto) {
        log.debug("Sending message to topic : {}", topicName);
        producer.sendMessageWithCallback(dto, topicName);
        return ResponseEntity.ok(true);
    }
}
