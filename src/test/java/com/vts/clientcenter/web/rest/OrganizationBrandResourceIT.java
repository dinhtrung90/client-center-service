package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.OrganizationBrand;
import com.vts.clientcenter.domain.Organization;
import com.vts.clientcenter.repository.OrganizationBrandRepository;
import com.vts.clientcenter.service.OrganizationBrandService;
import com.vts.clientcenter.service.dto.OrganizationBrandDTO;
import com.vts.clientcenter.service.mapper.OrganizationBrandMapper;
import com.vts.clientcenter.service.dto.OrganizationBrandCriteria;
import com.vts.clientcenter.service.OrganizationBrandQueryService;

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
 * Integration tests for the {@link OrganizationBrandResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class OrganizationBrandResourceIT {

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BACKGROUND_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_COLOR = "BBBBBBBBBB";

    @Autowired
    private OrganizationBrandRepository organizationBrandRepository;

    @Autowired
    private OrganizationBrandMapper organizationBrandMapper;

    @Autowired
    private OrganizationBrandService organizationBrandService;

    @Autowired
    private OrganizationBrandQueryService organizationBrandQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationBrandMockMvc;

    private OrganizationBrand organizationBrand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationBrand createEntity(EntityManager em) {
        OrganizationBrand organizationBrand = new OrganizationBrand()
            .logoUrl(DEFAULT_LOGO_URL)
            .primaryColor(DEFAULT_PRIMARY_COLOR)
            .backgroundColor(DEFAULT_BACKGROUND_COLOR);
        return organizationBrand;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationBrand createUpdatedEntity(EntityManager em) {
        OrganizationBrand organizationBrand = new OrganizationBrand()
            .logoUrl(UPDATED_LOGO_URL)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .backgroundColor(UPDATED_BACKGROUND_COLOR);
        return organizationBrand;
    }

    @BeforeEach
    public void initTest() {
        organizationBrand = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrands() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList
        restOrganizationBrandMockMvc.perform(get("/api/organization-brands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)));
    }

    @Test
    @Transactional
    public void getOrganizationBrand() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get the organizationBrand
        restOrganizationBrandMockMvc.perform(get("/api/organization-brands/{id}", organizationBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationBrand.getId().intValue()))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.primaryColor").value(DEFAULT_PRIMARY_COLOR))
            .andExpect(jsonPath("$.backgroundColor").value(DEFAULT_BACKGROUND_COLOR));
    }


    @Test
    @Transactional
    public void getOrganizationBrandsByIdFiltering() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        Long id = organizationBrand.getId();

        defaultOrganizationBrandShouldBeFound("id.equals=" + id);
        defaultOrganizationBrandShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationBrandShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationBrandShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationBrandShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationBrandShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrganizationBrandsByLogoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where logoUrl equals to DEFAULT_LOGO_URL
        defaultOrganizationBrandShouldBeFound("logoUrl.equals=" + DEFAULT_LOGO_URL);

        // Get all the organizationBrandList where logoUrl equals to UPDATED_LOGO_URL
        defaultOrganizationBrandShouldNotBeFound("logoUrl.equals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByLogoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where logoUrl not equals to DEFAULT_LOGO_URL
        defaultOrganizationBrandShouldNotBeFound("logoUrl.notEquals=" + DEFAULT_LOGO_URL);

        // Get all the organizationBrandList where logoUrl not equals to UPDATED_LOGO_URL
        defaultOrganizationBrandShouldBeFound("logoUrl.notEquals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByLogoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where logoUrl in DEFAULT_LOGO_URL or UPDATED_LOGO_URL
        defaultOrganizationBrandShouldBeFound("logoUrl.in=" + DEFAULT_LOGO_URL + "," + UPDATED_LOGO_URL);

        // Get all the organizationBrandList where logoUrl equals to UPDATED_LOGO_URL
        defaultOrganizationBrandShouldNotBeFound("logoUrl.in=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByLogoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where logoUrl is not null
        defaultOrganizationBrandShouldBeFound("logoUrl.specified=true");

        // Get all the organizationBrandList where logoUrl is null
        defaultOrganizationBrandShouldNotBeFound("logoUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationBrandsByLogoUrlContainsSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where logoUrl contains DEFAULT_LOGO_URL
        defaultOrganizationBrandShouldBeFound("logoUrl.contains=" + DEFAULT_LOGO_URL);

        // Get all the organizationBrandList where logoUrl contains UPDATED_LOGO_URL
        defaultOrganizationBrandShouldNotBeFound("logoUrl.contains=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByLogoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where logoUrl does not contain DEFAULT_LOGO_URL
        defaultOrganizationBrandShouldNotBeFound("logoUrl.doesNotContain=" + DEFAULT_LOGO_URL);

        // Get all the organizationBrandList where logoUrl does not contain UPDATED_LOGO_URL
        defaultOrganizationBrandShouldBeFound("logoUrl.doesNotContain=" + UPDATED_LOGO_URL);
    }


    @Test
    @Transactional
    public void getAllOrganizationBrandsByPrimaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where primaryColor equals to DEFAULT_PRIMARY_COLOR
        defaultOrganizationBrandShouldBeFound("primaryColor.equals=" + DEFAULT_PRIMARY_COLOR);

        // Get all the organizationBrandList where primaryColor equals to UPDATED_PRIMARY_COLOR
        defaultOrganizationBrandShouldNotBeFound("primaryColor.equals=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByPrimaryColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where primaryColor not equals to DEFAULT_PRIMARY_COLOR
        defaultOrganizationBrandShouldNotBeFound("primaryColor.notEquals=" + DEFAULT_PRIMARY_COLOR);

        // Get all the organizationBrandList where primaryColor not equals to UPDATED_PRIMARY_COLOR
        defaultOrganizationBrandShouldBeFound("primaryColor.notEquals=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByPrimaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where primaryColor in DEFAULT_PRIMARY_COLOR or UPDATED_PRIMARY_COLOR
        defaultOrganizationBrandShouldBeFound("primaryColor.in=" + DEFAULT_PRIMARY_COLOR + "," + UPDATED_PRIMARY_COLOR);

        // Get all the organizationBrandList where primaryColor equals to UPDATED_PRIMARY_COLOR
        defaultOrganizationBrandShouldNotBeFound("primaryColor.in=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByPrimaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where primaryColor is not null
        defaultOrganizationBrandShouldBeFound("primaryColor.specified=true");

        // Get all the organizationBrandList where primaryColor is null
        defaultOrganizationBrandShouldNotBeFound("primaryColor.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationBrandsByPrimaryColorContainsSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where primaryColor contains DEFAULT_PRIMARY_COLOR
        defaultOrganizationBrandShouldBeFound("primaryColor.contains=" + DEFAULT_PRIMARY_COLOR);

        // Get all the organizationBrandList where primaryColor contains UPDATED_PRIMARY_COLOR
        defaultOrganizationBrandShouldNotBeFound("primaryColor.contains=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByPrimaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where primaryColor does not contain DEFAULT_PRIMARY_COLOR
        defaultOrganizationBrandShouldNotBeFound("primaryColor.doesNotContain=" + DEFAULT_PRIMARY_COLOR);

        // Get all the organizationBrandList where primaryColor does not contain UPDATED_PRIMARY_COLOR
        defaultOrganizationBrandShouldBeFound("primaryColor.doesNotContain=" + UPDATED_PRIMARY_COLOR);
    }


    @Test
    @Transactional
    public void getAllOrganizationBrandsByBackgroundColorIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where backgroundColor equals to DEFAULT_BACKGROUND_COLOR
        defaultOrganizationBrandShouldBeFound("backgroundColor.equals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the organizationBrandList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultOrganizationBrandShouldNotBeFound("backgroundColor.equals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByBackgroundColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where backgroundColor not equals to DEFAULT_BACKGROUND_COLOR
        defaultOrganizationBrandShouldNotBeFound("backgroundColor.notEquals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the organizationBrandList where backgroundColor not equals to UPDATED_BACKGROUND_COLOR
        defaultOrganizationBrandShouldBeFound("backgroundColor.notEquals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByBackgroundColorIsInShouldWork() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where backgroundColor in DEFAULT_BACKGROUND_COLOR or UPDATED_BACKGROUND_COLOR
        defaultOrganizationBrandShouldBeFound("backgroundColor.in=" + DEFAULT_BACKGROUND_COLOR + "," + UPDATED_BACKGROUND_COLOR);

        // Get all the organizationBrandList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultOrganizationBrandShouldNotBeFound("backgroundColor.in=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByBackgroundColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where backgroundColor is not null
        defaultOrganizationBrandShouldBeFound("backgroundColor.specified=true");

        // Get all the organizationBrandList where backgroundColor is null
        defaultOrganizationBrandShouldNotBeFound("backgroundColor.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationBrandsByBackgroundColorContainsSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where backgroundColor contains DEFAULT_BACKGROUND_COLOR
        defaultOrganizationBrandShouldBeFound("backgroundColor.contains=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the organizationBrandList where backgroundColor contains UPDATED_BACKGROUND_COLOR
        defaultOrganizationBrandShouldNotBeFound("backgroundColor.contains=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    public void getAllOrganizationBrandsByBackgroundColorNotContainsSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);

        // Get all the organizationBrandList where backgroundColor does not contain DEFAULT_BACKGROUND_COLOR
        defaultOrganizationBrandShouldNotBeFound("backgroundColor.doesNotContain=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the organizationBrandList where backgroundColor does not contain UPDATED_BACKGROUND_COLOR
        defaultOrganizationBrandShouldBeFound("backgroundColor.doesNotContain=" + UPDATED_BACKGROUND_COLOR);
    }


    @Test
    @Transactional
    public void getAllOrganizationBrandsByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationBrandRepository.saveAndFlush(organizationBrand);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        organizationBrand.setOrganization(organization);
        organizationBrandRepository.saveAndFlush(organizationBrand);
        String organizationId = organization.getId();

        // Get all the organizationBrandList where organization equals to organizationId
        defaultOrganizationBrandShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the organizationBrandList where organization equals to organizationId + 1
        defaultOrganizationBrandShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationBrandShouldBeFound(String filter) throws Exception {
        restOrganizationBrandMockMvc.perform(get("/api/organization-brands?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)));

        // Check, that the count call also returns 1
        restOrganizationBrandMockMvc.perform(get("/api/organization-brands/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationBrandShouldNotBeFound(String filter) throws Exception {
        restOrganizationBrandMockMvc.perform(get("/api/organization-brands?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationBrandMockMvc.perform(get("/api/organization-brands/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationBrand() throws Exception {
        // Get the organizationBrand
        restOrganizationBrandMockMvc.perform(get("/api/organization-brands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
