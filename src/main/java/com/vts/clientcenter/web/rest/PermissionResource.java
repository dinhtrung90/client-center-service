package com.vts.clientcenter.web.rest;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import com.vts.clientcenter.service.PermissionQueryService;
import com.vts.clientcenter.service.PermissionService;
import com.vts.clientcenter.service.dto.PermissionCriteria;
import com.vts.clientcenter.service.dto.PermissionDTO;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Permission}.
 */
@RestController
@RequestMapping("/api")
public class PermissionResource {
    private final Logger log = LoggerFactory.getLogger(PermissionResource.class);

    private final PermissionService permissionService;

    private final PermissionQueryService permissionQueryService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PermissionResource(PermissionService permissionService, PermissionQueryService permissionQueryService) {
        this.permissionService = permissionService;
        this.permissionQueryService = permissionQueryService;
    }

    /**
     * {@code GET  /permissions} : get all the permissions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissions in body.
     */
    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionDTO>> getAllPermissions(PermissionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Permissions by criteria: {}", criteria);
        Page<PermissionDTO> page = permissionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permissions/count} : count all the permissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/permissions/count")
    public ResponseEntity<Long> countPermissions(PermissionCriteria criteria) {
        log.debug("REST request to count Permissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(permissionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /permissions/:id} : get the "id" permission.
     *
     * @param id the id of the permissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permissions/{id}")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable Long id) {
        log.debug("REST request to get Permission : {}", id);
        Optional<PermissionDTO> permissionDTO = permissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionDTO);
    }

    @PostMapping("/permissions")
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO dto) throws URISyntaxException {
        log.debug("Sending message to topic : {}", dto);
        PermissionDTO result = permissionService.save(dto);
        return ResponseEntity
            .created(new URI("/api/permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
