package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.EligibilityMetadataService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.EligibilityMetadataDTO;
import com.vts.clientcenter.service.dto.EligibilityMetadataCriteria;
import com.vts.clientcenter.service.EligibilityMetadataQueryService;

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
 * REST controller for managing {@link com.vts.clientcenter.domain.EligibilityMetadata}.
 */
@RestController
@RequestMapping("/api")
public class EligibilityMetadataResource {

    private final Logger log = LoggerFactory.getLogger(EligibilityMetadataResource.class);

    private final EligibilityMetadataService eligibilityMetadataService;

    private final EligibilityMetadataQueryService eligibilityMetadataQueryService;

    public EligibilityMetadataResource(EligibilityMetadataService eligibilityMetadataService, EligibilityMetadataQueryService eligibilityMetadataQueryService) {
        this.eligibilityMetadataService = eligibilityMetadataService;
        this.eligibilityMetadataQueryService = eligibilityMetadataQueryService;
    }

    /**
     * {@code GET  /eligibility-metadata} : get all the eligibilityMetadata.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eligibilityMetadata in body.
     */
    @GetMapping("/eligibility-metadata")
    public ResponseEntity<List<EligibilityMetadataDTO>> getAllEligibilityMetadata(EligibilityMetadataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EligibilityMetadata by criteria: {}", criteria);
        Page<EligibilityMetadataDTO> page = eligibilityMetadataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eligibility-metadata/count} : count all the eligibilityMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/eligibility-metadata/count")
    public ResponseEntity<Long> countEligibilityMetadata(EligibilityMetadataCriteria criteria) {
        log.debug("REST request to count EligibilityMetadata by criteria: {}", criteria);
        return ResponseEntity.ok().body(eligibilityMetadataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /eligibility-metadata/:id} : get the "id" eligibilityMetadata.
     *
     * @param id the id of the eligibilityMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eligibilityMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eligibility-metadata/{id}")
    public ResponseEntity<EligibilityMetadataDTO> getEligibilityMetadata(@PathVariable Long id) {
        log.debug("REST request to get EligibilityMetadata : {}", id);
        Optional<EligibilityMetadataDTO> eligibilityMetadataDTO = eligibilityMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eligibilityMetadataDTO);
    }
}
