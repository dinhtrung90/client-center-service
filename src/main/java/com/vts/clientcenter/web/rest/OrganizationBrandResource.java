package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.OrganizationBrandService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.OrganizationBrandDTO;
import com.vts.clientcenter.service.dto.OrganizationBrandCriteria;
import com.vts.clientcenter.service.OrganizationBrandQueryService;

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
 * REST controller for managing {@link com.vts.clientcenter.domain.OrganizationBrand}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationBrandResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationBrandResource.class);

    private final OrganizationBrandService organizationBrandService;

    private final OrganizationBrandQueryService organizationBrandQueryService;

    public OrganizationBrandResource(OrganizationBrandService organizationBrandService, OrganizationBrandQueryService organizationBrandQueryService) {
        this.organizationBrandService = organizationBrandService;
        this.organizationBrandQueryService = organizationBrandQueryService;
    }

    /**
     * {@code GET  /organization-brands} : get all the organizationBrands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationBrands in body.
     */
    @GetMapping("/organization-brands")
    public ResponseEntity<List<OrganizationBrandDTO>> getAllOrganizationBrands(OrganizationBrandCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrganizationBrands by criteria: {}", criteria);
        Page<OrganizationBrandDTO> page = organizationBrandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organization-brands/count} : count all the organizationBrands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/organization-brands/count")
    public ResponseEntity<Long> countOrganizationBrands(OrganizationBrandCriteria criteria) {
        log.debug("REST request to count OrganizationBrands by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationBrandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organization-brands/:id} : get the "id" organizationBrand.
     *
     * @param id the id of the organizationBrandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationBrandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-brands/{id}")
    public ResponseEntity<OrganizationBrandDTO> getOrganizationBrand(@PathVariable Long id) {
        log.debug("REST request to get OrganizationBrand : {}", id);
        Optional<OrganizationBrandDTO> organizationBrandDTO = organizationBrandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationBrandDTO);
    }
}
