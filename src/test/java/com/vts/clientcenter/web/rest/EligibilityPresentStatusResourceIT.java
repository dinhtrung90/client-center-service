package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.EligibilityPresentStatus;
import com.vts.clientcenter.domain.Eligibility;
import com.vts.clientcenter.repository.EligibilityPresentStatusRepository;
import com.vts.clientcenter.service.EligibilityPresentStatusService;
import com.vts.clientcenter.service.dto.EligibilityPresentStatusDTO;
import com.vts.clientcenter.service.mapper.EligibilityPresentStatusMapper;
import com.vts.clientcenter.service.dto.EligibilityPresentStatusCriteria;
import com.vts.clientcenter.service.EligibilityPresentStatusQueryService;

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
 * Integration tests for the {@link EligibilityPresentStatusResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class EligibilityPresentStatusResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_QR_CODE_URL = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_PRESENT = false;
    private static final Boolean UPDATED_HAS_PRESENT = true;

    private static final Instant DEFAULT_EXPIRED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EligibilityPresentStatusRepository eligibilityPresentStatusRepository;

    @Autowired
    private EligibilityPresentStatusMapper eligibilityPresentStatusMapper;

    @Autowired
    private EligibilityPresentStatusService eligibilityPresentStatusService;

    @Autowired
    private EligibilityPresentStatusQueryService eligibilityPresentStatusQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEligibilityPresentStatusMockMvc;

    private EligibilityPresentStatus eligibilityPresentStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EligibilityPresentStatus createEntity(EntityManager em) {
        EligibilityPresentStatus eligibilityPresentStatus = new EligibilityPresentStatus()
            .code(DEFAULT_CODE)
            .qrCodeUrl(DEFAULT_QR_CODE_URL)
            .hasPresent(DEFAULT_HAS_PRESENT)
            .expiredAt(DEFAULT_EXPIRED_AT);
        return eligibilityPresentStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EligibilityPresentStatus createUpdatedEntity(EntityManager em) {
        EligibilityPresentStatus eligibilityPresentStatus = new EligibilityPresentStatus()
            .code(UPDATED_CODE)
            .qrCodeUrl(UPDATED_QR_CODE_URL)
            .hasPresent(UPDATED_HAS_PRESENT)
            .expiredAt(UPDATED_EXPIRED_AT);
        return eligibilityPresentStatus;
    }

    @BeforeEach
    public void initTest() {
        eligibilityPresentStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatuses() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList
        restEligibilityPresentStatusMockMvc.perform(get("/api/eligibility-present-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibilityPresentStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].qrCodeUrl").value(hasItem(DEFAULT_QR_CODE_URL)))
            .andExpect(jsonPath("$.[*].hasPresent").value(hasItem(DEFAULT_HAS_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].expiredAt").value(hasItem(DEFAULT_EXPIRED_AT.toString())));
    }

    @Test
    @Transactional
    public void getEligibilityPresentStatus() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get the eligibilityPresentStatus
        restEligibilityPresentStatusMockMvc.perform(get("/api/eligibility-present-statuses/{id}", eligibilityPresentStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eligibilityPresentStatus.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.qrCodeUrl").value(DEFAULT_QR_CODE_URL))
            .andExpect(jsonPath("$.hasPresent").value(DEFAULT_HAS_PRESENT.booleanValue()))
            .andExpect(jsonPath("$.expiredAt").value(DEFAULT_EXPIRED_AT.toString()));
    }


    @Test
    @Transactional
    public void getEligibilityPresentStatusesByIdFiltering() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        Long id = eligibilityPresentStatus.getId();

        defaultEligibilityPresentStatusShouldBeFound("id.equals=" + id);
        defaultEligibilityPresentStatusShouldNotBeFound("id.notEquals=" + id);

        defaultEligibilityPresentStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEligibilityPresentStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultEligibilityPresentStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEligibilityPresentStatusShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where code equals to DEFAULT_CODE
        defaultEligibilityPresentStatusShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the eligibilityPresentStatusList where code equals to UPDATED_CODE
        defaultEligibilityPresentStatusShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where code not equals to DEFAULT_CODE
        defaultEligibilityPresentStatusShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the eligibilityPresentStatusList where code not equals to UPDATED_CODE
        defaultEligibilityPresentStatusShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where code in DEFAULT_CODE or UPDATED_CODE
        defaultEligibilityPresentStatusShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the eligibilityPresentStatusList where code equals to UPDATED_CODE
        defaultEligibilityPresentStatusShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where code is not null
        defaultEligibilityPresentStatusShouldBeFound("code.specified=true");

        // Get all the eligibilityPresentStatusList where code is null
        defaultEligibilityPresentStatusShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByCodeContainsSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where code contains DEFAULT_CODE
        defaultEligibilityPresentStatusShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the eligibilityPresentStatusList where code contains UPDATED_CODE
        defaultEligibilityPresentStatusShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where code does not contain DEFAULT_CODE
        defaultEligibilityPresentStatusShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the eligibilityPresentStatusList where code does not contain UPDATED_CODE
        defaultEligibilityPresentStatusShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByQrCodeUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where qrCodeUrl equals to DEFAULT_QR_CODE_URL
        defaultEligibilityPresentStatusShouldBeFound("qrCodeUrl.equals=" + DEFAULT_QR_CODE_URL);

        // Get all the eligibilityPresentStatusList where qrCodeUrl equals to UPDATED_QR_CODE_URL
        defaultEligibilityPresentStatusShouldNotBeFound("qrCodeUrl.equals=" + UPDATED_QR_CODE_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByQrCodeUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where qrCodeUrl not equals to DEFAULT_QR_CODE_URL
        defaultEligibilityPresentStatusShouldNotBeFound("qrCodeUrl.notEquals=" + DEFAULT_QR_CODE_URL);

        // Get all the eligibilityPresentStatusList where qrCodeUrl not equals to UPDATED_QR_CODE_URL
        defaultEligibilityPresentStatusShouldBeFound("qrCodeUrl.notEquals=" + UPDATED_QR_CODE_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByQrCodeUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where qrCodeUrl in DEFAULT_QR_CODE_URL or UPDATED_QR_CODE_URL
        defaultEligibilityPresentStatusShouldBeFound("qrCodeUrl.in=" + DEFAULT_QR_CODE_URL + "," + UPDATED_QR_CODE_URL);

        // Get all the eligibilityPresentStatusList where qrCodeUrl equals to UPDATED_QR_CODE_URL
        defaultEligibilityPresentStatusShouldNotBeFound("qrCodeUrl.in=" + UPDATED_QR_CODE_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByQrCodeUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where qrCodeUrl is not null
        defaultEligibilityPresentStatusShouldBeFound("qrCodeUrl.specified=true");

        // Get all the eligibilityPresentStatusList where qrCodeUrl is null
        defaultEligibilityPresentStatusShouldNotBeFound("qrCodeUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByQrCodeUrlContainsSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where qrCodeUrl contains DEFAULT_QR_CODE_URL
        defaultEligibilityPresentStatusShouldBeFound("qrCodeUrl.contains=" + DEFAULT_QR_CODE_URL);

        // Get all the eligibilityPresentStatusList where qrCodeUrl contains UPDATED_QR_CODE_URL
        defaultEligibilityPresentStatusShouldNotBeFound("qrCodeUrl.contains=" + UPDATED_QR_CODE_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByQrCodeUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where qrCodeUrl does not contain DEFAULT_QR_CODE_URL
        defaultEligibilityPresentStatusShouldNotBeFound("qrCodeUrl.doesNotContain=" + DEFAULT_QR_CODE_URL);

        // Get all the eligibilityPresentStatusList where qrCodeUrl does not contain UPDATED_QR_CODE_URL
        defaultEligibilityPresentStatusShouldBeFound("qrCodeUrl.doesNotContain=" + UPDATED_QR_CODE_URL);
    }


    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByHasPresentIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where hasPresent equals to DEFAULT_HAS_PRESENT
        defaultEligibilityPresentStatusShouldBeFound("hasPresent.equals=" + DEFAULT_HAS_PRESENT);

        // Get all the eligibilityPresentStatusList where hasPresent equals to UPDATED_HAS_PRESENT
        defaultEligibilityPresentStatusShouldNotBeFound("hasPresent.equals=" + UPDATED_HAS_PRESENT);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByHasPresentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where hasPresent not equals to DEFAULT_HAS_PRESENT
        defaultEligibilityPresentStatusShouldNotBeFound("hasPresent.notEquals=" + DEFAULT_HAS_PRESENT);

        // Get all the eligibilityPresentStatusList where hasPresent not equals to UPDATED_HAS_PRESENT
        defaultEligibilityPresentStatusShouldBeFound("hasPresent.notEquals=" + UPDATED_HAS_PRESENT);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByHasPresentIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where hasPresent in DEFAULT_HAS_PRESENT or UPDATED_HAS_PRESENT
        defaultEligibilityPresentStatusShouldBeFound("hasPresent.in=" + DEFAULT_HAS_PRESENT + "," + UPDATED_HAS_PRESENT);

        // Get all the eligibilityPresentStatusList where hasPresent equals to UPDATED_HAS_PRESENT
        defaultEligibilityPresentStatusShouldNotBeFound("hasPresent.in=" + UPDATED_HAS_PRESENT);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByHasPresentIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where hasPresent is not null
        defaultEligibilityPresentStatusShouldBeFound("hasPresent.specified=true");

        // Get all the eligibilityPresentStatusList where hasPresent is null
        defaultEligibilityPresentStatusShouldNotBeFound("hasPresent.specified=false");
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByExpiredAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where expiredAt equals to DEFAULT_EXPIRED_AT
        defaultEligibilityPresentStatusShouldBeFound("expiredAt.equals=" + DEFAULT_EXPIRED_AT);

        // Get all the eligibilityPresentStatusList where expiredAt equals to UPDATED_EXPIRED_AT
        defaultEligibilityPresentStatusShouldNotBeFound("expiredAt.equals=" + UPDATED_EXPIRED_AT);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByExpiredAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where expiredAt not equals to DEFAULT_EXPIRED_AT
        defaultEligibilityPresentStatusShouldNotBeFound("expiredAt.notEquals=" + DEFAULT_EXPIRED_AT);

        // Get all the eligibilityPresentStatusList where expiredAt not equals to UPDATED_EXPIRED_AT
        defaultEligibilityPresentStatusShouldBeFound("expiredAt.notEquals=" + UPDATED_EXPIRED_AT);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByExpiredAtIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where expiredAt in DEFAULT_EXPIRED_AT or UPDATED_EXPIRED_AT
        defaultEligibilityPresentStatusShouldBeFound("expiredAt.in=" + DEFAULT_EXPIRED_AT + "," + UPDATED_EXPIRED_AT);

        // Get all the eligibilityPresentStatusList where expiredAt equals to UPDATED_EXPIRED_AT
        defaultEligibilityPresentStatusShouldNotBeFound("expiredAt.in=" + UPDATED_EXPIRED_AT);
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByExpiredAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);

        // Get all the eligibilityPresentStatusList where expiredAt is not null
        defaultEligibilityPresentStatusShouldBeFound("expiredAt.specified=true");

        // Get all the eligibilityPresentStatusList where expiredAt is null
        defaultEligibilityPresentStatusShouldNotBeFound("expiredAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllEligibilityPresentStatusesByEligibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);
        Eligibility eligibility = EligibilityResourceIT.createEntity(em);
        em.persist(eligibility);
        em.flush();
        eligibilityPresentStatus.setEligibility(eligibility);
        eligibilityPresentStatusRepository.saveAndFlush(eligibilityPresentStatus);
        String eligibilityId = eligibility.getId();

        // Get all the eligibilityPresentStatusList where eligibility equals to eligibilityId
        defaultEligibilityPresentStatusShouldBeFound("eligibilityId.equals=" + eligibilityId);

        // Get all the eligibilityPresentStatusList where eligibility equals to eligibilityId + 1
        defaultEligibilityPresentStatusShouldNotBeFound("eligibilityId.equals=" + (eligibilityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEligibilityPresentStatusShouldBeFound(String filter) throws Exception {
        restEligibilityPresentStatusMockMvc.perform(get("/api/eligibility-present-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibilityPresentStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].qrCodeUrl").value(hasItem(DEFAULT_QR_CODE_URL)))
            .andExpect(jsonPath("$.[*].hasPresent").value(hasItem(DEFAULT_HAS_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].expiredAt").value(hasItem(DEFAULT_EXPIRED_AT.toString())));

        // Check, that the count call also returns 1
        restEligibilityPresentStatusMockMvc.perform(get("/api/eligibility-present-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEligibilityPresentStatusShouldNotBeFound(String filter) throws Exception {
        restEligibilityPresentStatusMockMvc.perform(get("/api/eligibility-present-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEligibilityPresentStatusMockMvc.perform(get("/api/eligibility-present-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEligibilityPresentStatus() throws Exception {
        // Get the eligibilityPresentStatus
        restEligibilityPresentStatusMockMvc.perform(get("/api/eligibility-present-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
