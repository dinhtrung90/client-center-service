package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.service.EligibilityMetadataService;
import com.vts.clientcenter.domain.EligibilityMetadata;
import com.vts.clientcenter.repository.EligibilityMetadataRepository;
import com.vts.clientcenter.service.dto.EligibilityMetadataDTO;
import com.vts.clientcenter.service.mapper.EligibilityMetadataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EligibilityMetadata}.
 */
@Service
@Transactional
public class EligibilityMetadataServiceImpl implements EligibilityMetadataService {

    private final Logger log = LoggerFactory.getLogger(EligibilityMetadataServiceImpl.class);

    private final EligibilityMetadataRepository eligibilityMetadataRepository;

    private final EligibilityMetadataMapper eligibilityMetadataMapper;

    public EligibilityMetadataServiceImpl(EligibilityMetadataRepository eligibilityMetadataRepository, EligibilityMetadataMapper eligibilityMetadataMapper) {
        this.eligibilityMetadataRepository = eligibilityMetadataRepository;
        this.eligibilityMetadataMapper = eligibilityMetadataMapper;
    }

    @Override
    public EligibilityMetadataDTO save(EligibilityMetadataDTO eligibilityMetadataDTO) {
        log.debug("Request to save EligibilityMetadata : {}", eligibilityMetadataDTO);
        EligibilityMetadata eligibilityMetadata = eligibilityMetadataMapper.toEntity(eligibilityMetadataDTO);
        eligibilityMetadata = eligibilityMetadataRepository.save(eligibilityMetadata);
        return eligibilityMetadataMapper.toDto(eligibilityMetadata);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EligibilityMetadataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EligibilityMetadata");
        return eligibilityMetadataRepository.findAll(pageable)
            .map(eligibilityMetadataMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EligibilityMetadataDTO> findOne(Long id) {
        log.debug("Request to get EligibilityMetadata : {}", id);
        return eligibilityMetadataRepository.findById(id)
            .map(eligibilityMetadataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EligibilityMetadata : {}", id);
        eligibilityMetadataRepository.deleteById(id);
    }
}
