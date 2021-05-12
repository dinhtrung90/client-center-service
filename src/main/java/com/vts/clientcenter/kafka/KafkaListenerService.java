package com.vts.clientcenter.kafka;

import com.vts.clientcenter.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class KafkaListenerService {
    private final Logger log = LoggerFactory.getLogger(KafkaListenerService.class);
    //    @KafkaListener(
    //        topics = "users-created-messages",
    //        groupId = "client-center-consumers",
    //        containerFactory = "appKafkaListenerContainerFactory")
    //    void processCreatedAccount(UserDTO message) {
    //        log.debug("process created account: {}", message);
    //
    //    }
    //
    //    @KafkaListener(
    //        topics = ERROR_EMPLOYEE_PROCESS,
    //        groupId = DATA_PROGRESS_GROUP,
    //        containerFactory = "employeeKafkaListenerContainerFactory")
    //    void handleErrorEmployee(String messageError) {
    //        log.debug("process create account: {}", messageError);
    //    }

}
