package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EmployerBrandExtensionService;
import com.vts.clientcenter.service.EmployerBrandService;
import com.vts.clientcenter.service.EmployerExtensionService;
import com.vts.clientcenter.service.dto.EmployerBrandDTO;
import com.vts.clientcenter.service.dto.EmployerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Employer}.
 */
@Controller
@RequestMapping("/api")
public class EmployerBrandExtensionResource {
    private final Logger log = LoggerFactory.getLogger(EmployerBrandExtensionResource.class);

    @Autowired
    private  EmployerBrandExtensionService employerService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String ENTITY_NAME = "Employer-Brand";

    public EmployerBrandExtensionResource() {

    }

    @RequestMapping(value = "/brand/create", method = RequestMethod.POST)
    public ResponseEntity<EmployerBrandDTO> createBrand(@Valid @RequestBody EmployerBrandDTO dto){
        log.debug("REST request to get Employer : {}", dto);
        EmployerBrandDTO employerDTO = employerService.save(dto);
        return ResponseEntity.ok(employerDTO);
    }
}
