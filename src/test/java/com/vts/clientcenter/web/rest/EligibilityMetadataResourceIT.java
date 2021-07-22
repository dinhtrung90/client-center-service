package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.EligibilityMetadata;
import com.vts.clientcenter.repository.EligibilityMetadataRepository;
import com.vts.clientcenter.service.EligibilityMetadataService;
import com.vts.clientcenter.service.dto.EligibilityMetadataDTO;
import com.vts.clientcenter.service.mapper.EligibilityMetadataMapper;
import com.vts.clientcenter.service.dto.EligibilityMetadataCriteria;
import com.vts.clientcenter.service.EligibilityMetadataQueryService;

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
 * Integration tests for the {@link EligibilityMetadataResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class EligibilityMetadataResourceIT {

    private static final String DEFAULT_THUMB_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMB_URL = "BBBBBBBBBB";

    @Autowired
    private EligibilityMetadataRepository eligibilityMetadataRepository;

    @Autowired
    private EligibilityMetadataMapper eligibilityMetadataMapper;

    @Autowired
    private EligibilityMetadataService eligibilityMetadataService;

    @Autowired
    private EligibilityMetadataQueryService eligibilityMetadataQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEligibilityMetadataMockMvc;

    private EligibilityMetadata eligibilityMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EligibilityMetadata createEntity(EntityManager em) {
        EligibilityMetadata eligibilityMetadata = new EligibilityMetadata()
            .thumbUrl(DEFAULT_THUMB_URL);
        return eligibilityMetadata;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EligibilityMetadata createUpdatedEntity(EntityManager em) {
        EligibilityMetadata eligibilityMetadata = new EligibilityMetadata()
            .thumbUrl(UPDATED_THUMB_URL);
        return eligibilityMetadata;
    }

    @BeforeEach
    public void initTest() {
        eligibilityMetadata = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllEligibilityMetadata() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        // Get all the eligibilityMetadataList
        restEligibilityMetadataMockMvc.perform(get("/api/eligibility-metadata?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibilityMetadata.getId())))
            .andExpect(jsonPath("$.[*].thumbUrl").value(hasItem(DEFAULT_THUMB_URL)));
    }

    @Test
    @Transactional
    public void getEligibilityMetadata() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        // Get the eligibilityMetadata
        restEligibilityMetadataMockMvc.perform(get("/api/eligibility-metadata/{id}", eligibilityMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eligibilityMetadata.getId()))
            .andExpect(jsonPath("$.thumbUrl").value(DEFAULT_THUMB_URL));
    }


    @Test
    @Transactional
    public void getEligibilityMetadataByIdFiltering() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        String id = eligibilityMetadata.getId();

        defaultEligibilityMetadataShouldBeFound("id.equals=" + id);
        defaultEligibilityMetadataShouldNotBeFound("id.notEquals=" + id);

        defaultEligibilityMetadataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEligibilityMetadataShouldNotBeFound("id.greaterThan=" + id);

        defaultEligibilityMetadataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEligibilityMetadataShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEligibilityMetadataByThumbUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        // Get all the eligibilityMetadataList where thumbUrl equals to DEFAULT_THUMB_URL
        defaultEligibilityMetadataShouldBeFound("thumbUrl.equals=" + DEFAULT_THUMB_URL);

        // Get all the eligibilityMetadataList where thumbUrl equals to UPDATED_THUMB_URL
        defaultEligibilityMetadataShouldNotBeFound("thumbUrl.equals=" + UPDATED_THUMB_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilityMetadataByThumbUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        // Get all the eligibilityMetadataList where thumbUrl not equals to DEFAULT_THUMB_URL
        defaultEligibilityMetadataShouldNotBeFound("thumbUrl.notEquals=" + DEFAULT_THUMB_URL);

        // Get all the eligibilityMetadataList where thumbUrl not equals to UPDATED_THUMB_URL
        defaultEligibilityMetadataShouldBeFound("thumbUrl.notEquals=" + UPDATED_THUMB_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilityMetadataByThumbUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        // Get all the eligibilityMetadataList where thumbUrl in DEFAULT_THUMB_URL or UPDATED_THUMB_URL
        defaultEligibilityMetadataShouldBeFound("thumbUrl.in=" + DEFAULT_THUMB_URL + "," + UPDATED_THUMB_URL);

        // Get all the eligibilityMetadataList where thumbUrl equals to UPDATED_THUMB_URL
        defaultEligibilityMetadataShouldNotBeFound("thumbUrl.in=" + UPDATED_THUMB_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilityMetadataByThumbUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        // Get all the eligibilityMetadataList where thumbUrl is not null
        defaultEligibilityMetadataShouldBeFound("thumbUrl.specified=true");

        // Get all the eligibilityMetadataList where thumbUrl is null
        defaultEligibilityMetadataShouldNotBeFound("thumbUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllEligibilityMetadataByThumbUrlContainsSomething() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        // Get all the eligibilityMetadataList where thumbUrl contains DEFAULT_THUMB_URL
        defaultEligibilityMetadataShouldBeFound("thumbUrl.contains=" + DEFAULT_THUMB_URL);

        // Get all the eligibilityMetadataList where thumbUrl contains UPDATED_THUMB_URL
        defaultEligibilityMetadataShouldNotBeFound("thumbUrl.contains=" + UPDATED_THUMB_URL);
    }

    @Test
    @Transactional
    public void getAllEligibilityMetadataByThumbUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eligibilityMetadataRepository.saveAndFlush(eligibilityMetadata);

        // Get all the eligibilityMetadataList where thumbUrl does not contain DEFAULT_THUMB_URL
        defaultEligibilityMetadataShouldNotBeFound("thumbUrl.doesNotContain=" + DEFAULT_THUMB_URL);

        // Get all the eligibilityMetadataList where thumbUrl does not contain UPDATED_THUMB_URL
        defaultEligibilityMetadataShouldBeFound("thumbUrl.doesNotContain=" + UPDATED_THUMB_URL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEligibilityMetadataShouldBeFound(String filter) throws Exception {
        restEligibilityMetadataMockMvc.perform(get("/api/eligibility-metadata?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibilityMetadata.getId())))
            .andExpect(jsonPath("$.[*].thumbUrl").value(hasItem(DEFAULT_THUMB_URL)));

        // Check, that the count call also returns 1
        restEligibilityMetadataMockMvc.perform(get("/api/eligibility-metadata/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEligibilityMetadataShouldNotBeFound(String filter) throws Exception {
        restEligibilityMetadataMockMvc.perform(get("/api/eligibility-metadata?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEligibilityMetadataMockMvc.perform(get("/api/eligibility-metadata/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEligibilityMetadata() throws Exception {
        // Get the eligibilityMetadata
        restEligibilityMetadataMockMvc.perform(get("/api/eligibility-metadata/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
