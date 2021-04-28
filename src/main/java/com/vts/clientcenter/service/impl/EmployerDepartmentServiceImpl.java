package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.service.EmployerDepartmentService;
import com.vts.clientcenter.domain.EmployerDepartment;
import com.vts.clientcenter.repository.EmployerDepartmentRepository;
import com.vts.clientcenter.service.dto.EmployerDepartmentDTO;
import com.vts.clientcenter.service.mapper.EmployerDepartmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EmployerDepartment}.
 */
@Service
@Transactional
public class EmployerDepartmentServiceImpl implements EmployerDepartmentService {

    private final Logger log = LoggerFactory.getLogger(EmployerDepartmentServiceImpl.class);

    private final EmployerDepartmentRepository employerDepartmentRepository;

    private final EmployerDepartmentMapper employerDepartmentMapper;

    public EmployerDepartmentServiceImpl(EmployerDepartmentRepository employerDepartmentRepository, EmployerDepartmentMapper employerDepartmentMapper) {
        this.employerDepartmentRepository = employerDepartmentRepository;
        this.employerDepartmentMapper = employerDepartmentMapper;
    }

    @Override
    public EmployerDepartmentDTO save(EmployerDepartmentDTO employerDepartmentDTO) {
        log.debug("Request to save EmployerDepartment : {}", employerDepartmentDTO);
        EmployerDepartment employerDepartment = employerDepartmentMapper.toEntity(employerDepartmentDTO);
        employerDepartment = employerDepartmentRepository.save(employerDepartment);
        return employerDepartmentMapper.toDto(employerDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployerDepartmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployerDepartments");
        return employerDepartmentRepository.findAll(pageable)
            .map(employerDepartmentMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EmployerDepartmentDTO> findOne(Long id) {
        log.debug("Request to get EmployerDepartment : {}", id);
        return employerDepartmentRepository.findById(id)
            .map(employerDepartmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployerDepartment : {}", id);
        employerDepartmentRepository.deleteById(id);
    }
}
