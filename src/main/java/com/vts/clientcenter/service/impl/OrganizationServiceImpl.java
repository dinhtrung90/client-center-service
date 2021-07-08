package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.OrganizationBrand;
import com.vts.clientcenter.domain.OrganizationGroup;
import com.vts.clientcenter.service.OrganizationService;
import com.vts.clientcenter.domain.Organization;
import com.vts.clientcenter.repository.OrganizationRepository;
import com.vts.clientcenter.service.dto.OrganizationBrandDTO;
import com.vts.clientcenter.service.dto.OrganizationDTO;
import com.vts.clientcenter.service.dto.OrganizationUpdateRequest;
import com.vts.clientcenter.service.dto.OrganizationUpdateResponse;
import com.vts.clientcenter.service.mapper.OrganizationBrandMapper;
import com.vts.clientcenter.service.mapper.OrganizationGroupMapper;
import com.vts.clientcenter.service.mapper.OrganizationMapper;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Service Implementation for managing {@link Organization}.
 */
@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    private final OrganizationBrandMapper organizationBrandMapper;

    private final OrganizationGroupMapper organizationGroupMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper, OrganizationBrandMapper organizationBrandMapper, OrganizationGroupMapper organizationGroupMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.organizationBrandMapper = organizationBrandMapper;
        this.organizationGroupMapper = organizationGroupMapper;
    }

    @Override
    public OrganizationDTO save(OrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);
        Organization organization = organizationMapper.toEntity(organizationDTO);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll(pageable)
            .map(organizationMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationDTO> findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id)
            .map(organizationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }

    @Override
    public OrganizationDTO saveByRequest(OrganizationDTO request) {

        log.debug("Request to create Organization with new request : {}", request);

        validateCreateOrg(request.getOrganizationUUID(), request.getName());

        Organization organization = organizationRepository.save(organizationMapper.toEntity(request));

        return organizationMapper.toDto(organization);
    }

    private void validateCreateOrg(String id, String name) {
        if (Objects.nonNull(id)) {
            throw new BadRequestAlertException("Can not create organization.", "Organization", Constants.ID_IS_NULL);
        }

        boolean existsByName = organizationRepository.existsByName(name);

        if (existsByName) {
            throw new BadRequestAlertException("Organization Name has existed.", "Organization", Constants.ORGANIZATION_NAME_EXISTED);
        }
    }


    @Override
    public OrganizationUpdateResponse updateByRequest(OrganizationUpdateRequest request) {

        Organization organization = getValidatedOrganization(request.getUuid());

        organization.setDisplayName(request.getDisplayName());

        organization.setDescription(request.getDescription());

        if (!CollectionUtils.isEmpty(request.getBrands())) {

            List<OrganizationBrandDTO> brands = request.getBrands();

            List<OrganizationBrand> organizationBrands = organizationBrandMapper.toEntity(brands);

            organizationBrands.forEach(organization::addOrganizationBrand);

        }

        if (!CollectionUtils.isEmpty(request.getGroups())) {

            List<OrganizationGroup> organizationGroups = organizationGroupMapper.toEntity(request.getGroups());

            organizationGroups.forEach(organization::addOrganizationGroup);
        }

        Organization result = organizationRepository.save(organization);

        return OrganizationUpdateResponse.builder()
            .brands(organizationBrandMapper.toDto(new ArrayList<>(result.getOrganizationBrands())))
            .groups(organizationGroupMapper.toDto(new ArrayList<>(result.getOrganizationGroups())))
            .build();
    }

    private Organization getValidatedOrganization(String uuid) {
        if (Objects.isNull(uuid)) {
            throw new BadRequestAlertException("Id can not be null.", "Organization", Constants.ID_NOT_NULL);
        }

        Optional<Organization> organizationOptional = organizationRepository.findByOrganizationUUID(uuid);

        if (!organizationOptional.isPresent()) {
            throw new BadRequestAlertException("Not found any organization.", "Organization", Constants.ORGANIZATION_NOT_FOUND);
        }

        return organizationOptional.get();
    }

    @Override
    public void deleteByUUID(String uuid) {

        Organization organization = getValidatedOrganization(uuid);

        organizationRepository.delete(organization);
    }

    @Override
    public Optional<OrganizationUpdateResponse> findByUUID(String uuid) {

        if (Objects.isNull(uuid)) {
            throw new BadRequestAlertException("Id can not be null.", "Organization", Constants.ID_NOT_NULL);
        }

        Organization organization = organizationRepository.getByUUID(uuid);

        return Optional.of(
         OrganizationUpdateResponse.builder()
            .brands(organizationBrandMapper.toDto(new ArrayList<>(organization.getOrganizationBrands())))
            .groups(organizationGroupMapper.toDto(new ArrayList<>(organization.getOrganizationGroups())))
            .build()
        );
    }
}
