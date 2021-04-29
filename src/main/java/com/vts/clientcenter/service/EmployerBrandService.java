package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.EmployerBrandDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.EmployerBrand}.
 */
public interface EmployerBrandService {

    /**
     * Save a employerBrand.
     *
     * @param employerBrandDTO the entity to save.
     * @return the persisted entity.
     */
    EmployerBrandDTO save(EmployerBrandDTO employerBrandDTO);

    /**
     * Get all the employerBrands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployerBrandDTO> findAll(Pageable pageable);


    /**
     * Get the "id" employerBrand.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployerBrandDTO> findOne(Long id);

    /**
     * Delete the "id" employerBrand.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
