package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EmployerExtensionService;
import com.vts.clientcenter.service.EmployerQueryService;
import com.vts.clientcenter.service.EmployerService;
import com.vts.clientcenter.service.dto.EmployerCriteria;
import com.vts.clientcenter.service.dto.EmployerDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Employer}.
 */
@Controller
@RequestMapping("/api")
public class EmployerExtensionResource {
    private final Logger log = LoggerFactory.getLogger(EmployerExtensionResource.class);

    private final EmployerExtensionService employerService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String ENTITY_NAME = "employers";

    public EmployerExtensionResource(EmployerExtensionService employerService) {
        this.employerService = employerService;
    }

    @RequestMapping(value = "/employers/create", method = RequestMethod.POST)
    public ResponseEntity<EmployerDTO> createEmployer(@Valid @RequestBody EmployerDTO dto) throws URISyntaxException {
        log.debug("REST request to get Employer : {}", dto);
        EmployerDTO employerDTO = employerService.save(dto);
        return ResponseEntity.ok(employerDTO);
    }
}
