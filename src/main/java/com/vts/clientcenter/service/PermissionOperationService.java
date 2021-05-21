package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.PermissionOperationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.PermissionOperation}.
 */
public interface PermissionOperationService {
    /**
     * Save a permissionOperation.
     *
     * @param permissionOperationDTO the entity to save.
     * @return the persisted entity.
     */
    PermissionOperationDTO save(PermissionOperationDTO permissionOperationDTO);

    /**
     * Get all the permissionOperations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PermissionOperationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" permissionOperation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PermissionOperationDTO> findOne(Long id);

    /**
     * Delete the "id" permissionOperation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
