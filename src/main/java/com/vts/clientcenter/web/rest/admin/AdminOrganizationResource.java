package com.vts.clientcenter.web.rest.admin;

import com.vts.clientcenter.service.OrganizationQueryService;
import com.vts.clientcenter.service.OrganizationService;
import com.vts.clientcenter.service.dto.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Organization}.
 */
@RestController
@RequestMapping("/api/cms")
@PreAuthorize("denyAll()")
public class AdminOrganizationResource {

    private final Logger log = LoggerFactory.getLogger(AdminOrganizationResource.class);

    private final OrganizationService organizationService;

    private final OrganizationQueryService organizationQueryService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AdminOrganizationResource(OrganizationService organizationService, OrganizationQueryService organizationQueryService) {
        this.organizationService = organizationService;
        this.organizationQueryService = organizationQueryService;
    }

    /**
     * {@code GET  /organizations} : get all the organizations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizations in body.
     */
    @GetMapping("/organization")
    @PreAuthorize("hasPermission('Organization', 'Read')")
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations(OrganizationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Organizations by criteria: {}", criteria);
        Page<OrganizationDTO> page = organizationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organizations/:id} : get the "id" organization.
     *
     * @param id the id of the organizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization/{id}")
    @PreAuthorize("hasPermission('Organization', 'Read')")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable String id) {
        log.debug("REST request to get Organization : {}", id);
        Optional<OrganizationDTO> organizationDTO = organizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationDTO);
    }

    @PostMapping("/organization/create")
    @PreAuthorize("hasPermission('Organization', 'Create')")
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationDTO request) throws URISyntaxException {
        log.debug("REST request to create CreateOrganizationRequest : {}", request);
        OrganizationDTO res = organizationService.saveByRequest(request);
        return ResponseEntity
            .created(new URI("/api/cms/organization/" + res.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
            .body(res);
    }

    @PutMapping("/organization/update")
    @PreAuthorize("hasPermission('Organization', 'Update')")
    public ResponseEntity<OrganizationFullResponse> updateOrganization(@Valid @RequestBody OrganizationUpdateRequest request) {
        log.debug("REST request to create CreateOrganizationRequest : {}", request);
        OrganizationFullResponse res = organizationService.updateByRequest(request);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/organization/delete/{uuid}")
    @PreAuthorize("hasPermission('Organization', 'Delete')")
    public ResponseEntity<Void> deleteOrganizationById(@PathVariable String uuid) {
        log.debug("REST request to delete Org : {}", uuid);
        organizationService.deleteByUUID(uuid);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, uuid)).build();
    }


    @GetMapping("/organization/{uuid}")
    @PreAuthorize("hasPermission('Organization', 'Read')")
    public ResponseEntity<OrganizationFullResponse> getFullOrganization(@PathVariable String uuid) {
        log.debug("REST request to get Organization : {}", uuid);
        Optional<OrganizationFullResponse> organizationDTO = organizationService.findByUUID(uuid);
        return ResponseUtil.wrapOrNotFound(organizationDTO);
    }

}
