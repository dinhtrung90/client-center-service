package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.OrganizationGroupService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.OrganizationGroupDTO;
import com.vts.clientcenter.service.dto.OrganizationGroupCriteria;
import com.vts.clientcenter.service.OrganizationGroupQueryService;

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
 * REST controller for managing {@link com.vts.clientcenter.domain.OrganizationGroup}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationGroupResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationGroupResource.class);

    private final OrganizationGroupService organizationGroupService;

    private final OrganizationGroupQueryService organizationGroupQueryService;

    public OrganizationGroupResource(OrganizationGroupService organizationGroupService, OrganizationGroupQueryService organizationGroupQueryService) {
        this.organizationGroupService = organizationGroupService;
        this.organizationGroupQueryService = organizationGroupQueryService;
    }

    /**
     * {@code GET  /organization-groups} : get all the organizationGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationGroups in body.
     */
    @GetMapping("/organization-groups")
    public ResponseEntity<List<OrganizationGroupDTO>> getAllOrganizationGroups(OrganizationGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrganizationGroups by criteria: {}", criteria);
        Page<OrganizationGroupDTO> page = organizationGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organization-groups/count} : count all the organizationGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/organization-groups/count")
    public ResponseEntity<Long> countOrganizationGroups(OrganizationGroupCriteria criteria) {
        log.debug("REST request to count OrganizationGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organization-groups/:id} : get the "id" organizationGroup.
     *
     * @param id the id of the organizationGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-groups/{id}")
    public ResponseEntity<OrganizationGroupDTO> getOrganizationGroup(@PathVariable Long id) {
        log.debug("REST request to get OrganizationGroup : {}", id);
        Optional<OrganizationGroupDTO> organizationGroupDTO = organizationGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationGroupDTO);
    }
}
