package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.service.OrganizationGroupService;
import com.vts.clientcenter.domain.OrganizationGroup;
import com.vts.clientcenter.repository.OrganizationGroupRepository;
import com.vts.clientcenter.service.dto.OrganizationGroupDTO;
import com.vts.clientcenter.service.mapper.OrganizationGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OrganizationGroup}.
 */
@Service
@Transactional
public class OrganizationGroupServiceImpl implements OrganizationGroupService {

    private final Logger log = LoggerFactory.getLogger(OrganizationGroupServiceImpl.class);

    private final OrganizationGroupRepository organizationGroupRepository;

    private final OrganizationGroupMapper organizationGroupMapper;

    public OrganizationGroupServiceImpl(OrganizationGroupRepository organizationGroupRepository, OrganizationGroupMapper organizationGroupMapper) {
        this.organizationGroupRepository = organizationGroupRepository;
        this.organizationGroupMapper = organizationGroupMapper;
    }

    @Override
    public OrganizationGroupDTO save(OrganizationGroupDTO organizationGroupDTO) {
        log.debug("Request to save OrganizationGroup : {}", organizationGroupDTO);
        OrganizationGroup organizationGroup = organizationGroupMapper.toEntity(organizationGroupDTO);
        organizationGroup = organizationGroupRepository.save(organizationGroup);
        return organizationGroupMapper.toDto(organizationGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationGroups");
        return organizationGroupRepository.findAll(pageable)
            .map(organizationGroupMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationGroupDTO> findOne(Long id) {
        log.debug("Request to get OrganizationGroup : {}", id);
        return organizationGroupRepository.findById(id)
            .map(organizationGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganizationGroup : {}", id);
        organizationGroupRepository.deleteById(id);
    }
}
