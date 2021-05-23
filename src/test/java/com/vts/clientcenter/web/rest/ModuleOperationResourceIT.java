package com.vts.clientcenter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.ModuleOperation;
import com.vts.clientcenter.domain.enumeration.OperationEnum;
import com.vts.clientcenter.repository.ModuleOperationRepository;
import com.vts.clientcenter.service.ModuleOperationQueryService;
import com.vts.clientcenter.service.ModuleOperationService;
import com.vts.clientcenter.service.dto.ModuleOperationCriteria;
import com.vts.clientcenter.service.dto.ModuleOperationDTO;
import com.vts.clientcenter.service.mapper.ModuleOperationMapper;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ModuleOperationResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class ModuleOperationResourceIT {
    private static final OperationEnum DEFAULT_NAME = OperationEnum.CREATE;
    private static final OperationEnum UPDATED_NAME = OperationEnum.UPDATE;

    @Autowired
    private ModuleOperationRepository moduleOperationRepository;

    @Autowired
    private ModuleOperationMapper moduleOperationMapper;

    @Autowired
    private ModuleOperationService moduleOperationService;

    @Autowired
    private ModuleOperationQueryService moduleOperationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModuleOperationMockMvc;

    private ModuleOperation moduleOperation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModuleOperation createEntity(EntityManager em) {
        ModuleOperation moduleOperation = ModuleOperation.builder().name(DEFAULT_NAME).build();
        return moduleOperation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModuleOperation createUpdatedEntity(EntityManager em) {
        ModuleOperation moduleOperation = ModuleOperation.builder().name(DEFAULT_NAME).build();
        return moduleOperation;
    }

    @BeforeEach
    public void initTest() {
        moduleOperation = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllModuleOperations() throws Exception {
        // Initialize the database
        moduleOperationRepository.saveAndFlush(moduleOperation);

        // Get all the moduleOperationList
        restModuleOperationMockMvc
            .perform(get("/api/module-operations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moduleOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getModuleOperation() throws Exception {
        // Initialize the database
        moduleOperationRepository.saveAndFlush(moduleOperation);

        // Get the moduleOperation
        restModuleOperationMockMvc
            .perform(get("/api/module-operations/{id}", moduleOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moduleOperation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getModuleOperationsByIdFiltering() throws Exception {
        // Initialize the database
        moduleOperationRepository.saveAndFlush(moduleOperation);

        Long id = moduleOperation.getId();

        defaultModuleOperationShouldBeFound("id.equals=" + id);
        defaultModuleOperationShouldNotBeFound("id.notEquals=" + id);

        defaultModuleOperationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultModuleOperationShouldNotBeFound("id.greaterThan=" + id);

        defaultModuleOperationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultModuleOperationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllModuleOperationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        moduleOperationRepository.saveAndFlush(moduleOperation);

        // Get all the moduleOperationList where name equals to DEFAULT_NAME
        defaultModuleOperationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the moduleOperationList where name equals to UPDATED_NAME
        defaultModuleOperationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllModuleOperationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        moduleOperationRepository.saveAndFlush(moduleOperation);

        // Get all the moduleOperationList where name not equals to DEFAULT_NAME
        defaultModuleOperationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the moduleOperationList where name not equals to UPDATED_NAME
        defaultModuleOperationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllModuleOperationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        moduleOperationRepository.saveAndFlush(moduleOperation);

        // Get all the moduleOperationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultModuleOperationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the moduleOperationList where name equals to UPDATED_NAME
        defaultModuleOperationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllModuleOperationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        moduleOperationRepository.saveAndFlush(moduleOperation);

        // Get all the moduleOperationList where name is not null
        defaultModuleOperationShouldBeFound("name.specified=true");

        // Get all the moduleOperationList where name is null
        defaultModuleOperationShouldNotBeFound("name.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultModuleOperationShouldBeFound(String filter) throws Exception {
        restModuleOperationMockMvc
            .perform(get("/api/module-operations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moduleOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));

        // Check, that the count call also returns 1
        restModuleOperationMockMvc
            .perform(get("/api/module-operations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultModuleOperationShouldNotBeFound(String filter) throws Exception {
        restModuleOperationMockMvc
            .perform(get("/api/module-operations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restModuleOperationMockMvc
            .perform(get("/api/module-operations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingModuleOperation() throws Exception {
        // Get the moduleOperation
        restModuleOperationMockMvc.perform(get("/api/module-operations/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
