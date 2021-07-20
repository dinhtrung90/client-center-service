package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.config.OrganizationConfig;
import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.events.OrganizationCreatedEvent;
import com.vts.clientcenter.repository.ClientAppRepository;
import com.vts.clientcenter.repository.UserRepository;
import com.vts.clientcenter.service.OrganizationService;
import com.vts.clientcenter.repository.OrganizationRepository;
import com.vts.clientcenter.service.dto.*;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.AuthorityMapper;
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
import org.springframework.scheduling.annotation.Async;
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

    private final UserRepository userRepository;

    private final AuthorityMapper authorityMapper;

    @Autowired
    private KeycloakConfig setting;

    @Autowired
    @Qualifier("keycloakFacade")
    private KeycloakFacade keycloakFacade;

    @Autowired
    private OrganizationConfig organizationConfig;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper, OrganizationBrandMapper organizationBrandMapper, OrganizationGroupMapper organizationGroupMapper, ClientAppRepository clientAppRepository, UserRepository userRepository, AuthorityMapper authorityMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.organizationBrandMapper = organizationBrandMapper;
        this.organizationGroupMapper = organizationGroupMapper;
        this.clientAppRepository = clientAppRepository;
        this.userRepository = userRepository;
        this.authorityMapper = authorityMapper;
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
        organization.setEnabled(true); //default enabled = true

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

        organization.setEmail(request.getEmail());

        organization.setPhone(request.getPhone());

        organization.setEnabled(request.getIsEnabled());

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
        List<AuthorityDto> authorities = getAuthoritiesOfClientApp(organization);

        return OrganizationFullResponse.builder()
            .displayName(result.getDisplayName())
            .name(result.getName())
            .email(result.getEmail())
            .phone(result.getPhone())
            .isEnabled(result.isEnabled())
            .roles(authorities)
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

        // bulk delete
        userRepository.deleteBulkByOrganizationIdentifier(organization.getId());

        organizationRepository.deleteByIdentifier(organization.getId());

        removeOrganizationFromKeycloak(organization.getId(), organization.getName());
    }

    @Override
    public Optional<OrganizationFullResponse> findByUUID(String uuid) {

        if (Objects.isNull(uuid)) {
            throw new BadRequestAlertException("Id can not be null.", "Organization", Constants.ID_NOT_NULL);
        }

        Organization organization = organizationRepository.getByUUID(uuid);

        List<AuthorityDto> authorities = getAuthoritiesOfClientApp(organization);
        return Optional.of(
         OrganizationFullResponse.builder()
             .displayName(organization.getDisplayName())
             .description(organization.getDescription())
             .name(organization.getName())
            .brands(organizationBrandMapper.toDto(new ArrayList<>(organization.getOrganizationBrands())))
            .groups(organizationGroupMapper.toDto(new ArrayList<>(organization.getOrganizationGroups())))
             .roles(authorities)
            .build()
        );
    }

    private List<AuthorityDto> getAuthoritiesOfClientApp(Organization organization) {
        Optional<ClientApp> clientAppOptional = clientAppRepository.findById(organization.getId());
        List<AuthorityDto> authorities = new ArrayList<>();
        if (clientAppOptional.isPresent()) {
            Set<Authority> clientAuthorities = clientAppOptional.get().getAuthorities();
            authorities = authorityMapper.authorityToDtos(new ArrayList<>(clientAuthorities));
        }
        return authorities;
    }

    @Override
    @Async
    public void removeOrganizationFromKeycloak(String clientUUID, String clientName) {
        keycloakFacade.removeOrganizationFromKeycloak(setting.getRealmApp(), clientUUID, clientName);
    }
}
