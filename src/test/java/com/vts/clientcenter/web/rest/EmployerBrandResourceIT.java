package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.EmployerBrand;
import com.vts.clientcenter.domain.Employer;
import com.vts.clientcenter.repository.EmployerBrandRepository;
import com.vts.clientcenter.service.EmployerBrandService;
import com.vts.clientcenter.service.dto.EmployerBrandDTO;
import com.vts.clientcenter.service.mapper.EmployerBrandMapper;
import com.vts.clientcenter.service.dto.EmployerBrandCriteria;
import com.vts.clientcenter.service.EmployerBrandQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EmployerBrandResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class EmployerBrandResourceIT {

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private EmployerBrandRepository employerBrandRepository;

    @Autowired
    private EmployerBrandMapper employerBrandMapper;

    @Autowired
    private EmployerBrandService employerBrandService;

    @Autowired
    private EmployerBrandQueryService employerBrandQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployerBrandMockMvc;

    private EmployerBrand employerBrand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployerBrand createEntity(EntityManager em) {
        EmployerBrand employerBrand = new EmployerBrand()
            .logoUrl(DEFAULT_LOGO_URL)
            .primaryColor(DEFAULT_PRIMARY_COLOR)
            .displayName(DEFAULT_DISPLAY_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return employerBrand;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployerBrand createUpdatedEntity(EntityManager em) {
        EmployerBrand employerBrand = new EmployerBrand()
            .logoUrl(UPDATED_LOGO_URL)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .displayName(UPDATED_DISPLAY_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return employerBrand;
    }

    @BeforeEach
    public void initTest() {
        employerBrand = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllEmployerBrands() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList
        restEmployerBrandMockMvc.perform(get("/api/employer-brands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employerBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getEmployerBrand() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get the employerBrand
        restEmployerBrandMockMvc.perform(get("/api/employer-brands/{id}", employerBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employerBrand.getId().intValue()))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.primaryColor").value(DEFAULT_PRIMARY_COLOR))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getEmployerBrandsByIdFiltering() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        Long id = employerBrand.getId();

        defaultEmployerBrandShouldBeFound("id.equals=" + id);
        defaultEmployerBrandShouldNotBeFound("id.notEquals=" + id);

        defaultEmployerBrandShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployerBrandShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployerBrandShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployerBrandShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmployerBrandsByLogoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where logoUrl equals to DEFAULT_LOGO_URL
        defaultEmployerBrandShouldBeFound("logoUrl.equals=" + DEFAULT_LOGO_URL);

        // Get all the employerBrandList where logoUrl equals to UPDATED_LOGO_URL
        defaultEmployerBrandShouldNotBeFound("logoUrl.equals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLogoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where logoUrl not equals to DEFAULT_LOGO_URL
        defaultEmployerBrandShouldNotBeFound("logoUrl.notEquals=" + DEFAULT_LOGO_URL);

        // Get all the employerBrandList where logoUrl not equals to UPDATED_LOGO_URL
        defaultEmployerBrandShouldBeFound("logoUrl.notEquals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLogoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where logoUrl in DEFAULT_LOGO_URL or UPDATED_LOGO_URL
        defaultEmployerBrandShouldBeFound("logoUrl.in=" + DEFAULT_LOGO_URL + "," + UPDATED_LOGO_URL);

        // Get all the employerBrandList where logoUrl equals to UPDATED_LOGO_URL
        defaultEmployerBrandShouldNotBeFound("logoUrl.in=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLogoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where logoUrl is not null
        defaultEmployerBrandShouldBeFound("logoUrl.specified=true");

        // Get all the employerBrandList where logoUrl is null
        defaultEmployerBrandShouldNotBeFound("logoUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployerBrandsByLogoUrlContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where logoUrl contains DEFAULT_LOGO_URL
        defaultEmployerBrandShouldBeFound("logoUrl.contains=" + DEFAULT_LOGO_URL);

        // Get all the employerBrandList where logoUrl contains UPDATED_LOGO_URL
        defaultEmployerBrandShouldNotBeFound("logoUrl.contains=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLogoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where logoUrl does not contain DEFAULT_LOGO_URL
        defaultEmployerBrandShouldNotBeFound("logoUrl.doesNotContain=" + DEFAULT_LOGO_URL);

        // Get all the employerBrandList where logoUrl does not contain UPDATED_LOGO_URL
        defaultEmployerBrandShouldBeFound("logoUrl.doesNotContain=" + UPDATED_LOGO_URL);
    }


    @Test
    @Transactional
    public void getAllEmployerBrandsByPrimaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where primaryColor equals to DEFAULT_PRIMARY_COLOR
        defaultEmployerBrandShouldBeFound("primaryColor.equals=" + DEFAULT_PRIMARY_COLOR);

        // Get all the employerBrandList where primaryColor equals to UPDATED_PRIMARY_COLOR
        defaultEmployerBrandShouldNotBeFound("primaryColor.equals=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByPrimaryColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where primaryColor not equals to DEFAULT_PRIMARY_COLOR
        defaultEmployerBrandShouldNotBeFound("primaryColor.notEquals=" + DEFAULT_PRIMARY_COLOR);

        // Get all the employerBrandList where primaryColor not equals to UPDATED_PRIMARY_COLOR
        defaultEmployerBrandShouldBeFound("primaryColor.notEquals=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByPrimaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where primaryColor in DEFAULT_PRIMARY_COLOR or UPDATED_PRIMARY_COLOR
        defaultEmployerBrandShouldBeFound("primaryColor.in=" + DEFAULT_PRIMARY_COLOR + "," + UPDATED_PRIMARY_COLOR);

        // Get all the employerBrandList where primaryColor equals to UPDATED_PRIMARY_COLOR
        defaultEmployerBrandShouldNotBeFound("primaryColor.in=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByPrimaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where primaryColor is not null
        defaultEmployerBrandShouldBeFound("primaryColor.specified=true");

        // Get all the employerBrandList where primaryColor is null
        defaultEmployerBrandShouldNotBeFound("primaryColor.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployerBrandsByPrimaryColorContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where primaryColor contains DEFAULT_PRIMARY_COLOR
        defaultEmployerBrandShouldBeFound("primaryColor.contains=" + DEFAULT_PRIMARY_COLOR);

        // Get all the employerBrandList where primaryColor contains UPDATED_PRIMARY_COLOR
        defaultEmployerBrandShouldNotBeFound("primaryColor.contains=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByPrimaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where primaryColor does not contain DEFAULT_PRIMARY_COLOR
        defaultEmployerBrandShouldNotBeFound("primaryColor.doesNotContain=" + DEFAULT_PRIMARY_COLOR);

        // Get all the employerBrandList where primaryColor does not contain UPDATED_PRIMARY_COLOR
        defaultEmployerBrandShouldBeFound("primaryColor.doesNotContain=" + UPDATED_PRIMARY_COLOR);
    }


    @Test
    @Transactional
    public void getAllEmployerBrandsByDisplayNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where displayName equals to DEFAULT_DISPLAY_NAME
        defaultEmployerBrandShouldBeFound("displayName.equals=" + DEFAULT_DISPLAY_NAME);

        // Get all the employerBrandList where displayName equals to UPDATED_DISPLAY_NAME
        defaultEmployerBrandShouldNotBeFound("displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByDisplayNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where displayName not equals to DEFAULT_DISPLAY_NAME
        defaultEmployerBrandShouldNotBeFound("displayName.notEquals=" + DEFAULT_DISPLAY_NAME);

        // Get all the employerBrandList where displayName not equals to UPDATED_DISPLAY_NAME
        defaultEmployerBrandShouldBeFound("displayName.notEquals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByDisplayNameIsInShouldWork() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where displayName in DEFAULT_DISPLAY_NAME or UPDATED_DISPLAY_NAME
        defaultEmployerBrandShouldBeFound("displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME);

        // Get all the employerBrandList where displayName equals to UPDATED_DISPLAY_NAME
        defaultEmployerBrandShouldNotBeFound("displayName.in=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByDisplayNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where displayName is not null
        defaultEmployerBrandShouldBeFound("displayName.specified=true");

        // Get all the employerBrandList where displayName is null
        defaultEmployerBrandShouldNotBeFound("displayName.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployerBrandsByDisplayNameContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where displayName contains DEFAULT_DISPLAY_NAME
        defaultEmployerBrandShouldBeFound("displayName.contains=" + DEFAULT_DISPLAY_NAME);

        // Get all the employerBrandList where displayName contains UPDATED_DISPLAY_NAME
        defaultEmployerBrandShouldNotBeFound("displayName.contains=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByDisplayNameNotContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where displayName does not contain DEFAULT_DISPLAY_NAME
        defaultEmployerBrandShouldNotBeFound("displayName.doesNotContain=" + DEFAULT_DISPLAY_NAME);

        // Get all the employerBrandList where displayName does not contain UPDATED_DISPLAY_NAME
        defaultEmployerBrandShouldBeFound("displayName.doesNotContain=" + UPDATED_DISPLAY_NAME);
    }


    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdDate equals to DEFAULT_CREATED_DATE
        defaultEmployerBrandShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the employerBrandList where createdDate equals to UPDATED_CREATED_DATE
        defaultEmployerBrandShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultEmployerBrandShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the employerBrandList where createdDate not equals to UPDATED_CREATED_DATE
        defaultEmployerBrandShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultEmployerBrandShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the employerBrandList where createdDate equals to UPDATED_CREATED_DATE
        defaultEmployerBrandShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdDate is not null
        defaultEmployerBrandShouldBeFound("createdDate.specified=true");

        // Get all the employerBrandList where createdDate is null
        defaultEmployerBrandShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultEmployerBrandShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the employerBrandList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultEmployerBrandShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultEmployerBrandShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the employerBrandList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultEmployerBrandShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultEmployerBrandShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the employerBrandList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultEmployerBrandShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedDate is not null
        defaultEmployerBrandShouldBeFound("lastModifiedDate.specified=true");

        // Get all the employerBrandList where lastModifiedDate is null
        defaultEmployerBrandShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdBy equals to DEFAULT_CREATED_BY
        defaultEmployerBrandShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the employerBrandList where createdBy equals to UPDATED_CREATED_BY
        defaultEmployerBrandShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdBy not equals to DEFAULT_CREATED_BY
        defaultEmployerBrandShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the employerBrandList where createdBy not equals to UPDATED_CREATED_BY
        defaultEmployerBrandShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultEmployerBrandShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the employerBrandList where createdBy equals to UPDATED_CREATED_BY
        defaultEmployerBrandShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdBy is not null
        defaultEmployerBrandShouldBeFound("createdBy.specified=true");

        // Get all the employerBrandList where createdBy is null
        defaultEmployerBrandShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdBy contains DEFAULT_CREATED_BY
        defaultEmployerBrandShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the employerBrandList where createdBy contains UPDATED_CREATED_BY
        defaultEmployerBrandShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where createdBy does not contain DEFAULT_CREATED_BY
        defaultEmployerBrandShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the employerBrandList where createdBy does not contain UPDATED_CREATED_BY
        defaultEmployerBrandShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultEmployerBrandShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerBrandList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerBrandShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultEmployerBrandShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerBrandList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerBrandShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultEmployerBrandShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the employerBrandList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerBrandShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedBy is not null
        defaultEmployerBrandShouldBeFound("lastModifiedBy.specified=true");

        // Get all the employerBrandList where lastModifiedBy is null
        defaultEmployerBrandShouldNotBeFound("lastModifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultEmployerBrandShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerBrandList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultEmployerBrandShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerBrandsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);

        // Get all the employerBrandList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultEmployerBrandShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerBrandList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultEmployerBrandShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllEmployerBrandsByEmployerIsEqualToSomething() throws Exception {
        // Initialize the database
        employerBrandRepository.saveAndFlush(employerBrand);
        Employer employer = EmployerResourceIT.createEntity(em);
        em.persist(employer);
        em.flush();
        employerBrand.setEmployer(employer);
        employerBrandRepository.saveAndFlush(employerBrand);
        Long employerId = employer.getId();

        // Get all the employerBrandList where employer equals to employerId
        defaultEmployerBrandShouldBeFound("employerId.equals=" + employerId);

        // Get all the employerBrandList where employer equals to employerId + 1
        defaultEmployerBrandShouldNotBeFound("employerId.equals=" + (employerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployerBrandShouldBeFound(String filter) throws Exception {
        restEmployerBrandMockMvc.perform(get("/api/employer-brands?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employerBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restEmployerBrandMockMvc.perform(get("/api/employer-brands/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployerBrandShouldNotBeFound(String filter) throws Exception {
        restEmployerBrandMockMvc.perform(get("/api/employer-brands?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployerBrandMockMvc.perform(get("/api/employer-brands/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEmployerBrand() throws Exception {
        // Get the employerBrand
        restEmployerBrandMockMvc.perform(get("/api/employer-brands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
