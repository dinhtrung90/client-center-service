package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.OrganizationGroup;
import com.vts.clientcenter.domain.Organization;
import com.vts.clientcenter.repository.OrganizationGroupRepository;
import com.vts.clientcenter.service.OrganizationGroupService;
import com.vts.clientcenter.service.dto.OrganizationGroupDTO;
import com.vts.clientcenter.service.mapper.OrganizationGroupMapper;
import com.vts.clientcenter.service.dto.OrganizationGroupCriteria;
import com.vts.clientcenter.service.OrganizationGroupQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OrganizationGroupResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class OrganizationGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private OrganizationGroupRepository organizationGroupRepository;

    @Autowired
    private OrganizationGroupMapper organizationGroupMapper;

    @Autowired
    private OrganizationGroupService organizationGroupService;

    @Autowired
    private OrganizationGroupQueryService organizationGroupQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationGroupMockMvc;

    private OrganizationGroup organizationGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationGroup createEntity(EntityManager em) {
        OrganizationGroup organizationGroup = new OrganizationGroup()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return organizationGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationGroup createUpdatedEntity(EntityManager em) {
        OrganizationGroup organizationGroup = new OrganizationGroup()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return organizationGroup;
    }

    @BeforeEach
    public void initTest() {
        organizationGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroups() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList
        restOrganizationGroupMockMvc.perform(get("/api/organization-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getOrganizationGroup() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get the organizationGroup
        restOrganizationGroupMockMvc.perform(get("/api/organization-groups/{id}", organizationGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getOrganizationGroupsByIdFiltering() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        Long id = organizationGroup.getId();

        defaultOrganizationGroupShouldBeFound("id.equals=" + id);
        defaultOrganizationGroupShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationGroupShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrganizationGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where name equals to DEFAULT_NAME
        defaultOrganizationGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the organizationGroupList where name equals to UPDATED_NAME
        defaultOrganizationGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where name not equals to DEFAULT_NAME
        defaultOrganizationGroupShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the organizationGroupList where name not equals to UPDATED_NAME
        defaultOrganizationGroupShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrganizationGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the organizationGroupList where name equals to UPDATED_NAME
        defaultOrganizationGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where name is not null
        defaultOrganizationGroupShouldBeFound("name.specified=true");

        // Get all the organizationGroupList where name is null
        defaultOrganizationGroupShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where name contains DEFAULT_NAME
        defaultOrganizationGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the organizationGroupList where name contains UPDATED_NAME
        defaultOrganizationGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where name does not contain DEFAULT_NAME
        defaultOrganizationGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the organizationGroupList where name does not contain UPDATED_NAME
        defaultOrganizationGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOrganizationGroupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where description equals to DEFAULT_DESCRIPTION
        defaultOrganizationGroupShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the organizationGroupList where description equals to UPDATED_DESCRIPTION
        defaultOrganizationGroupShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroupsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where description not equals to DEFAULT_DESCRIPTION
        defaultOrganizationGroupShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the organizationGroupList where description not equals to UPDATED_DESCRIPTION
        defaultOrganizationGroupShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrganizationGroupShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the organizationGroupList where description equals to UPDATED_DESCRIPTION
        defaultOrganizationGroupShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where description is not null
        defaultOrganizationGroupShouldBeFound("description.specified=true");

        // Get all the organizationGroupList where description is null
        defaultOrganizationGroupShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationGroupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where description contains DEFAULT_DESCRIPTION
        defaultOrganizationGroupShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the organizationGroupList where description contains UPDATED_DESCRIPTION
        defaultOrganizationGroupShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrganizationGroupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);

        // Get all the organizationGroupList where description does not contain DEFAULT_DESCRIPTION
        defaultOrganizationGroupShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the organizationGroupList where description does not contain UPDATED_DESCRIPTION
        defaultOrganizationGroupShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllOrganizationGroupsByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationGroupRepository.saveAndFlush(organizationGroup);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        organizationGroup.setOrganization(organization);
        organizationGroupRepository.saveAndFlush(organizationGroup);
        String organizationId = organization.getId();

        // Get all the organizationGroupList where organization equals to organizationId
        defaultOrganizationGroupShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the organizationGroupList where organization equals to organizationId + 1
        defaultOrganizationGroupShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationGroupShouldBeFound(String filter) throws Exception {
        restOrganizationGroupMockMvc.perform(get("/api/organization-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restOrganizationGroupMockMvc.perform(get("/api/organization-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationGroupShouldNotBeFound(String filter) throws Exception {
        restOrganizationGroupMockMvc.perform(get("/api/organization-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationGroupMockMvc.perform(get("/api/organization-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationGroup() throws Exception {
        // Get the organizationGroup
        restOrganizationGroupMockMvc.perform(get("/api/organization-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
