package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.ModuleOperationQueryService;
import com.vts.clientcenter.service.ModuleOperationService;
import com.vts.clientcenter.service.dto.ModuleOperationCriteria;
import com.vts.clientcenter.service.dto.ModuleOperationDTO;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
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
 * REST controller for managing {@link com.vts.clientcenter.domain.ModuleOperation}.
 */
@RestController
@RequestMapping("/api")
public class ModuleOperationResource {
    private final Logger log = LoggerFactory.getLogger(ModuleOperationResource.class);

    private final ModuleOperationService moduleOperationService;

    private final ModuleOperationQueryService moduleOperationQueryService;

    public ModuleOperationResource(ModuleOperationService moduleOperationService, ModuleOperationQueryService moduleOperationQueryService) {
        this.moduleOperationService = moduleOperationService;
        this.moduleOperationQueryService = moduleOperationQueryService;
    }

    /**
     * {@code GET  /module-operations} : get all the moduleOperations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moduleOperations in body.
     */
    @GetMapping("/module-operations")
    public ResponseEntity<List<ModuleOperationDTO>> getAllModuleOperations(ModuleOperationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ModuleOperations by criteria: {}", criteria);
        Page<ModuleOperationDTO> page = moduleOperationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /module-operations/count} : count all the moduleOperations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/module-operations/count")
    public ResponseEntity<Long> countModuleOperations(ModuleOperationCriteria criteria) {
        log.debug("REST request to count ModuleOperations by criteria: {}", criteria);
        return ResponseEntity.ok().body(moduleOperationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /module-operations/:id} : get the "id" moduleOperation.
     *
     * @param id the id of the moduleOperationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moduleOperationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/module-operations/{id}")
    public ResponseEntity<ModuleOperationDTO> getModuleOperation(@PathVariable Long id) {
        log.debug("REST request to get ModuleOperation : {}", id);
        Optional<ModuleOperationDTO> moduleOperationDTO = moduleOperationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moduleOperationDTO);
    }
}
