package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.EmployerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.Employer}.
 */
public interface EmployerService {

    /**
     * Save a employer.
     *
     * @param employerDTO the entity to save.
     * @return the persisted entity.
     */
    EmployerDTO save(EmployerDTO employerDTO);

    /**
     * Get all the employers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployerDTO> findAll(Pageable pageable);


    /**
     * Get the "id" employer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployerDTO> findOne(Long id);

    /**
     * Delete the "id" employer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
