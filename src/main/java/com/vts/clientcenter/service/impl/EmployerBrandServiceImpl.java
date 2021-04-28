package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.service.EmployerBrandService;
import com.vts.clientcenter.domain.EmployerBrand;
import com.vts.clientcenter.repository.EmployerBrandRepository;
import com.vts.clientcenter.service.dto.EmployerBrandDTO;
import com.vts.clientcenter.service.mapper.EmployerBrandMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EmployerBrand}.
 */
@Service
@Transactional
public class EmployerBrandServiceImpl implements EmployerBrandService {

    private final Logger log = LoggerFactory.getLogger(EmployerBrandServiceImpl.class);

    private final EmployerBrandRepository employerBrandRepository;

    private final EmployerBrandMapper employerBrandMapper;

    public EmployerBrandServiceImpl(EmployerBrandRepository employerBrandRepository, EmployerBrandMapper employerBrandMapper) {
        this.employerBrandRepository = employerBrandRepository;
        this.employerBrandMapper = employerBrandMapper;
    }

    @Override
    public EmployerBrandDTO save(EmployerBrandDTO employerBrandDTO) {
        log.debug("Request to save EmployerBrand : {}", employerBrandDTO);
        EmployerBrand employerBrand = employerBrandMapper.toEntity(employerBrandDTO);
        employerBrand = employerBrandRepository.save(employerBrand);
        return employerBrandMapper.toDto(employerBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployerBrandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployerBrands");
        return employerBrandRepository.findAll(pageable)
            .map(employerBrandMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EmployerBrandDTO> findOne(Long id) {
        log.debug("Request to get EmployerBrand : {}", id);
        return employerBrandRepository.findById(id)
            .map(employerBrandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployerBrand : {}", id);
        employerBrandRepository.deleteById(id);
    }
}
