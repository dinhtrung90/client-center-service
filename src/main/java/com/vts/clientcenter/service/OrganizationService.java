package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.UserOrganizationMembership;
import com.vts.clientcenter.service.dto.OrganizationDTO;

import com.vts.clientcenter.service.dto.OrganizationUpdateRequest;
import com.vts.clientcenter.service.dto.OrganizationFullResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.Organization}.
 */
public interface OrganizationService {

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationDTO save(OrganizationDTO organizationDTO);

    /**
     * Get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganizationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationDTO> findOne(String id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    OrganizationDTO saveByRequest(OrganizationDTO request);

    OrganizationFullResponse updateByRequest(OrganizationUpdateRequest request);

    void deleteByUUID(String uuid);

    Optional<OrganizationFullResponse> findByUUID(String uuid);

    void removeOrganizationFromKeycloak(String id, String name);

    UserOrganizationMembership assignUserToOrganization(String userId, String organizationId);

}
