package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class EmailResource {
    private final Logger log = LoggerFactory.getLogger(EmailResource.class);

    @Autowired
    private MailService mailService;

    @PostMapping("/send-create-email/")
    public ResponseEntity<String> sending() {
        log.debug("REST request to send to email");
        User user = new User();
        user.setEmail("trung@mailinator.com");
        user.setLogin("trung");
        user.setLangKey("en");
        mailService.sendCreationEmail(user);
        return ResponseEntity.ok("sending");
    }

}
