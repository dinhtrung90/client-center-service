package com.vts.clientcenter.service;

import com.google.zxing.WriterException;
import com.vts.clientcenter.service.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.Eligibility}.
 */
public interface EligibilityService {

    /**
     * Save a eligibility.
     *
     * @param eligibilityDTO the entity to save.
     * @return the persisted entity.
     */
    EligibilityDTO save(EligibilityDTO eligibilityDTO);

    /**
     * Get all the eligibilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EligibilityDTO> findAll(Pageable pageable);


    /**
     * Get the "id" eligibility.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EligibilityDTO> findOne(String id);

    /**
     * Delete the "id" eligibility.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    EligibilityDTO createEligibility(EligibilityCreationRequest dto) throws IOException, WriterException;

    EligibilityDetailDto findByPrimaryId(String id);

    EligibilityPresentStatusDTO receivedPresentCheck(ReceivedPresentDto dto);
}
