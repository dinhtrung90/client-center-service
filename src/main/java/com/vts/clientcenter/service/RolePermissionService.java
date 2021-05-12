package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.RolePermissionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.RolePermission}.
 */
public interface RolePermissionService {
    /**
     * Save a rolePermission.
     *
     * @param rolePermissionDTO the entity to save.
     * @return the persisted entity.
     */
    RolePermissionDTO save(RolePermissionDTO rolePermissionDTO);

    /**
     * Get all the rolePermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RolePermissionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rolePermission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RolePermissionDTO> findOne(Long id);

    /**
     * Delete the "id" rolePermission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
