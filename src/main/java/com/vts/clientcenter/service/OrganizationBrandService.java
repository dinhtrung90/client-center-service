package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.OrganizationBrandDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.OrganizationBrand}.
 */
public interface OrganizationBrandService {

    /**
     * Save a organizationBrand.
     *
     * @param organizationBrandDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationBrandDTO save(OrganizationBrandDTO organizationBrandDTO);

    /**
     * Get all the organizationBrands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganizationBrandDTO> findAll(Pageable pageable);


    /**
     * Get the "id" organizationBrand.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationBrandDTO> findOne(Long id);

    /**
     * Delete the "id" organizationBrand.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
