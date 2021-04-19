package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EmployerService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.EmployerDTO;
import com.vts.clientcenter.service.dto.EmployerCriteria;
import com.vts.clientcenter.service.EmployerQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Employer}.
 */
@RestController
@RequestMapping("/api")
public class EmployerResource {

    private final Logger log = LoggerFactory.getLogger(EmployerResource.class);

    private final EmployerService employerService;

    private final EmployerQueryService employerQueryService;

    public EmployerResource(EmployerService employerService, EmployerQueryService employerQueryService) {
        this.employerService = employerService;
        this.employerQueryService = employerQueryService;
    }

    /**
     * {@code GET  /employers} : get all the employers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employers in body.
     */
    @GetMapping("/employers")
    public ResponseEntity<List<EmployerDTO>> getAllEmployers(EmployerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Employers by criteria: {}", criteria);
        Page<EmployerDTO> page = employerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employers/count} : count all the employers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employers/count")
    public ResponseEntity<Long> countEmployers(EmployerCriteria criteria) {
        log.debug("REST request to count Employers by criteria: {}", criteria);
        return ResponseEntity.ok().body(employerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employers/:id} : get the "id" employer.
     *
     * @param id the id of the employerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employers/{id}")
    public ResponseEntity<EmployerDTO> getEmployer(@PathVariable Long id) {
        log.debug("REST request to get Employer : {}", id);
        Optional<EmployerDTO> employerDTO = employerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employerDTO);
    }
}
