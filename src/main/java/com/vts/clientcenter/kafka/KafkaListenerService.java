package com.vts.clientcenter.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListenerService {
    private static final String ERROR_EMPLOYEE_PROCESS = "ERROR_EMPLOYEE_PROCESS";

    private static final String EMPLOYEE = "EMPLOYEE";

    private static final String DATA_PROGRESS_GROUP = "client-center-service";

    @KafkaListener(topics = EMPLOYEE, groupId = DATA_PROGRESS_GROUP, containerFactory = "employeeKafkaListenerContainerFactory")
    void processCreateAccountForEmployee(String message) {
        log.debug("process create account: {}", message);
    }

    @KafkaListener(
        topics = ERROR_EMPLOYEE_PROCESS,
        groupId = DATA_PROGRESS_GROUP,
        containerFactory = "employeeKafkaListenerContainerFactory"
    )
    void handleErrorEmployee(String messageError) {
        log.debug("process create account: {}", messageError);
    }
}
