package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EligibilityPresentStatusService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.EligibilityPresentStatusDTO;
import com.vts.clientcenter.service.dto.EligibilityPresentStatusCriteria;
import com.vts.clientcenter.service.EligibilityPresentStatusQueryService;

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
 * REST controller for managing {@link com.vts.clientcenter.domain.EligibilityPresentStatus}.
 */
@RestController
@RequestMapping("/api")
public class EligibilityPresentStatusResource {

    private final Logger log = LoggerFactory.getLogger(EligibilityPresentStatusResource.class);

    private final EligibilityPresentStatusService eligibilityPresentStatusService;

    private final EligibilityPresentStatusQueryService eligibilityPresentStatusQueryService;

    public EligibilityPresentStatusResource(EligibilityPresentStatusService eligibilityPresentStatusService, EligibilityPresentStatusQueryService eligibilityPresentStatusQueryService) {
        this.eligibilityPresentStatusService = eligibilityPresentStatusService;
        this.eligibilityPresentStatusQueryService = eligibilityPresentStatusQueryService;
    }

    /**
     * {@code GET  /eligibility-present-statuses} : get all the eligibilityPresentStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eligibilityPresentStatuses in body.
     */
    @GetMapping("/eligibility-present-statuses")
    public ResponseEntity<List<EligibilityPresentStatusDTO>> getAllEligibilityPresentStatuses(EligibilityPresentStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EligibilityPresentStatuses by criteria: {}", criteria);
        Page<EligibilityPresentStatusDTO> page = eligibilityPresentStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eligibility-present-statuses/count} : count all the eligibilityPresentStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/eligibility-present-statuses/count")
    public ResponseEntity<Long> countEligibilityPresentStatuses(EligibilityPresentStatusCriteria criteria) {
        log.debug("REST request to count EligibilityPresentStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(eligibilityPresentStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /eligibility-present-statuses/:id} : get the "id" eligibilityPresentStatus.
     *
     * @param id the id of the eligibilityPresentStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eligibilityPresentStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eligibility-present-statuses/{id}")
    public ResponseEntity<EligibilityPresentStatusDTO> getEligibilityPresentStatus(@PathVariable Long id) {
        log.debug("REST request to get EligibilityPresentStatus : {}", id);
        Optional<EligibilityPresentStatusDTO> eligibilityPresentStatusDTO = eligibilityPresentStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eligibilityPresentStatusDTO);
    }
}
