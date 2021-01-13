package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.Eligibility;
import com.vts.clientcenter.repository.EligibilityRepository;
import com.vts.clientcenter.service.EligibilityService;
import com.vts.clientcenter.service.dto.EligibilityDTO;
import com.vts.clientcenter.service.mapper.EligibilityMapper;
import com.vts.clientcenter.service.dto.EligibilityCriteria;
import com.vts.clientcenter.service.EligibilityQueryService;

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
 * Integration tests for the {@link EligibilityResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class EligibilityResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REF_ID = "AAAAAAAAAA";
    private static final String UPDATED_REF_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EligibilityRepository eligibilityRepository;

    @Autowired
    private EligibilityMapper eligibilityMapper;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private EligibilityQueryService eligibilityQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEligibilityMockMvc;

    private Eligibility eligibility;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eligibility createEntity(EntityManager em) {
        Eligibility eligibility = new Eligibility()
            .fileName(DEFAULT_FILE_NAME)
            .refId(DEFAULT_REF_ID)
            .fileUrl(DEFAULT_FILE_URL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return eligibility;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eligibility createUpdatedEntity(EntityManager em) {
        Eligibility eligibility = new Eligibility()
            .fileName(UPDATED_FILE_NAME)
            .refId(UPDATED_REF_ID)
            .fileUrl(UPDATED_FILE_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return eligibility;
    }

    @BeforeEach
    public void initTest() {
        eligibility = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllEligibilities() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList
        restEligibilityMockMvc.perform(get("/api/eligibilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibility.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].refId").value(hasItem(DEFAULT_REF_ID)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getEligibility() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get the eligibility
        restEligibilityMockMvc.perform(get("/api/eligibilities/{id}", eligibility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eligibility.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.refId").value(DEFAULT_REF_ID))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }


    @Test
    @Transactional
    public void getEligibilitiesByIdFiltering() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        Long id = eligibility.getId();

        defaultEligibilityShouldBeFound("id.equals=" + id);
        defaultEligibilityShouldNotBeFound("id.notEquals=" + id);

        defaultEligibilityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEligibilityShouldNotBeFound("id.greaterThan=" + id);

        defaultEligibilityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEligibilityShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileName equals to DEFAULT_FILE_NAME
        defaultEligibilityShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the eligibilityList where fileName equals to UPDATED_FILE_NAME
        defaultEligibilityShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileName not equals to DEFAULT_FILE_NAME
        defaultEligibilityShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the eligibilityList where fileName not equals to UPDATED_FILE_NAME
        defaultEligibilityShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultEligibilityShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the eligibilityList where fileName equals to UPDATED_FILE_NAME
        defaultEligibilityShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileName is not null
        defaultEligibilityShouldBeFound("fileName.specified=true");

        // Get all the eligibilityList where fileName is null
        defaultEligibilityShouldNotBeFound("fileName.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileName contains DEFAULT_FILE_NAME
        defaultEligibilityShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the eligibilityList where fileName contains UPDATED_FILE_NAME
        defaultEligibilityShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileName does not contain DEFAULT_FILE_NAME
        defaultEligibilityShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the eligibilityList where fileName does not contain UPDATED_FILE_NAME
        defaultEligibilityShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByRefIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where refId equals to DEFAULT_REF_ID
        defaultEligibilityShouldBeFound("refId.equals=" + DEFAULT_REF_ID);

        // Get all the eligibilityList where refId equals to UPDATED_REF_ID
        defaultEligibilityShouldNotBeFound("refId.equals=" + UPDATED_REF_ID);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByRefIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where refId not equals to DEFAULT_REF_ID
        defaultEligibilityShouldNotBeFound("refId.notEquals=" + DEFAULT_REF_ID);

        // Get all the eligibilityList where refId not equals to UPDATED_REF_ID
        defaultEligibilityShouldBeFound("refId.notEquals=" + UPDATED_REF_ID);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByRefIdIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where refId in DEFAULT_REF_ID or UPDATED_REF_ID
        defaultEligibilityShouldBeFound("refId.in=" + DEFAULT_REF_ID + "," + UPDATED_REF_ID);

        // Get all the eligibilityList where refId equals to UPDATED_REF_ID
        defaultEligibilityShouldNotBeFound("refId.in=" + UPDATED_REF_ID);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByRefIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where refId is not null
        defaultEligibilityShouldBeFound("refId.specified=true");

        // Get all the eligibilityList where refId is null
        defaultEligibilityShouldNotBeFound("refId.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesByRefIdContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where refId contains DEFAULT_REF_ID
        defaultEligibilityShouldBeFound("refId.contains=" + DEFAULT_REF_ID);

        // Get all the eligibilityList where refId contains UPDATED_REF_ID
        defaultEligibilityShouldNotBeFound("refId.contains=" + UPDATED_REF_ID);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByRefIdNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where refId does not contain DEFAULT_REF_ID
        defaultEligibilityShouldNotBeFound("refId.doesNotContain=" + DEFAULT_REF_ID);

        // Get all the eligibilityList where refId does not contain UPDATED_REF_ID
        defaultEligibilityShouldBeFound("refId.doesNotContain=" + UPDATED_REF_ID);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByFileUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileUrl equals to DEFAULT_FILE_URL
        defaultEligibilityShouldBeFound("fileUrl.equals=" + DEFAULT_FILE_URL);

        // Get all the eligibilityList where fileUrl equals to UPDATED_FILE_URL
        defaultEligibilityShouldNotBeFound("fileUrl.equals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFileUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileUrl not equals to DEFAULT_FILE_URL
        defaultEligibilityShouldNotBeFound("fileUrl.notEquals=" + DEFAULT_FILE_URL);

        // Get all the eligibilityList where fileUrl not equals to UPDATED_FILE_URL
        defaultEligibilityShouldBeFound("fileUrl.notEquals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFileUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileUrl in DEFAULT_FILE_URL or UPDATED_FILE_URL
        defaultEligibilityShouldBeFound("fileUrl.in=" + DEFAULT_FILE_URL + "," + UPDATED_FILE_URL);

        // Get all the eligibilityList where fileUrl equals to UPDATED_FILE_URL
        defaultEligibilityShouldNotBeFound("fileUrl.in=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFileUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileUrl is not null
        defaultEligibilityShouldBeFound("fileUrl.specified=true");

        // Get all the eligibilityList where fileUrl is null
        defaultEligibilityShouldNotBeFound("fileUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesByFileUrlContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileUrl contains DEFAULT_FILE_URL
        defaultEligibilityShouldBeFound("fileUrl.contains=" + DEFAULT_FILE_URL);

        // Get all the eligibilityList where fileUrl contains UPDATED_FILE_URL
        defaultEligibilityShouldNotBeFound("fileUrl.contains=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFileUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fileUrl does not contain DEFAULT_FILE_URL
        defaultEligibilityShouldNotBeFound("fileUrl.doesNotContain=" + DEFAULT_FILE_URL);

        // Get all the eligibilityList where fileUrl does not contain UPDATED_FILE_URL
        defaultEligibilityShouldBeFound("fileUrl.doesNotContain=" + UPDATED_FILE_URL);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdBy equals to DEFAULT_CREATED_BY
        defaultEligibilityShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the eligibilityList where createdBy equals to UPDATED_CREATED_BY
        defaultEligibilityShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdBy not equals to DEFAULT_CREATED_BY
        defaultEligibilityShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the eligibilityList where createdBy not equals to UPDATED_CREATED_BY
        defaultEligibilityShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultEligibilityShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the eligibilityList where createdBy equals to UPDATED_CREATED_BY
        defaultEligibilityShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdBy is not null
        defaultEligibilityShouldBeFound("createdBy.specified=true");

        // Get all the eligibilityList where createdBy is null
        defaultEligibilityShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdBy contains DEFAULT_CREATED_BY
        defaultEligibilityShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the eligibilityList where createdBy contains UPDATED_CREATED_BY
        defaultEligibilityShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdBy does not contain DEFAULT_CREATED_BY
        defaultEligibilityShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the eligibilityList where createdBy does not contain UPDATED_CREATED_BY
        defaultEligibilityShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdDate equals to DEFAULT_CREATED_DATE
        defaultEligibilityShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the eligibilityList where createdDate equals to UPDATED_CREATED_DATE
        defaultEligibilityShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultEligibilityShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the eligibilityList where createdDate not equals to UPDATED_CREATED_DATE
        defaultEligibilityShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultEligibilityShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the eligibilityList where createdDate equals to UPDATED_CREATED_DATE
        defaultEligibilityShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where createdDate is not null
        defaultEligibilityShouldBeFound("createdDate.specified=true");

        // Get all the eligibilityList where createdDate is null
        defaultEligibilityShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultEligibilityShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the eligibilityList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEligibilityShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultEligibilityShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the eligibilityList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultEligibilityShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultEligibilityShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the eligibilityList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEligibilityShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedBy is not null
        defaultEligibilityShouldBeFound("lastModifiedBy.specified=true");

        // Get all the eligibilityList where lastModifiedBy is null
        defaultEligibilityShouldNotBeFound("lastModifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultEligibilityShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the eligibilityList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultEligibilityShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultEligibilityShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the eligibilityList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultEligibilityShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultEligibilityShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the eligibilityList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultEligibilityShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultEligibilityShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the eligibilityList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultEligibilityShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultEligibilityShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the eligibilityList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultEligibilityShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where lastModifiedDate is not null
        defaultEligibilityShouldBeFound("lastModifiedDate.specified=true");

        // Get all the eligibilityList where lastModifiedDate is null
        defaultEligibilityShouldNotBeFound("lastModifiedDate.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEligibilityShouldBeFound(String filter) throws Exception {
        restEligibilityMockMvc.perform(get("/api/eligibilities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibility.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].refId").value(hasItem(DEFAULT_REF_ID)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restEligibilityMockMvc.perform(get("/api/eligibilities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEligibilityShouldNotBeFound(String filter) throws Exception {
        restEligibilityMockMvc.perform(get("/api/eligibilities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEligibilityMockMvc.perform(get("/api/eligibilities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEligibility() throws Exception {
        // Get the eligibility
        restEligibilityMockMvc.perform(get("/api/eligibilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
