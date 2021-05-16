package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.RolePermissionQueryService;
import com.vts.clientcenter.service.RolePermissionService;
import com.vts.clientcenter.service.dto.RolePermissionCriteria;
import com.vts.clientcenter.service.dto.RolePermissionDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.RolePermission}.
 */
@RestController
@RequestMapping("/api")
public class RolePermissionResource {
    private final Logger log = LoggerFactory.getLogger(RolePermissionResource.class);

    private final RolePermissionService rolePermissionService;

    private final RolePermissionQueryService rolePermissionQueryService;

    public RolePermissionResource(RolePermissionService rolePermissionService, RolePermissionQueryService rolePermissionQueryService) {
        this.rolePermissionService = rolePermissionService;
        this.rolePermissionQueryService = rolePermissionQueryService;
    }

    /**
     * {@code GET  /role-permissions} : get all the rolePermissions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rolePermissions in body.
     */
    @GetMapping("/role-permissions")
    public ResponseEntity<List<RolePermissionDTO>> getAllRolePermissions(RolePermissionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RolePermissions by criteria: {}", criteria);
        Page<RolePermissionDTO> page = rolePermissionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /role-permissions/count} : count all the rolePermissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/role-permissions/count")
    public ResponseEntity<Long> countRolePermissions(RolePermissionCriteria criteria) {
        log.debug("REST request to count RolePermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(rolePermissionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /role-permissions/:id} : get the "id" rolePermission.
     *
     * @param id the id of the rolePermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rolePermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/role-permissions/{id}")
    public ResponseEntity<RolePermissionDTO> getRolePermission(@PathVariable Long id) {
        log.debug("REST request to get RolePermission : {}", id);
        Optional<RolePermissionDTO> rolePermissionDTO = rolePermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rolePermissionDTO);
    }
}
