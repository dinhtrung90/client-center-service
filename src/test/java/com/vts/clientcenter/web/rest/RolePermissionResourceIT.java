package com.vts.clientcenter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.RolePermission;
import com.vts.clientcenter.repository.RolePermissionRepository;
import com.vts.clientcenter.service.RolePermissionQueryService;
import com.vts.clientcenter.service.RolePermissionService;
import com.vts.clientcenter.service.dto.RolePermissionCriteria;
import com.vts.clientcenter.service.dto.RolePermissionDTO;
import com.vts.clientcenter.service.mapper.RolePermissionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link RolePermissionResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class RolePermissionResourceIT {
    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE_CREATE = false;
    private static final Boolean UPDATED_ENABLE_CREATE = true;

    private static final Boolean DEFAULT_ENABLE_UPDATE = false;
    private static final Boolean UPDATED_ENABLE_UPDATE = true;

    private static final Boolean DEFAULT_ENABLE_READ = false;
    private static final Boolean UPDATED_ENABLE_READ = true;

    private static final Boolean DEFAULT_ENABLE_DELETE = false;
    private static final Boolean UPDATED_ENABLE_DELETE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RolePermissionQueryService rolePermissionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRolePermissionMockMvc;

    private RolePermission rolePermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolePermission createEntity(EntityManager em) {
        RolePermission rolePermission = new RolePermission()
            .roleName(DEFAULT_ROLE_NAME)
            .permission_id(DEFAULT_PERMISSION_ID)
            .enableCreate(DEFAULT_ENABLE_CREATE)
            .enableUpdate(DEFAULT_ENABLE_UPDATE)
            .enableRead(DEFAULT_ENABLE_READ)
            .enableDelete(DEFAULT_ENABLE_DELETE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return rolePermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolePermission createUpdatedEntity(EntityManager em) {
        RolePermission rolePermission = new RolePermission()
            .roleName(UPDATED_ROLE_NAME)
            .permission_id(UPDATED_PERMISSION_ID)
            .enableCreate(UPDATED_ENABLE_CREATE)
            .enableUpdate(UPDATED_ENABLE_UPDATE)
            .enableRead(UPDATED_ENABLE_READ)
            .enableDelete(UPDATED_ENABLE_DELETE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return rolePermission;
    }

    @BeforeEach
    public void initTest() {
        rolePermission = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllRolePermissions() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList
        restRolePermissionMockMvc
            .perform(get("/api/role-permissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolePermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)))
            .andExpect(jsonPath("$.[*].permission_id").value(hasItem(DEFAULT_PERMISSION_ID)))
            .andExpect(jsonPath("$.[*].enableCreate").value(hasItem(DEFAULT_ENABLE_CREATE.booleanValue())))
            .andExpect(jsonPath("$.[*].enableUpdate").value(hasItem(DEFAULT_ENABLE_UPDATE.booleanValue())))
            .andExpect(jsonPath("$.[*].enableRead").value(hasItem(DEFAULT_ENABLE_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].enableDelete").value(hasItem(DEFAULT_ENABLE_DELETE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    public void getRolePermission() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get the rolePermission
        restRolePermissionMockMvc
            .perform(get("/api/role-permissions/{id}", rolePermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rolePermission.getId().intValue()))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME))
            .andExpect(jsonPath("$.permission_id").value(DEFAULT_PERMISSION_ID))
            .andExpect(jsonPath("$.enableCreate").value(DEFAULT_ENABLE_CREATE.booleanValue()))
            .andExpect(jsonPath("$.enableUpdate").value(DEFAULT_ENABLE_UPDATE.booleanValue()))
            .andExpect(jsonPath("$.enableRead").value(DEFAULT_ENABLE_READ.booleanValue()))
            .andExpect(jsonPath("$.enableDelete").value(DEFAULT_ENABLE_DELETE.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    public void getRolePermissionsByIdFiltering() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        Long id = rolePermission.getId();

        defaultRolePermissionShouldBeFound("id.equals=" + id);
        defaultRolePermissionShouldNotBeFound("id.notEquals=" + id);

        defaultRolePermissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRolePermissionShouldNotBeFound("id.greaterThan=" + id);

        defaultRolePermissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRolePermissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByRoleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where roleName equals to DEFAULT_ROLE_NAME
        defaultRolePermissionShouldBeFound("roleName.equals=" + DEFAULT_ROLE_NAME);

        // Get all the rolePermissionList where roleName equals to UPDATED_ROLE_NAME
        defaultRolePermissionShouldNotBeFound("roleName.equals=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByRoleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where roleName not equals to DEFAULT_ROLE_NAME
        defaultRolePermissionShouldNotBeFound("roleName.notEquals=" + DEFAULT_ROLE_NAME);

        // Get all the rolePermissionList where roleName not equals to UPDATED_ROLE_NAME
        defaultRolePermissionShouldBeFound("roleName.notEquals=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByRoleNameIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where roleName in DEFAULT_ROLE_NAME or UPDATED_ROLE_NAME
        defaultRolePermissionShouldBeFound("roleName.in=" + DEFAULT_ROLE_NAME + "," + UPDATED_ROLE_NAME);

        // Get all the rolePermissionList where roleName equals to UPDATED_ROLE_NAME
        defaultRolePermissionShouldNotBeFound("roleName.in=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByRoleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where roleName is not null
        defaultRolePermissionShouldBeFound("roleName.specified=true");

        // Get all the rolePermissionList where roleName is null
        defaultRolePermissionShouldNotBeFound("roleName.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByRoleNameContainsSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where roleName contains DEFAULT_ROLE_NAME
        defaultRolePermissionShouldBeFound("roleName.contains=" + DEFAULT_ROLE_NAME);

        // Get all the rolePermissionList where roleName contains UPDATED_ROLE_NAME
        defaultRolePermissionShouldNotBeFound("roleName.contains=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByRoleNameNotContainsSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where roleName does not contain DEFAULT_ROLE_NAME
        defaultRolePermissionShouldNotBeFound("roleName.doesNotContain=" + DEFAULT_ROLE_NAME);

        // Get all the rolePermissionList where roleName does not contain UPDATED_ROLE_NAME
        defaultRolePermissionShouldBeFound("roleName.doesNotContain=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByPermission_idIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where permission_id equals to DEFAULT_PERMISSION_ID
        defaultRolePermissionShouldBeFound("permission_id.equals=" + DEFAULT_PERMISSION_ID);

        // Get all the rolePermissionList where permission_id equals to UPDATED_PERMISSION_ID
        defaultRolePermissionShouldNotBeFound("permission_id.equals=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByPermission_idIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where permission_id not equals to DEFAULT_PERMISSION_ID
        defaultRolePermissionShouldNotBeFound("permission_id.notEquals=" + DEFAULT_PERMISSION_ID);

        // Get all the rolePermissionList where permission_id not equals to UPDATED_PERMISSION_ID
        defaultRolePermissionShouldBeFound("permission_id.notEquals=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByPermission_idIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where permission_id in DEFAULT_PERMISSION_ID or UPDATED_PERMISSION_ID
        defaultRolePermissionShouldBeFound("permission_id.in=" + DEFAULT_PERMISSION_ID + "," + UPDATED_PERMISSION_ID);

        // Get all the rolePermissionList where permission_id equals to UPDATED_PERMISSION_ID
        defaultRolePermissionShouldNotBeFound("permission_id.in=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByPermission_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where permission_id is not null
        defaultRolePermissionShouldBeFound("permission_id.specified=true");

        // Get all the rolePermissionList where permission_id is null
        defaultRolePermissionShouldNotBeFound("permission_id.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByPermission_idContainsSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where permission_id contains DEFAULT_PERMISSION_ID
        defaultRolePermissionShouldBeFound("permission_id.contains=" + DEFAULT_PERMISSION_ID);

        // Get all the rolePermissionList where permission_id contains UPDATED_PERMISSION_ID
        defaultRolePermissionShouldNotBeFound("permission_id.contains=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByPermission_idNotContainsSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where permission_id does not contain DEFAULT_PERMISSION_ID
        defaultRolePermissionShouldNotBeFound("permission_id.doesNotContain=" + DEFAULT_PERMISSION_ID);

        // Get all the rolePermissionList where permission_id does not contain UPDATED_PERMISSION_ID
        defaultRolePermissionShouldBeFound("permission_id.doesNotContain=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableCreateIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableCreate equals to DEFAULT_ENABLE_CREATE
        defaultRolePermissionShouldBeFound("enableCreate.equals=" + DEFAULT_ENABLE_CREATE);

        // Get all the rolePermissionList where enableCreate equals to UPDATED_ENABLE_CREATE
        defaultRolePermissionShouldNotBeFound("enableCreate.equals=" + UPDATED_ENABLE_CREATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableCreateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableCreate not equals to DEFAULT_ENABLE_CREATE
        defaultRolePermissionShouldNotBeFound("enableCreate.notEquals=" + DEFAULT_ENABLE_CREATE);

        // Get all the rolePermissionList where enableCreate not equals to UPDATED_ENABLE_CREATE
        defaultRolePermissionShouldBeFound("enableCreate.notEquals=" + UPDATED_ENABLE_CREATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableCreateIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableCreate in DEFAULT_ENABLE_CREATE or UPDATED_ENABLE_CREATE
        defaultRolePermissionShouldBeFound("enableCreate.in=" + DEFAULT_ENABLE_CREATE + "," + UPDATED_ENABLE_CREATE);

        // Get all the rolePermissionList where enableCreate equals to UPDATED_ENABLE_CREATE
        defaultRolePermissionShouldNotBeFound("enableCreate.in=" + UPDATED_ENABLE_CREATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableCreateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableCreate is not null
        defaultRolePermissionShouldBeFound("enableCreate.specified=true");

        // Get all the rolePermissionList where enableCreate is null
        defaultRolePermissionShouldNotBeFound("enableCreate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableUpdate equals to DEFAULT_ENABLE_UPDATE
        defaultRolePermissionShouldBeFound("enableUpdate.equals=" + DEFAULT_ENABLE_UPDATE);

        // Get all the rolePermissionList where enableUpdate equals to UPDATED_ENABLE_UPDATE
        defaultRolePermissionShouldNotBeFound("enableUpdate.equals=" + UPDATED_ENABLE_UPDATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableUpdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableUpdate not equals to DEFAULT_ENABLE_UPDATE
        defaultRolePermissionShouldNotBeFound("enableUpdate.notEquals=" + DEFAULT_ENABLE_UPDATE);

        // Get all the rolePermissionList where enableUpdate not equals to UPDATED_ENABLE_UPDATE
        defaultRolePermissionShouldBeFound("enableUpdate.notEquals=" + UPDATED_ENABLE_UPDATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableUpdate in DEFAULT_ENABLE_UPDATE or UPDATED_ENABLE_UPDATE
        defaultRolePermissionShouldBeFound("enableUpdate.in=" + DEFAULT_ENABLE_UPDATE + "," + UPDATED_ENABLE_UPDATE);

        // Get all the rolePermissionList where enableUpdate equals to UPDATED_ENABLE_UPDATE
        defaultRolePermissionShouldNotBeFound("enableUpdate.in=" + UPDATED_ENABLE_UPDATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableUpdate is not null
        defaultRolePermissionShouldBeFound("enableUpdate.specified=true");

        // Get all the rolePermissionList where enableUpdate is null
        defaultRolePermissionShouldNotBeFound("enableUpdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableReadIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableRead equals to DEFAULT_ENABLE_READ
        defaultRolePermissionShouldBeFound("enableRead.equals=" + DEFAULT_ENABLE_READ);

        // Get all the rolePermissionList where enableRead equals to UPDATED_ENABLE_READ
        defaultRolePermissionShouldNotBeFound("enableRead.equals=" + UPDATED_ENABLE_READ);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableReadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableRead not equals to DEFAULT_ENABLE_READ
        defaultRolePermissionShouldNotBeFound("enableRead.notEquals=" + DEFAULT_ENABLE_READ);

        // Get all the rolePermissionList where enableRead not equals to UPDATED_ENABLE_READ
        defaultRolePermissionShouldBeFound("enableRead.notEquals=" + UPDATED_ENABLE_READ);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableReadIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableRead in DEFAULT_ENABLE_READ or UPDATED_ENABLE_READ
        defaultRolePermissionShouldBeFound("enableRead.in=" + DEFAULT_ENABLE_READ + "," + UPDATED_ENABLE_READ);

        // Get all the rolePermissionList where enableRead equals to UPDATED_ENABLE_READ
        defaultRolePermissionShouldNotBeFound("enableRead.in=" + UPDATED_ENABLE_READ);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableRead is not null
        defaultRolePermissionShouldBeFound("enableRead.specified=true");

        // Get all the rolePermissionList where enableRead is null
        defaultRolePermissionShouldNotBeFound("enableRead.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableDeleteIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableDelete equals to DEFAULT_ENABLE_DELETE
        defaultRolePermissionShouldBeFound("enableDelete.equals=" + DEFAULT_ENABLE_DELETE);

        // Get all the rolePermissionList where enableDelete equals to UPDATED_ENABLE_DELETE
        defaultRolePermissionShouldNotBeFound("enableDelete.equals=" + UPDATED_ENABLE_DELETE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableDeleteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableDelete not equals to DEFAULT_ENABLE_DELETE
        defaultRolePermissionShouldNotBeFound("enableDelete.notEquals=" + DEFAULT_ENABLE_DELETE);

        // Get all the rolePermissionList where enableDelete not equals to UPDATED_ENABLE_DELETE
        defaultRolePermissionShouldBeFound("enableDelete.notEquals=" + UPDATED_ENABLE_DELETE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableDeleteIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableDelete in DEFAULT_ENABLE_DELETE or UPDATED_ENABLE_DELETE
        defaultRolePermissionShouldBeFound("enableDelete.in=" + DEFAULT_ENABLE_DELETE + "," + UPDATED_ENABLE_DELETE);

        // Get all the rolePermissionList where enableDelete equals to UPDATED_ENABLE_DELETE
        defaultRolePermissionShouldNotBeFound("enableDelete.in=" + UPDATED_ENABLE_DELETE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByEnableDeleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where enableDelete is not null
        defaultRolePermissionShouldBeFound("enableDelete.specified=true");

        // Get all the rolePermissionList where enableDelete is null
        defaultRolePermissionShouldNotBeFound("enableDelete.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdDate equals to DEFAULT_CREATED_DATE
        defaultRolePermissionShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the rolePermissionList where createdDate equals to UPDATED_CREATED_DATE
        defaultRolePermissionShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultRolePermissionShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the rolePermissionList where createdDate not equals to UPDATED_CREATED_DATE
        defaultRolePermissionShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultRolePermissionShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the rolePermissionList where createdDate equals to UPDATED_CREATED_DATE
        defaultRolePermissionShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdDate is not null
        defaultRolePermissionShouldBeFound("createdDate.specified=true");

        // Get all the rolePermissionList where createdDate is null
        defaultRolePermissionShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultRolePermissionShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the rolePermissionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultRolePermissionShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultRolePermissionShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the rolePermissionList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultRolePermissionShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultRolePermissionShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the rolePermissionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultRolePermissionShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedDate is not null
        defaultRolePermissionShouldBeFound("lastModifiedDate.specified=true");

        // Get all the rolePermissionList where lastModifiedDate is null
        defaultRolePermissionShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdBy equals to DEFAULT_CREATED_BY
        defaultRolePermissionShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the rolePermissionList where createdBy equals to UPDATED_CREATED_BY
        defaultRolePermissionShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdBy not equals to DEFAULT_CREATED_BY
        defaultRolePermissionShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the rolePermissionList where createdBy not equals to UPDATED_CREATED_BY
        defaultRolePermissionShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultRolePermissionShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the rolePermissionList where createdBy equals to UPDATED_CREATED_BY
        defaultRolePermissionShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdBy is not null
        defaultRolePermissionShouldBeFound("createdBy.specified=true");

        // Get all the rolePermissionList where createdBy is null
        defaultRolePermissionShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdBy contains DEFAULT_CREATED_BY
        defaultRolePermissionShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the rolePermissionList where createdBy contains UPDATED_CREATED_BY
        defaultRolePermissionShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where createdBy does not contain DEFAULT_CREATED_BY
        defaultRolePermissionShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the rolePermissionList where createdBy does not contain UPDATED_CREATED_BY
        defaultRolePermissionShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultRolePermissionShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the rolePermissionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultRolePermissionShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultRolePermissionShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the rolePermissionList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultRolePermissionShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultRolePermissionShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the rolePermissionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultRolePermissionShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedBy is not null
        defaultRolePermissionShouldBeFound("lastModifiedBy.specified=true");

        // Get all the rolePermissionList where lastModifiedBy is null
        defaultRolePermissionShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultRolePermissionShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the rolePermissionList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultRolePermissionShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllRolePermissionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultRolePermissionShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the rolePermissionList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultRolePermissionShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRolePermissionShouldBeFound(String filter) throws Exception {
        restRolePermissionMockMvc
            .perform(get("/api/role-permissions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolePermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)))
            .andExpect(jsonPath("$.[*].permission_id").value(hasItem(DEFAULT_PERMISSION_ID)))
            .andExpect(jsonPath("$.[*].enableCreate").value(hasItem(DEFAULT_ENABLE_CREATE.booleanValue())))
            .andExpect(jsonPath("$.[*].enableUpdate").value(hasItem(DEFAULT_ENABLE_UPDATE.booleanValue())))
            .andExpect(jsonPath("$.[*].enableRead").value(hasItem(DEFAULT_ENABLE_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].enableDelete").value(hasItem(DEFAULT_ENABLE_DELETE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restRolePermissionMockMvc
            .perform(get("/api/role-permissions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRolePermissionShouldNotBeFound(String filter) throws Exception {
        restRolePermissionMockMvc
            .perform(get("/api/role-permissions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRolePermissionMockMvc
            .perform(get("/api/role-permissions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRolePermission() throws Exception {
        // Get the rolePermission
        restRolePermissionMockMvc.perform(get("/api/role-permissions/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
