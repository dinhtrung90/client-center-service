package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.service.OrganizationBrandService;
import com.vts.clientcenter.domain.OrganizationBrand;
import com.vts.clientcenter.repository.OrganizationBrandRepository;
import com.vts.clientcenter.service.dto.OrganizationBrandDTO;
import com.vts.clientcenter.service.mapper.OrganizationBrandMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OrganizationBrand}.
 */
@Service
@Transactional
public class OrganizationBrandServiceImpl implements OrganizationBrandService {

    private final Logger log = LoggerFactory.getLogger(OrganizationBrandServiceImpl.class);

    private final OrganizationBrandRepository organizationBrandRepository;

    private final OrganizationBrandMapper organizationBrandMapper;

    public OrganizationBrandServiceImpl(OrganizationBrandRepository organizationBrandRepository, OrganizationBrandMapper organizationBrandMapper) {
        this.organizationBrandRepository = organizationBrandRepository;
        this.organizationBrandMapper = organizationBrandMapper;
    }

    @Override
    public OrganizationBrandDTO save(OrganizationBrandDTO organizationBrandDTO) {
        log.debug("Request to save OrganizationBrand : {}", organizationBrandDTO);
        OrganizationBrand organizationBrand = organizationBrandMapper.toEntity(organizationBrandDTO);
        organizationBrand = organizationBrandRepository.save(organizationBrand);
        return organizationBrandMapper.toDto(organizationBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationBrandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationBrands");
        return organizationBrandRepository.findAll(pageable)
            .map(organizationBrandMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationBrandDTO> findOne(Long id) {
        log.debug("Request to get OrganizationBrand : {}", id);
        return organizationBrandRepository.findById(id)
            .map(organizationBrandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganizationBrand : {}", id);
        organizationBrandRepository.deleteById(id);
    }
}
