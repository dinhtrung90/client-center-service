package com.vts.clientcenter.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class KafkaListenerService {

    private final Logger log = LoggerFactory.getLogger(KafkaListenerService.class);

    private static final String ERROR_EMPLOYEE_PROCESS = "ERROR_EMPLOYEE_PROCESS";

    private static final String EMPLOYEE = "EMPLOYEE";

    private static final String DATA_PROGRESS_GROUP = "tvsales-message-groups";


//
//    @KafkaListener(
//        topics = EMPLOYEE,
//        groupId = DATA_PROGRESS_GROUP,
//        containerFactory = "employeeKafkaListenerContainerFactory")
//    void processCreateAccountForEmployee(EmployeeDTO message) {
//        log.debug("process create account: {}", message);
//        employeeService.createSignUpWithMessage(message);
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
