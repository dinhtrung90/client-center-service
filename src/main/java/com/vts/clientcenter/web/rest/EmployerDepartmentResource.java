package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EmployerDepartmentService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.EmployerDepartmentDTO;
import com.vts.clientcenter.service.dto.EmployerDepartmentCriteria;
import com.vts.clientcenter.service.EmployerDepartmentQueryService;

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
 * REST controller for managing {@link com.vts.clientcenter.domain.EmployerDepartment}.
 */
@RestController
@RequestMapping("/api")
public class EmployerDepartmentResource {

    private final Logger log = LoggerFactory.getLogger(EmployerDepartmentResource.class);

    private final EmployerDepartmentService employerDepartmentService;

    private final EmployerDepartmentQueryService employerDepartmentQueryService;

    public EmployerDepartmentResource(EmployerDepartmentService employerDepartmentService, EmployerDepartmentQueryService employerDepartmentQueryService) {
        this.employerDepartmentService = employerDepartmentService;
        this.employerDepartmentQueryService = employerDepartmentQueryService;
    }

    /**
     * {@code GET  /employer-departments} : get all the employerDepartments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employerDepartments in body.
     */
    @GetMapping("/employer-departments")
    public ResponseEntity<List<EmployerDepartmentDTO>> getAllEmployerDepartments(EmployerDepartmentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmployerDepartments by criteria: {}", criteria);
        Page<EmployerDepartmentDTO> page = employerDepartmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employer-departments/count} : count all the employerDepartments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employer-departments/count")
    public ResponseEntity<Long> countEmployerDepartments(EmployerDepartmentCriteria criteria) {
        log.debug("REST request to count EmployerDepartments by criteria: {}", criteria);
        return ResponseEntity.ok().body(employerDepartmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employer-departments/:id} : get the "id" employerDepartment.
     *
     * @param id the id of the employerDepartmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employerDepartmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employer-departments/{id}")
    public ResponseEntity<EmployerDepartmentDTO> getEmployerDepartment(@PathVariable Long id) {
        log.debug("REST request to get EmployerDepartment : {}", id);
        Optional<EmployerDepartmentDTO> employerDepartmentDTO = employerDepartmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employerDepartmentDTO);
    }
}
