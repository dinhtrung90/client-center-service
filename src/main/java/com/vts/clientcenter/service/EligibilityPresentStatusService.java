package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.EligibilityPresentStatusDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.EligibilityPresentStatus}.
 */
public interface EligibilityPresentStatusService {

    /**
     * Save a eligibilityPresentStatus.
     *
     * @param eligibilityPresentStatusDTO the entity to save.
     * @return the persisted entity.
     */
    EligibilityPresentStatusDTO save(EligibilityPresentStatusDTO eligibilityPresentStatusDTO);

    /**
     * Get all the eligibilityPresentStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EligibilityPresentStatusDTO> findAll(Pageable pageable);


    /**
     * Get the "id" eligibilityPresentStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EligibilityPresentStatusDTO> findOne(Long id);

    /**
     * Delete the "id" eligibilityPresentStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
