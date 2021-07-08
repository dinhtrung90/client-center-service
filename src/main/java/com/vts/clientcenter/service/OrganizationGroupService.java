package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.OrganizationGroupDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.OrganizationGroup}.
 */
public interface OrganizationGroupService {

    /**
     * Save a organizationGroup.
     *
     * @param organizationGroupDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationGroupDTO save(OrganizationGroupDTO organizationGroupDTO);

    /**
     * Get all the organizationGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganizationGroupDTO> findAll(Pageable pageable);


    /**
     * Get the "id" organizationGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationGroupDTO> findOne(Long id);

    /**
     * Delete the "id" organizationGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
