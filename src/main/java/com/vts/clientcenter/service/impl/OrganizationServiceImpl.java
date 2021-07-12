package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.config.OrganizationConfig;
import com.vts.clientcenter.domain.ClientApp;
import com.vts.clientcenter.domain.OrganizationBrand;
import com.vts.clientcenter.domain.OrganizationGroup;
import com.vts.clientcenter.events.OrganizationCreatedEvent;
import com.vts.clientcenter.repository.ClientAppRepository;
import com.vts.clientcenter.service.OrganizationService;
import com.vts.clientcenter.domain.Organization;
import com.vts.clientcenter.repository.OrganizationRepository;
import com.vts.clientcenter.service.dto.OrganizationBrandDTO;
import com.vts.clientcenter.service.dto.OrganizationDTO;
import com.vts.clientcenter.service.dto.OrganizationUpdateRequest;
import com.vts.clientcenter.service.dto.OrganizationFullResponse;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.OrganizationBrandMapper;
import com.vts.clientcenter.service.mapper.OrganizationGroupMapper;
import com.vts.clientcenter.service.mapper.OrganizationMapper;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.vts.clientcenter.config.Constants.BACKGROUND_COLOR;
import static com.vts.clientcenter.config.Constants.PRIMARY_COLOR;

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

    private final ClientAppRepository clientAppRepository;

    @Autowired
    private KeycloakConfig setting;

    @Autowired
    @Qualifier("keycloakFacade")
    private KeycloakFacade keycloakFacade;

    @Autowired
    private OrganizationConfig organizationConfig;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper, OrganizationBrandMapper organizationBrandMapper, OrganizationGroupMapper organizationGroupMapper, ClientAppRepository clientAppRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.organizationBrandMapper = organizationBrandMapper;
        this.organizationGroupMapper = organizationGroupMapper;
        this.clientAppRepository = clientAppRepository;
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
    public Optional<OrganizationDTO> findOne(String id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id)
            .map(organizationMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }

    @Override
    public OrganizationDTO saveByRequest(OrganizationDTO request) {

        log.debug("Request to create Organization with new request : {}", request);

        validateCreateOrg(request.getId(), request.getName());

        Organization organization = organizationMapper.toEntity(request);

        //create keycloak
        ClientApp clientApp = keycloakFacade.createClientWithConfig(setting.getRealmApp(), organization.getName(), organizationConfig);
        if (Objects.isNull(clientApp)){
            throw new BadRequestAlertException("Can not Create client.", "KClient", request.getName());
        }
        organization.setId(clientApp.getId());

        log.info("Create organization in local database : {}", organization);

        applicationEventPublisher.publishEvent(OrganizationCreatedEvent.builder().organization(organization).build());

        // create default brand
        OrganizationBrand defaultBrand =  new OrganizationBrand();
        defaultBrand.setOrganization(organization);
        defaultBrand.setBackgroundColor(BACKGROUND_COLOR);
        defaultBrand.setLogoUrl(null);
        defaultBrand.setPrimary(true);
        defaultBrand.setPrimaryColor(PRIMARY_COLOR);
        defaultBrand.setSubDomain(organization.getName());
        Set<OrganizationBrand> organizationBrands = new HashSet<>();
        organizationBrands.add(defaultBrand);
        organization.setOrganizationBrands(organizationBrands);
        organization = organizationRepository.save(organization);

        //save client app
        clientAppRepository.save(clientApp);

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
    public OrganizationFullResponse updateByRequest(OrganizationUpdateRequest request) {

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

        return OrganizationFullResponse.builder()
            .brands(organizationBrandMapper.toDto(new ArrayList<>(result.getOrganizationBrands())))
            .groups(organizationGroupMapper.toDto(new ArrayList<>(result.getOrganizationGroups())))
            .build();
    }

    private Organization getValidatedOrganization(String uuid) {
        if (Objects.isNull(uuid)) {
            throw new BadRequestAlertException("Id can not be null.", "Organization", Constants.ID_NOT_NULL);
        }

        Optional<Organization> organizationOptional = organizationRepository.findById(uuid);

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
    public Optional<OrganizationFullResponse> findByUUID(String uuid) {

        if (Objects.isNull(uuid)) {
            throw new BadRequestAlertException("Id can not be null.", "Organization", Constants.ID_NOT_NULL);
        }

        Organization organization = organizationRepository.getByUUID(uuid);

        return Optional.of(
         OrganizationFullResponse.builder()
             .displayName(organization.getDisplayName())
             .description(organization.getDescription())
             .name(organization.getName())
            .brands(organizationBrandMapper.toDto(new ArrayList<>(organization.getOrganizationBrands())))
            .groups(organizationGroupMapper.toDto(new ArrayList<>(organization.getOrganizationGroups())))
            .build()
        );
    }

    @Override
    public void removeOrganizationFromKeycloak(String clientUUID, String clientName) {
        keycloakFacade.removeOrganizationFromKeycloak(setting.getRealmApp(), clientUUID, clientName);
    }
}
