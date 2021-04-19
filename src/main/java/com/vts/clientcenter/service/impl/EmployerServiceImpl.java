package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.service.EmployerService;
import com.vts.clientcenter.domain.Employer;
import com.vts.clientcenter.repository.EmployerRepository;
import com.vts.clientcenter.service.dto.EmployerDTO;
import com.vts.clientcenter.service.mapper.EmployerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Employer}.
 */
@Service
@Transactional
public class EmployerServiceImpl implements EmployerService {

    private final Logger log = LoggerFactory.getLogger(EmployerServiceImpl.class);

    private final EmployerRepository employerRepository;

    private final EmployerMapper employerMapper;

    public EmployerServiceImpl(EmployerRepository employerRepository, EmployerMapper employerMapper) {
        this.employerRepository = employerRepository;
        this.employerMapper = employerMapper;
    }

    @Override
    public EmployerDTO save(EmployerDTO employerDTO) {
        log.debug("Request to save Employer : {}", employerDTO);
        Employer employer = employerMapper.toEntity(employerDTO);
        employer = employerRepository.save(employer);
        return employerMapper.toDto(employer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employers");
        return employerRepository.findAll(pageable)
            .map(employerMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EmployerDTO> findOne(Long id) {
        log.debug("Request to get Employer : {}", id);
        return employerRepository.findById(id)
            .map(employerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employer : {}", id);
        employerRepository.deleteById(id);
    }
}
