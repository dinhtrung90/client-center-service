package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EmployerBrandService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.EmployerBrandDTO;
import com.vts.clientcenter.service.dto.EmployerBrandCriteria;
import com.vts.clientcenter.service.EmployerBrandQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.EmployerBrand}.
 */
@RestController
@RequestMapping("/api")
public class EmployerBrandResource {

    private final Logger log = LoggerFactory.getLogger(EmployerBrandResource.class);

    private final EmployerBrandService employerBrandService;

    private final EmployerBrandQueryService employerBrandQueryService;

    public EmployerBrandResource(EmployerBrandService employerBrandService, EmployerBrandQueryService employerBrandQueryService) {
        this.employerBrandService = employerBrandService;
        this.employerBrandQueryService = employerBrandQueryService;
    }

    /**
     * {@code GET  /employer-brands} : get all the employerBrands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employerBrands in body.
     */
    @GetMapping("/employer-brands")
    public ResponseEntity<List<EmployerBrandDTO>> getAllEmployerBrands(EmployerBrandCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmployerBrands by criteria: {}", criteria);
        Page<EmployerBrandDTO> page = employerBrandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employer-brands/count} : count all the employerBrands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employer-brands/count")
    public ResponseEntity<Long> countEmployerBrands(EmployerBrandCriteria criteria) {
        log.debug("REST request to count EmployerBrands by criteria: {}", criteria);
        return ResponseEntity.ok().body(employerBrandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employer-brands/:id} : get the "id" employerBrand.
     *
     * @param id the id of the employerBrandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employerBrandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employer-brands/{id}")
    public ResponseEntity<EmployerBrandDTO> getEmployerBrand(@PathVariable Long id) {
        log.debug("REST request to get EmployerBrand : {}", id);
        Optional<EmployerBrandDTO> employerBrandDTO = employerBrandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employerBrandDTO);
    }
}
