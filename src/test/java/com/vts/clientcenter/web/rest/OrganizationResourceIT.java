package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.Organization;
import com.vts.clientcenter.domain.UserAddress;
import com.vts.clientcenter.domain.OrganizationBrand;
import com.vts.clientcenter.domain.OrganizationGroup;
import com.vts.clientcenter.repository.OrganizationRepository;
import com.vts.clientcenter.service.OrganizationService;
import com.vts.clientcenter.service.dto.OrganizationDTO;
import com.vts.clientcenter.service.mapper.OrganizationMapper;
import com.vts.clientcenter.service.dto.OrganizationCriteria;
import com.vts.clientcenter.service.OrganizationQueryService;

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
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class OrganizationResourceIT {

    private static final String DEFAULT_ORGANIZATION_UUID = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationQueryService organizationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization()
            .name(DEFAULT_NAME)
            .displayName(DEFAULT_DISPLAY_NAME)
            .description(DEFAULT_DESCRIPTION)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE);
        return organization;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity(EntityManager em) {
        Organization organization = new Organization()
            .name(UPDATED_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE);
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId())))
            .andExpect(jsonPath("$.[*].organizationUUID").value(hasItem(DEFAULT_ORGANIZATION_UUID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    public void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId()))
            .andExpect(jsonPath("$.organizationUUID").value(DEFAULT_ORGANIZATION_UUID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }


    @Test
    @Transactional
    public void getOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        String id = organization.getId();

        defaultOrganizationShouldBeFound("id.equals=" + id);
        defaultOrganizationShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationUUIDIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationUUID equals to DEFAULT_ORGANIZATION_UUID
        defaultOrganizationShouldBeFound("organizationUUID.equals=" + DEFAULT_ORGANIZATION_UUID);

        // Get all the organizationList where organizationUUID equals to UPDATED_ORGANIZATION_UUID
        defaultOrganizationShouldNotBeFound("organizationUUID.equals=" + UPDATED_ORGANIZATION_UUID);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationUUIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationUUID not equals to DEFAULT_ORGANIZATION_UUID
        defaultOrganizationShouldNotBeFound("organizationUUID.notEquals=" + DEFAULT_ORGANIZATION_UUID);

        // Get all the organizationList where organizationUUID not equals to UPDATED_ORGANIZATION_UUID
        defaultOrganizationShouldBeFound("organizationUUID.notEquals=" + UPDATED_ORGANIZATION_UUID);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationUUIDIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationUUID in DEFAULT_ORGANIZATION_UUID or UPDATED_ORGANIZATION_UUID
        defaultOrganizationShouldBeFound("organizationUUID.in=" + DEFAULT_ORGANIZATION_UUID + "," + UPDATED_ORGANIZATION_UUID);

        // Get all the organizationList where organizationUUID equals to UPDATED_ORGANIZATION_UUID
        defaultOrganizationShouldNotBeFound("organizationUUID.in=" + UPDATED_ORGANIZATION_UUID);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationUUIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationUUID is not null
        defaultOrganizationShouldBeFound("organizationUUID.specified=true");

        // Get all the organizationList where organizationUUID is null
        defaultOrganizationShouldNotBeFound("organizationUUID.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationsByOrganizationUUIDContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationUUID contains DEFAULT_ORGANIZATION_UUID
        defaultOrganizationShouldBeFound("organizationUUID.contains=" + DEFAULT_ORGANIZATION_UUID);

        // Get all the organizationList where organizationUUID contains UPDATED_ORGANIZATION_UUID
        defaultOrganizationShouldNotBeFound("organizationUUID.contains=" + UPDATED_ORGANIZATION_UUID);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationUUIDNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where organizationUUID does not contain DEFAULT_ORGANIZATION_UUID
        defaultOrganizationShouldNotBeFound("organizationUUID.doesNotContain=" + DEFAULT_ORGANIZATION_UUID);

        // Get all the organizationList where organizationUUID does not contain UPDATED_ORGANIZATION_UUID
        defaultOrganizationShouldBeFound("organizationUUID.doesNotContain=" + UPDATED_ORGANIZATION_UUID);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name equals to DEFAULT_NAME
        defaultOrganizationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the organizationList where name equals to UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name not equals to DEFAULT_NAME
        defaultOrganizationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the organizationList where name not equals to UPDATED_NAME
        defaultOrganizationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrganizationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the organizationList where name equals to UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name is not null
        defaultOrganizationShouldBeFound("name.specified=true");

        // Get all the organizationList where name is null
        defaultOrganizationShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationsByNameContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name contains DEFAULT_NAME
        defaultOrganizationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the organizationList where name contains UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name does not contain DEFAULT_NAME
        defaultOrganizationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the organizationList where name does not contain UPDATED_NAME
        defaultOrganizationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByDisplayNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where displayName equals to DEFAULT_DISPLAY_NAME
        defaultOrganizationShouldBeFound("displayName.equals=" + DEFAULT_DISPLAY_NAME);

        // Get all the organizationList where displayName equals to UPDATED_DISPLAY_NAME
        defaultOrganizationShouldNotBeFound("displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByDisplayNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where displayName not equals to DEFAULT_DISPLAY_NAME
        defaultOrganizationShouldNotBeFound("displayName.notEquals=" + DEFAULT_DISPLAY_NAME);

        // Get all the organizationList where displayName not equals to UPDATED_DISPLAY_NAME
        defaultOrganizationShouldBeFound("displayName.notEquals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByDisplayNameIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where displayName in DEFAULT_DISPLAY_NAME or UPDATED_DISPLAY_NAME
        defaultOrganizationShouldBeFound("displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME);

        // Get all the organizationList where displayName equals to UPDATED_DISPLAY_NAME
        defaultOrganizationShouldNotBeFound("displayName.in=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByDisplayNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where displayName is not null
        defaultOrganizationShouldBeFound("displayName.specified=true");

        // Get all the organizationList where displayName is null
        defaultOrganizationShouldNotBeFound("displayName.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationsByDisplayNameContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where displayName contains DEFAULT_DISPLAY_NAME
        defaultOrganizationShouldBeFound("displayName.contains=" + DEFAULT_DISPLAY_NAME);

        // Get all the organizationList where displayName contains UPDATED_DISPLAY_NAME
        defaultOrganizationShouldNotBeFound("displayName.contains=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByDisplayNameNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where displayName does not contain DEFAULT_DISPLAY_NAME
        defaultOrganizationShouldNotBeFound("displayName.doesNotContain=" + DEFAULT_DISPLAY_NAME);

        // Get all the organizationList where displayName does not contain UPDATED_DISPLAY_NAME
        defaultOrganizationShouldBeFound("displayName.doesNotContain=" + UPDATED_DISPLAY_NAME);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description equals to DEFAULT_DESCRIPTION
        defaultOrganizationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the organizationList where description equals to UPDATED_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description not equals to DEFAULT_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the organizationList where description not equals to UPDATED_DESCRIPTION
        defaultOrganizationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrganizationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the organizationList where description equals to UPDATED_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description is not null
        defaultOrganizationShouldBeFound("description.specified=true");

        // Get all the organizationList where description is null
        defaultOrganizationShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description contains DEFAULT_DESCRIPTION
        defaultOrganizationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the organizationList where description contains UPDATED_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description does not contain DEFAULT_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the organizationList where description does not contain UPDATED_DESCRIPTION
        defaultOrganizationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email equals to DEFAULT_EMAIL
        defaultOrganizationShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the organizationList where email equals to UPDATED_EMAIL
        defaultOrganizationShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email not equals to DEFAULT_EMAIL
        defaultOrganizationShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the organizationList where email not equals to UPDATED_EMAIL
        defaultOrganizationShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultOrganizationShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the organizationList where email equals to UPDATED_EMAIL
        defaultOrganizationShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email is not null
        defaultOrganizationShouldBeFound("email.specified=true");

        // Get all the organizationList where email is null
        defaultOrganizationShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationsByEmailContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email contains DEFAULT_EMAIL
        defaultOrganizationShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the organizationList where email contains UPDATED_EMAIL
        defaultOrganizationShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email does not contain DEFAULT_EMAIL
        defaultOrganizationShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the organizationList where email does not contain UPDATED_EMAIL
        defaultOrganizationShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where phone equals to DEFAULT_PHONE
        defaultOrganizationShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the organizationList where phone equals to UPDATED_PHONE
        defaultOrganizationShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where phone not equals to DEFAULT_PHONE
        defaultOrganizationShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the organizationList where phone not equals to UPDATED_PHONE
        defaultOrganizationShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultOrganizationShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the organizationList where phone equals to UPDATED_PHONE
        defaultOrganizationShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where phone is not null
        defaultOrganizationShouldBeFound("phone.specified=true");

        // Get all the organizationList where phone is null
        defaultOrganizationShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where phone contains DEFAULT_PHONE
        defaultOrganizationShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the organizationList where phone contains UPDATED_PHONE
        defaultOrganizationShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where phone does not contain DEFAULT_PHONE
        defaultOrganizationShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the organizationList where phone does not contain UPDATED_PHONE
        defaultOrganizationShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByUserAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        UserAddress userAddress = UserAddressResourceIT.createEntity(em);
        em.persist(userAddress);
        em.flush();
        organization.addUserAddress(userAddress);
        organizationRepository.saveAndFlush(organization);
        Long userAddressId = userAddress.getId();

        // Get all the organizationList where userAddress equals to userAddressId
        defaultOrganizationShouldBeFound("userAddressId.equals=" + userAddressId);

        // Get all the organizationList where userAddress equals to userAddressId + 1
        defaultOrganizationShouldNotBeFound("userAddressId.equals=" + (userAddressId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        OrganizationBrand organizationBrand = OrganizationBrandResourceIT.createEntity(em);
        em.persist(organizationBrand);
        em.flush();
        organization.addOrganizationBrand(organizationBrand);
        organizationRepository.saveAndFlush(organization);
        Long organizationBrandId = organizationBrand.getId();

        // Get all the organizationList where organizationBrand equals to organizationBrandId
        defaultOrganizationShouldBeFound("organizationBrandId.equals=" + organizationBrandId);

        // Get all the organizationList where organizationBrand equals to organizationBrandId + 1
        defaultOrganizationShouldNotBeFound("organizationBrandId.equals=" + (organizationBrandId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganizationsByOrganizationGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        OrganizationGroup organizationGroup = OrganizationGroupResourceIT.createEntity(em);
        em.persist(organizationGroup);
        em.flush();
        organization.addOrganizationGroup(organizationGroup);
        organizationRepository.saveAndFlush(organization);
        Long organizationGroupId = organizationGroup.getId();

        // Get all the organizationList where organizationGroup equals to organizationGroupId
        defaultOrganizationShouldBeFound("organizationGroupId.equals=" + organizationGroupId);

        // Get all the organizationList where organizationGroup equals to organizationGroupId + 1
        defaultOrganizationShouldNotBeFound("organizationGroupId.equals=" + (organizationGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationShouldBeFound(String filter) throws Exception {
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId())))
            .andExpect(jsonPath("$.[*].organizationUUID").value(hasItem(DEFAULT_ORGANIZATION_UUID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restOrganizationMockMvc.perform(get("/api/organizations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationShouldNotBeFound(String filter) throws Exception {
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationMockMvc.perform(get("/api/organizations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
