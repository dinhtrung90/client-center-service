package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EligibilityService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.EligibilityDTO;
import com.vts.clientcenter.service.dto.EligibilityCriteria;
import com.vts.clientcenter.service.EligibilityQueryService;

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
 * REST controller for managing {@link com.vts.clientcenter.domain.Eligibility}.
 */
@RestController
@RequestMapping("/api")
public class EligibilityResource {

    private final Logger log = LoggerFactory.getLogger(EligibilityResource.class);

    private final EligibilityService eligibilityService;

    private final EligibilityQueryService eligibilityQueryService;

    public EligibilityResource(EligibilityService eligibilityService, EligibilityQueryService eligibilityQueryService) {
        this.eligibilityService = eligibilityService;
        this.eligibilityQueryService = eligibilityQueryService;
    }

    /**
     * {@code GET  /eligibilities} : get all the eligibilities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eligibilities in body.
     */
    @GetMapping("/eligibilities")
    public ResponseEntity<List<EligibilityDTO>> getAllEligibilities(EligibilityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Eligibilities by criteria: {}", criteria);
        Page<EligibilityDTO> page = eligibilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eligibilities/count} : count all the eligibilities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/eligibilities/count")
    public ResponseEntity<Long> countEligibilities(EligibilityCriteria criteria) {
        log.debug("REST request to count Eligibilities by criteria: {}", criteria);
        return ResponseEntity.ok().body(eligibilityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /eligibilities/:id} : get the "id" eligibility.
     *
     * @param id the id of the eligibilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eligibilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eligibilities/{id}")
    public ResponseEntity<EligibilityDTO> getEligibility(@PathVariable Long id) {
        log.debug("REST request to get Eligibility : {}", id);
        Optional<EligibilityDTO> eligibilityDTO = eligibilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eligibilityDTO);
    }
}
