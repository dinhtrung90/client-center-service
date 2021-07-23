package com.vts.clientcenter.web.rest.admin;

import com.vts.clientcenter.service.EligibilityService;
import com.vts.clientcenter.service.dto.EligibilityDTO;
import com.vts.clientcenter.service.dto.EligibilityCriteria;
import com.vts.clientcenter.service.EligibilityQueryService;

import com.vts.clientcenter.service.dto.EligibilityDetailDto;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Eligibility}.
 */
@RestController
@RequestMapping("/api/cms")
public class AdminEligibilityResource {

    private final Logger log = LoggerFactory.getLogger(AdminEligibilityResource.class);

    private final EligibilityService eligibilityService;

    private final EligibilityQueryService eligibilityQueryService;

    public AdminEligibilityResource(EligibilityService eligibilityService, EligibilityQueryService eligibilityQueryService) {
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
    @GetMapping("/eligibility")
    public ResponseEntity<List<EligibilityDTO>> getAllEligibility(EligibilityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get eligibility by criteria: {}", criteria);
        Page<EligibilityDTO> page = eligibilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * {@code GET  /eligibilities/:id} : get the "id" eligibility.
     *
     * @param id the id of the eligibilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eligibilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eligibility/{id}")
    public ResponseEntity<EligibilityDetailDto> getEligibility(@PathVariable String id) {
        log.debug("REST request to get Eligibility : {}", id);
        EligibilityDetailDto res = eligibilityService.findByPrimaryId(id);
        return ResponseEntity.ok(res);
    }
}
