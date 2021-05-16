package com.vts.clientcenter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.PermissionOperation;
import com.vts.clientcenter.repository.PermissionOperationRepository;
import com.vts.clientcenter.service.PermissionOperationQueryService;
import com.vts.clientcenter.service.PermissionOperationService;
import com.vts.clientcenter.service.dto.PermissionOperationCriteria;
import com.vts.clientcenter.service.dto.PermissionOperationDTO;
import com.vts.clientcenter.service.mapper.PermissionOperationMapper;
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
 * Integration tests for the {@link PermissionOperationResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class PermissionOperationResourceIT {
    private static final Long DEFAULT_ROLE_PERMISSION_ID = 1L;
    private static final Long UPDATED_ROLE_PERMISSION_ID = 2L;
    private static final Long SMALLER_ROLE_PERMISSION_ID = 1L - 1L;

    private static final Long DEFAULT_OPERATION_ID = 1L;
    private static final Long UPDATED_OPERATION_ID = 2L;
    private static final Long SMALLER_OPERATION_ID = 1L - 1L;

    @Autowired
    private PermissionOperationRepository permissionOperationRepository;

    @Autowired
    private PermissionOperationMapper permissionOperationMapper;

    @Autowired
    private PermissionOperationService permissionOperationService;

    @Autowired
    private PermissionOperationQueryService permissionOperationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionOperationMockMvc;

    private PermissionOperation permissionOperation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionOperation createEntity(EntityManager em) {
        PermissionOperation permissionOperation = new PermissionOperation()
            .rolePermissionId(DEFAULT_ROLE_PERMISSION_ID)
            .operationId(DEFAULT_OPERATION_ID);
        return permissionOperation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionOperation createUpdatedEntity(EntityManager em) {
        PermissionOperation permissionOperation = new PermissionOperation()
            .rolePermissionId(UPDATED_ROLE_PERMISSION_ID)
            .operationId(UPDATED_OPERATION_ID);
        return permissionOperation;
    }

    @BeforeEach
    public void initTest() {
        permissionOperation = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllPermissionOperations() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList
        restPermissionOperationMockMvc
            .perform(get("/api/permission-operations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].rolePermissionId").value(hasItem(DEFAULT_ROLE_PERMISSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].operationId").value(hasItem(DEFAULT_OPERATION_ID.intValue())));
    }

    @Test
    @Transactional
    public void getPermissionOperation() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get the permissionOperation
        restPermissionOperationMockMvc
            .perform(get("/api/permission-operations/{id}", permissionOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissionOperation.getId().intValue()))
            .andExpect(jsonPath("$.rolePermissionId").value(DEFAULT_ROLE_PERMISSION_ID.intValue()))
            .andExpect(jsonPath("$.operationId").value(DEFAULT_OPERATION_ID.intValue()));
    }

    @Test
    @Transactional
    public void getPermissionOperationsByIdFiltering() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        Long id = permissionOperation.getId();

        defaultPermissionOperationShouldBeFound("id.equals=" + id);
        defaultPermissionOperationShouldNotBeFound("id.notEquals=" + id);

        defaultPermissionOperationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPermissionOperationShouldNotBeFound("id.greaterThan=" + id);

        defaultPermissionOperationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPermissionOperationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByRolePermissionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where rolePermissionId equals to DEFAULT_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldBeFound("rolePermissionId.equals=" + DEFAULT_ROLE_PERMISSION_ID);

        // Get all the permissionOperationList where rolePermissionId equals to UPDATED_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldNotBeFound("rolePermissionId.equals=" + UPDATED_ROLE_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByRolePermissionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where rolePermissionId not equals to DEFAULT_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldNotBeFound("rolePermissionId.notEquals=" + DEFAULT_ROLE_PERMISSION_ID);

        // Get all the permissionOperationList where rolePermissionId not equals to UPDATED_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldBeFound("rolePermissionId.notEquals=" + UPDATED_ROLE_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByRolePermissionIdIsInShouldWork() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where rolePermissionId in DEFAULT_ROLE_PERMISSION_ID or UPDATED_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldBeFound("rolePermissionId.in=" + DEFAULT_ROLE_PERMISSION_ID + "," + UPDATED_ROLE_PERMISSION_ID);

        // Get all the permissionOperationList where rolePermissionId equals to UPDATED_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldNotBeFound("rolePermissionId.in=" + UPDATED_ROLE_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByRolePermissionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where rolePermissionId is not null
        defaultPermissionOperationShouldBeFound("rolePermissionId.specified=true");

        // Get all the permissionOperationList where rolePermissionId is null
        defaultPermissionOperationShouldNotBeFound("rolePermissionId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByRolePermissionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where rolePermissionId is greater than or equal to DEFAULT_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldBeFound("rolePermissionId.greaterThanOrEqual=" + DEFAULT_ROLE_PERMISSION_ID);

        // Get all the permissionOperationList where rolePermissionId is greater than or equal to UPDATED_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldNotBeFound("rolePermissionId.greaterThanOrEqual=" + UPDATED_ROLE_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByRolePermissionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where rolePermissionId is less than or equal to DEFAULT_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldBeFound("rolePermissionId.lessThanOrEqual=" + DEFAULT_ROLE_PERMISSION_ID);

        // Get all the permissionOperationList where rolePermissionId is less than or equal to SMALLER_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldNotBeFound("rolePermissionId.lessThanOrEqual=" + SMALLER_ROLE_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByRolePermissionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where rolePermissionId is less than DEFAULT_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldNotBeFound("rolePermissionId.lessThan=" + DEFAULT_ROLE_PERMISSION_ID);

        // Get all the permissionOperationList where rolePermissionId is less than UPDATED_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldBeFound("rolePermissionId.lessThan=" + UPDATED_ROLE_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByRolePermissionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where rolePermissionId is greater than DEFAULT_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldNotBeFound("rolePermissionId.greaterThan=" + DEFAULT_ROLE_PERMISSION_ID);

        // Get all the permissionOperationList where rolePermissionId is greater than SMALLER_ROLE_PERMISSION_ID
        defaultPermissionOperationShouldBeFound("rolePermissionId.greaterThan=" + SMALLER_ROLE_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByOperationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where operationId equals to DEFAULT_OPERATION_ID
        defaultPermissionOperationShouldBeFound("operationId.equals=" + DEFAULT_OPERATION_ID);

        // Get all the permissionOperationList where operationId equals to UPDATED_OPERATION_ID
        defaultPermissionOperationShouldNotBeFound("operationId.equals=" + UPDATED_OPERATION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByOperationIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where operationId not equals to DEFAULT_OPERATION_ID
        defaultPermissionOperationShouldNotBeFound("operationId.notEquals=" + DEFAULT_OPERATION_ID);

        // Get all the permissionOperationList where operationId not equals to UPDATED_OPERATION_ID
        defaultPermissionOperationShouldBeFound("operationId.notEquals=" + UPDATED_OPERATION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByOperationIdIsInShouldWork() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where operationId in DEFAULT_OPERATION_ID or UPDATED_OPERATION_ID
        defaultPermissionOperationShouldBeFound("operationId.in=" + DEFAULT_OPERATION_ID + "," + UPDATED_OPERATION_ID);

        // Get all the permissionOperationList where operationId equals to UPDATED_OPERATION_ID
        defaultPermissionOperationShouldNotBeFound("operationId.in=" + UPDATED_OPERATION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByOperationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where operationId is not null
        defaultPermissionOperationShouldBeFound("operationId.specified=true");

        // Get all the permissionOperationList where operationId is null
        defaultPermissionOperationShouldNotBeFound("operationId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByOperationIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where operationId is greater than or equal to DEFAULT_OPERATION_ID
        defaultPermissionOperationShouldBeFound("operationId.greaterThanOrEqual=" + DEFAULT_OPERATION_ID);

        // Get all the permissionOperationList where operationId is greater than or equal to UPDATED_OPERATION_ID
        defaultPermissionOperationShouldNotBeFound("operationId.greaterThanOrEqual=" + UPDATED_OPERATION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByOperationIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where operationId is less than or equal to DEFAULT_OPERATION_ID
        defaultPermissionOperationShouldBeFound("operationId.lessThanOrEqual=" + DEFAULT_OPERATION_ID);

        // Get all the permissionOperationList where operationId is less than or equal to SMALLER_OPERATION_ID
        defaultPermissionOperationShouldNotBeFound("operationId.lessThanOrEqual=" + SMALLER_OPERATION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByOperationIdIsLessThanSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where operationId is less than DEFAULT_OPERATION_ID
        defaultPermissionOperationShouldNotBeFound("operationId.lessThan=" + DEFAULT_OPERATION_ID);

        // Get all the permissionOperationList where operationId is less than UPDATED_OPERATION_ID
        defaultPermissionOperationShouldBeFound("operationId.lessThan=" + UPDATED_OPERATION_ID);
    }

    @Test
    @Transactional
    public void getAllPermissionOperationsByOperationIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        permissionOperationRepository.saveAndFlush(permissionOperation);

        // Get all the permissionOperationList where operationId is greater than DEFAULT_OPERATION_ID
        defaultPermissionOperationShouldNotBeFound("operationId.greaterThan=" + DEFAULT_OPERATION_ID);

        // Get all the permissionOperationList where operationId is greater than SMALLER_OPERATION_ID
        defaultPermissionOperationShouldBeFound("operationId.greaterThan=" + SMALLER_OPERATION_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermissionOperationShouldBeFound(String filter) throws Exception {
        restPermissionOperationMockMvc
            .perform(get("/api/permission-operations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].rolePermissionId").value(hasItem(DEFAULT_ROLE_PERMISSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].operationId").value(hasItem(DEFAULT_OPERATION_ID.intValue())));

        // Check, that the count call also returns 1
        restPermissionOperationMockMvc
            .perform(get("/api/permission-operations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermissionOperationShouldNotBeFound(String filter) throws Exception {
        restPermissionOperationMockMvc
            .perform(get("/api/permission-operations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermissionOperationMockMvc
            .perform(get("/api/permission-operations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPermissionOperation() throws Exception {
        // Get the permissionOperation
        restPermissionOperationMockMvc.perform(get("/api/permission-operations/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
