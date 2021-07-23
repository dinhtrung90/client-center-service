package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.service.EligibilityPresentStatusService;
import com.vts.clientcenter.domain.EligibilityPresentStatus;
import com.vts.clientcenter.repository.EligibilityPresentStatusRepository;
import com.vts.clientcenter.service.dto.EligibilityPresentStatusDTO;
import com.vts.clientcenter.service.mapper.EligibilityPresentStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EligibilityPresentStatus}.
 */
@Service
@Transactional
public class EligibilityPresentStatusServiceImpl implements EligibilityPresentStatusService {

    private final Logger log = LoggerFactory.getLogger(EligibilityPresentStatusServiceImpl.class);

    private final EligibilityPresentStatusRepository eligibilityPresentStatusRepository;

    private final EligibilityPresentStatusMapper eligibilityPresentStatusMapper;

    public EligibilityPresentStatusServiceImpl(EligibilityPresentStatusRepository eligibilityPresentStatusRepository, EligibilityPresentStatusMapper eligibilityPresentStatusMapper) {
        this.eligibilityPresentStatusRepository = eligibilityPresentStatusRepository;
        this.eligibilityPresentStatusMapper = eligibilityPresentStatusMapper;
    }

    @Override
    public EligibilityPresentStatusDTO save(EligibilityPresentStatusDTO eligibilityPresentStatusDTO) {
        log.debug("Request to save EligibilityPresentStatus : {}", eligibilityPresentStatusDTO);
        EligibilityPresentStatus eligibilityPresentStatus = eligibilityPresentStatusMapper.toEntity(eligibilityPresentStatusDTO);
        eligibilityPresentStatus = eligibilityPresentStatusRepository.save(eligibilityPresentStatus);
        return eligibilityPresentStatusMapper.toDto(eligibilityPresentStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EligibilityPresentStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EligibilityPresentStatuses");
        return eligibilityPresentStatusRepository.findAll(pageable)
            .map(eligibilityPresentStatusMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EligibilityPresentStatusDTO> findOne(Long id) {
        log.debug("Request to get EligibilityPresentStatus : {}", id);
        return eligibilityPresentStatusRepository.findById(id)
            .map(eligibilityPresentStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EligibilityPresentStatus : {}", id);
        eligibilityPresentStatusRepository.deleteById(id);
    }
}
