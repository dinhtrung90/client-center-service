package com.vts.clientcenter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.Permission;
import com.vts.clientcenter.repository.PermissionRepository;
import com.vts.clientcenter.service.PermissionQueryService;
import com.vts.clientcenter.service.PermissionService;
import com.vts.clientcenter.service.dto.PermissionCriteria;
import com.vts.clientcenter.service.dto.PermissionDTO;
import com.vts.clientcenter.service.mapper.PermissionMapper;
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
 * Integration tests for the {@link PermissionResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class PermissionResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionQueryService permissionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionMockMvc;

    private Permission permission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createEntity(EntityManager em) {
        Permission permission = new Permission()
            .name(DEFAULT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return permission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createUpdatedEntity(EntityManager em) {
        Permission permission = new Permission()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return permission;
    }

    @BeforeEach
    public void initTest() {
        permission = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllPermissions() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList
        restPermissionMockMvc
            .perform(get("/api/permissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    public void getPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get the permission
        restPermissionMockMvc
            .perform(get("/api/permissions/{id}", permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permission.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    public void getPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        Long id = permission.getId();

        defaultPermissionShouldBeFound("id.equals=" + id);
        defaultPermissionShouldNotBeFound("id.notEquals=" + id);

        defaultPermissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPermissionShouldNotBeFound("id.greaterThan=" + id);

        defaultPermissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPermissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllPermissionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where name equals to DEFAULT_NAME
        defaultPermissionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the permissionList where name equals to UPDATED_NAME
        defaultPermissionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPermissionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where name not equals to DEFAULT_NAME
        defaultPermissionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the permissionList where name not equals to UPDATED_NAME
        defaultPermissionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPermissionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPermissionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the permissionList where name equals to UPDATED_NAME
        defaultPermissionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPermissionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where name is not null
        defaultPermissionShouldBeFound("name.specified=true");

        // Get all the permissionList where name is null
        defaultPermissionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPermissionsByNameContainsSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where name contains DEFAULT_NAME
        defaultPermissionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the permissionList where name contains UPDATED_NAME
        defaultPermissionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPermissionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where name does not contain DEFAULT_NAME
        defaultPermissionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the permissionList where name does not contain UPDATED_NAME
        defaultPermissionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdDate equals to DEFAULT_CREATED_DATE
        defaultPermissionShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the permissionList where createdDate equals to UPDATED_CREATED_DATE
        defaultPermissionShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultPermissionShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the permissionList where createdDate not equals to UPDATED_CREATED_DATE
        defaultPermissionShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultPermissionShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the permissionList where createdDate equals to UPDATED_CREATED_DATE
        defaultPermissionShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdDate is not null
        defaultPermissionShouldBeFound("createdDate.specified=true");

        // Get all the permissionList where createdDate is null
        defaultPermissionShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultPermissionShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the permissionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPermissionShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultPermissionShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the permissionList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultPermissionShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultPermissionShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the permissionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPermissionShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedDate is not null
        defaultPermissionShouldBeFound("lastModifiedDate.specified=true");

        // Get all the permissionList where lastModifiedDate is null
        defaultPermissionShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdBy equals to DEFAULT_CREATED_BY
        defaultPermissionShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the permissionList where createdBy equals to UPDATED_CREATED_BY
        defaultPermissionShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdBy not equals to DEFAULT_CREATED_BY
        defaultPermissionShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the permissionList where createdBy not equals to UPDATED_CREATED_BY
        defaultPermissionShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultPermissionShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the permissionList where createdBy equals to UPDATED_CREATED_BY
        defaultPermissionShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdBy is not null
        defaultPermissionShouldBeFound("createdBy.specified=true");

        // Get all the permissionList where createdBy is null
        defaultPermissionShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdBy contains DEFAULT_CREATED_BY
        defaultPermissionShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the permissionList where createdBy contains UPDATED_CREATED_BY
        defaultPermissionShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where createdBy does not contain DEFAULT_CREATED_BY
        defaultPermissionShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the permissionList where createdBy does not contain UPDATED_CREATED_BY
        defaultPermissionShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPermissionShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the permissionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPermissionShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultPermissionShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the permissionList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultPermissionShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPermissionShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the permissionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPermissionShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedBy is not null
        defaultPermissionShouldBeFound("lastModifiedBy.specified=true");

        // Get all the permissionList where lastModifiedBy is null
        defaultPermissionShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultPermissionShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the permissionList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultPermissionShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPermissionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultPermissionShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the permissionList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultPermissionShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermissionShouldBeFound(String filter) throws Exception {
        restPermissionMockMvc
            .perform(get("/api/permissions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restPermissionMockMvc
            .perform(get("/api/permissions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermissionShouldNotBeFound(String filter) throws Exception {
        restPermissionMockMvc
            .perform(get("/api/permissions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermissionMockMvc
            .perform(get("/api/permissions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPermission() throws Exception {
        // Get the permission
        restPermissionMockMvc.perform(get("/api/permissions/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
