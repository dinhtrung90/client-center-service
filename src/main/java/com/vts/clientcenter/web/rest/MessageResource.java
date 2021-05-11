package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EmployerQueryService;
import com.vts.clientcenter.service.EmployerService;
import com.vts.clientcenter.service.dto.EmployerCriteria;
import com.vts.clientcenter.service.dto.EmployerDTO;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Employer}.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {
    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    public MessageResource() {}
}
