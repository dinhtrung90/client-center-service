package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.EligibilityMetadataDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.EligibilityMetadata}.
 */
public interface EligibilityMetadataService {

    /**
     * Save a eligibilityMetadata.
     *
     * @param eligibilityMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    EligibilityMetadataDTO save(EligibilityMetadataDTO eligibilityMetadataDTO);

    /**
     * Get all the eligibilityMetadata.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EligibilityMetadataDTO> findAll(Pageable pageable);


    /**
     * Get the "id" eligibilityMetadata.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EligibilityMetadataDTO> findOne(Long id);

    /**
     * Delete the "id" eligibilityMetadata.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
