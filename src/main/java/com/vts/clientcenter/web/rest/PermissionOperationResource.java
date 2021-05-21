package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.PermissionOperationQueryService;
import com.vts.clientcenter.service.PermissionOperationService;
import com.vts.clientcenter.service.dto.PermissionOperationCriteria;
import com.vts.clientcenter.service.dto.PermissionOperationDTO;
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
 * REST controller for managing {@link com.vts.clientcenter.domain.PermissionOperation}.
 */
@RestController
@RequestMapping("/api")
public class PermissionOperationResource {
    private final Logger log = LoggerFactory.getLogger(PermissionOperationResource.class);

    private final PermissionOperationService permissionOperationService;

    private final PermissionOperationQueryService permissionOperationQueryService;

    public PermissionOperationResource(
        PermissionOperationService permissionOperationService,
        PermissionOperationQueryService permissionOperationQueryService
    ) {
        this.permissionOperationService = permissionOperationService;
        this.permissionOperationQueryService = permissionOperationQueryService;
    }

    /**
     * {@code GET  /permission-operations} : get all the permissionOperations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissionOperations in body.
     */
    @GetMapping("/permission-operations")
    public ResponseEntity<List<PermissionOperationDTO>> getAllPermissionOperations(
        PermissionOperationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PermissionOperations by criteria: {}", criteria);
        Page<PermissionOperationDTO> page = permissionOperationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permission-operations/count} : count all the permissionOperations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/permission-operations/count")
    public ResponseEntity<Long> countPermissionOperations(PermissionOperationCriteria criteria) {
        log.debug("REST request to count PermissionOperations by criteria: {}", criteria);
        return ResponseEntity.ok().body(permissionOperationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /permission-operations/:id} : get the "id" permissionOperation.
     *
     * @param id the id of the permissionOperationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionOperationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permission-operations/{id}")
    public ResponseEntity<PermissionOperationDTO> getPermissionOperation(@PathVariable Long id) {
        log.debug("REST request to get PermissionOperation : {}", id);
        Optional<PermissionOperationDTO> permissionOperationDTO = permissionOperationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionOperationDTO);
    }
}
