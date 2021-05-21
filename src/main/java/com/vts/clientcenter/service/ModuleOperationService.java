package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.ModuleOperationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.ModuleOperation}.
 */
public interface ModuleOperationService {
    /**
     * Save a moduleOperation.
     *
     * @param moduleOperationDTO the entity to save.
     * @return the persisted entity.
     */
    ModuleOperationDTO save(ModuleOperationDTO moduleOperationDTO);

    /**
     * Get all the moduleOperations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ModuleOperationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moduleOperation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ModuleOperationDTO> findOne(Long id);

    /**
     * Delete the "id" moduleOperation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
