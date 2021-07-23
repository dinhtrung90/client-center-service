package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.Eligibility;
import com.vts.clientcenter.repository.EligibilityRepository;
import com.vts.clientcenter.service.EligibilityService;
import com.vts.clientcenter.service.mapper.EligibilityMapper;
import com.vts.clientcenter.service.EligibilityQueryService;

import com.vts.clientcenter.web.rest.admin.AdminEligibilityResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AdminEligibilityResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class EligibilityResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SSN = "AAAAAAAAAA";
    private static final String UPDATED_SSN = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .fullName(DEFAULT_FULL_NAME)
            .ssn(DEFAULT_SSN)
            .birthDay(DEFAULT_BIRTH_DAY);
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
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .fullName(UPDATED_FULL_NAME)
            .ssn(UPDATED_SSN)
            .birthDay(UPDATED_BIRTH_DAY);
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibility.getId())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN)))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())));
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
            .andExpect(jsonPath("$.id").value(eligibility.getId()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.ssn").value(DEFAULT_SSN))
            .andExpect(jsonPath("$.birthDay").value(DEFAULT_BIRTH_DAY.toString()));
    }


    @Test
    @Transactional
    public void getEligibilitiesByIdFiltering() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        String id = eligibility.getId();

        defaultEligibilityShouldBeFound("id.equals=" + id);
        defaultEligibilityShouldNotBeFound("id.notEquals=" + id);

        defaultEligibilityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEligibilityShouldNotBeFound("id.greaterThan=" + id);

        defaultEligibilityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEligibilityShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where email equals to DEFAULT_EMAIL
        defaultEligibilityShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the eligibilityList where email equals to UPDATED_EMAIL
        defaultEligibilityShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where email not equals to DEFAULT_EMAIL
        defaultEligibilityShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the eligibilityList where email not equals to UPDATED_EMAIL
        defaultEligibilityShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEligibilityShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the eligibilityList where email equals to UPDATED_EMAIL
        defaultEligibilityShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where email is not null
        defaultEligibilityShouldBeFound("email.specified=true");

        // Get all the eligibilityList where email is null
        defaultEligibilityShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesByEmailContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where email contains DEFAULT_EMAIL
        defaultEligibilityShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the eligibilityList where email contains UPDATED_EMAIL
        defaultEligibilityShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where email does not contain DEFAULT_EMAIL
        defaultEligibilityShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the eligibilityList where email does not contain UPDATED_EMAIL
        defaultEligibilityShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where phone equals to DEFAULT_PHONE
        defaultEligibilityShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the eligibilityList where phone equals to UPDATED_PHONE
        defaultEligibilityShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where phone not equals to DEFAULT_PHONE
        defaultEligibilityShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the eligibilityList where phone not equals to UPDATED_PHONE
        defaultEligibilityShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultEligibilityShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the eligibilityList where phone equals to UPDATED_PHONE
        defaultEligibilityShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where phone is not null
        defaultEligibilityShouldBeFound("phone.specified=true");

        // Get all the eligibilityList where phone is null
        defaultEligibilityShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where phone contains DEFAULT_PHONE
        defaultEligibilityShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the eligibilityList where phone contains UPDATED_PHONE
        defaultEligibilityShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where phone does not contain DEFAULT_PHONE
        defaultEligibilityShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the eligibilityList where phone does not contain UPDATED_PHONE
        defaultEligibilityShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fullName equals to DEFAULT_FULL_NAME
        defaultEligibilityShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the eligibilityList where fullName equals to UPDATED_FULL_NAME
        defaultEligibilityShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fullName not equals to DEFAULT_FULL_NAME
        defaultEligibilityShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the eligibilityList where fullName not equals to UPDATED_FULL_NAME
        defaultEligibilityShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultEligibilityShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the eligibilityList where fullName equals to UPDATED_FULL_NAME
        defaultEligibilityShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fullName is not null
        defaultEligibilityShouldBeFound("fullName.specified=true");

        // Get all the eligibilityList where fullName is null
        defaultEligibilityShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fullName contains DEFAULT_FULL_NAME
        defaultEligibilityShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the eligibilityList where fullName contains UPDATED_FULL_NAME
        defaultEligibilityShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where fullName does not contain DEFAULT_FULL_NAME
        defaultEligibilityShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the eligibilityList where fullName does not contain UPDATED_FULL_NAME
        defaultEligibilityShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesBySsnIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where ssn equals to DEFAULT_SSN
        defaultEligibilityShouldBeFound("ssn.equals=" + DEFAULT_SSN);

        // Get all the eligibilityList where ssn equals to UPDATED_SSN
        defaultEligibilityShouldNotBeFound("ssn.equals=" + UPDATED_SSN);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesBySsnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where ssn not equals to DEFAULT_SSN
        defaultEligibilityShouldNotBeFound("ssn.notEquals=" + DEFAULT_SSN);

        // Get all the eligibilityList where ssn not equals to UPDATED_SSN
        defaultEligibilityShouldBeFound("ssn.notEquals=" + UPDATED_SSN);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesBySsnIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where ssn in DEFAULT_SSN or UPDATED_SSN
        defaultEligibilityShouldBeFound("ssn.in=" + DEFAULT_SSN + "," + UPDATED_SSN);

        // Get all the eligibilityList where ssn equals to UPDATED_SSN
        defaultEligibilityShouldNotBeFound("ssn.in=" + UPDATED_SSN);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesBySsnIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where ssn is not null
        defaultEligibilityShouldBeFound("ssn.specified=true");

        // Get all the eligibilityList where ssn is null
        defaultEligibilityShouldNotBeFound("ssn.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilitiesBySsnContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where ssn contains DEFAULT_SSN
        defaultEligibilityShouldBeFound("ssn.contains=" + DEFAULT_SSN);

        // Get all the eligibilityList where ssn contains UPDATED_SSN
        defaultEligibilityShouldNotBeFound("ssn.contains=" + UPDATED_SSN);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesBySsnNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where ssn does not contain DEFAULT_SSN
        defaultEligibilityShouldNotBeFound("ssn.doesNotContain=" + DEFAULT_SSN);

        // Get all the eligibilityList where ssn does not contain UPDATED_SSN
        defaultEligibilityShouldBeFound("ssn.doesNotContain=" + UPDATED_SSN);
    }


    @Test
    @Transactional
    public void getAllEligibilitiesByBirthDayIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where birthDay equals to DEFAULT_BIRTH_DAY
        defaultEligibilityShouldBeFound("birthDay.equals=" + DEFAULT_BIRTH_DAY);

        // Get all the eligibilityList where birthDay equals to UPDATED_BIRTH_DAY
        defaultEligibilityShouldNotBeFound("birthDay.equals=" + UPDATED_BIRTH_DAY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByBirthDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where birthDay not equals to DEFAULT_BIRTH_DAY
        defaultEligibilityShouldNotBeFound("birthDay.notEquals=" + DEFAULT_BIRTH_DAY);

        // Get all the eligibilityList where birthDay not equals to UPDATED_BIRTH_DAY
        defaultEligibilityShouldBeFound("birthDay.notEquals=" + UPDATED_BIRTH_DAY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByBirthDayIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where birthDay in DEFAULT_BIRTH_DAY or UPDATED_BIRTH_DAY
        defaultEligibilityShouldBeFound("birthDay.in=" + DEFAULT_BIRTH_DAY + "," + UPDATED_BIRTH_DAY);

        // Get all the eligibilityList where birthDay equals to UPDATED_BIRTH_DAY
        defaultEligibilityShouldNotBeFound("birthDay.in=" + UPDATED_BIRTH_DAY);
    }

    @Test
    @Transactional
    public void getAllEligibilitiesByBirthDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList where birthDay is not null
        defaultEligibilityShouldBeFound("birthDay.specified=true");

        // Get all the eligibilityList where birthDay is null
        defaultEligibilityShouldNotBeFound("birthDay.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEligibilityShouldBeFound(String filter) throws Exception {
        restEligibilityMockMvc.perform(get("/api/eligibilities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibility.getId())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN)))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())));

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
