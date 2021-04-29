package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.Employer;
import com.vts.clientcenter.repository.EmployerRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.EmployerExtensionService;
import com.vts.clientcenter.service.dto.EmployerDTO;
import com.vts.clientcenter.service.mapper.EmployerMapper;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employer}.
 */
@Service
@Transactional
public class EmployerExtensionServiceImpl implements EmployerExtensionService {
    private final Logger log = LoggerFactory.getLogger(EmployerExtensionServiceImpl.class);

    private final EmployerRepository employerRepository;

    private final EmployerMapper employerMapper;

    public EmployerExtensionServiceImpl(EmployerRepository employerRepository, EmployerMapper employerMapper) {
        this.employerRepository = employerRepository;
        this.employerMapper = employerMapper;
    }

    /**
     * Save a employer.
     *
     * @param employerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EmployerDTO save(EmployerDTO employerDTO) {
        log.debug("Request to save Employer : {}", employerDTO);

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        employerDTO.setEmployerKey(UUID.randomUUID().toString());
        employerDTO.setCreatedBy(currentUserLogin.orElse("system"));
        employerDTO.setLastModifiedBy(currentUserLogin.orElse("system"));
        employerDTO.setCreatedDate(Instant.now());
        employerDTO.setLastModifiedDate(Instant.now());
        employerDTO.setLatitude(employerDTO.getLatitude() != null ? employerDTO.getLatitude() : "0");
        employerDTO.setLongitude(employerDTO.getLongitude() != null ? employerDTO.getLongitude() : "0");
        employerDTO.setAddress(employerDTO.getStreet() + ", " + employerDTO.getCity() + ", " + employerDTO.getCounty());
        employerDTO.setCounty(employerDTO.getCounty() != null ? employerDTO.getCounty() : Constants.MY_COUNTRY);
        Employer employer = employerMapper.toEntity(employerDTO);
        employer = employerRepository.save(employer);
        return employerMapper.toDto(employer);
    }
}
