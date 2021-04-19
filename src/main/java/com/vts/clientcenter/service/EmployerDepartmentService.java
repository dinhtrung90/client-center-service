package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.EmployerDepartmentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.EmployerDepartment}.
 */
public interface EmployerDepartmentService {

    /**
     * Save a employerDepartment.
     *
     * @param employerDepartmentDTO the entity to save.
     * @return the persisted entity.
     */
    EmployerDepartmentDTO save(EmployerDepartmentDTO employerDepartmentDTO);

    /**
     * Get all the employerDepartments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployerDepartmentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" employerDepartment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployerDepartmentDTO> findOne(Long id);

    /**
     * Delete the "id" employerDepartment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
