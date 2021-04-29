package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.EmployerDepartment;
import com.vts.clientcenter.domain.Employer;
import com.vts.clientcenter.repository.EmployerDepartmentRepository;
import com.vts.clientcenter.service.EmployerDepartmentService;
import com.vts.clientcenter.service.dto.EmployerDepartmentDTO;
import com.vts.clientcenter.service.mapper.EmployerDepartmentMapper;
import com.vts.clientcenter.service.dto.EmployerDepartmentCriteria;
import com.vts.clientcenter.service.EmployerDepartmentQueryService;

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
 * Integration tests for the {@link EmployerDepartmentResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class EmployerDepartmentResourceIT {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private EmployerDepartmentRepository employerDepartmentRepository;

    @Autowired
    private EmployerDepartmentMapper employerDepartmentMapper;

    @Autowired
    private EmployerDepartmentService employerDepartmentService;

    @Autowired
    private EmployerDepartmentQueryService employerDepartmentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployerDepartmentMockMvc;

    private EmployerDepartment employerDepartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployerDepartment createEntity(EntityManager em) {
        EmployerDepartment employerDepartment = new EmployerDepartment()
            .departmentName(DEFAULT_DEPARTMENT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lstModifiedDate(DEFAULT_LST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return employerDepartment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployerDepartment createUpdatedEntity(EntityManager em) {
        EmployerDepartment employerDepartment = new EmployerDepartment()
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lstModifiedDate(UPDATED_LST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return employerDepartment;
    }

    @BeforeEach
    public void initTest() {
        employerDepartment = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartments() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList
        restEmployerDepartmentMockMvc.perform(get("/api/employer-departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employerDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lstModifiedDate").value(hasItem(DEFAULT_LST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getEmployerDepartment() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get the employerDepartment
        restEmployerDepartmentMockMvc.perform(get("/api/employer-departments/{id}", employerDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employerDepartment.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lstModifiedDate").value(DEFAULT_LST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getEmployerDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        Long id = employerDepartment.getId();

        defaultEmployerDepartmentShouldBeFound("id.equals=" + id);
        defaultEmployerDepartmentShouldNotBeFound("id.notEquals=" + id);

        defaultEmployerDepartmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployerDepartmentShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployerDepartmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployerDepartmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmployerDepartmentsByDepartmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where departmentName equals to DEFAULT_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldBeFound("departmentName.equals=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the employerDepartmentList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldNotBeFound("departmentName.equals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByDepartmentNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where departmentName not equals to DEFAULT_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldNotBeFound("departmentName.notEquals=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the employerDepartmentList where departmentName not equals to UPDATED_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldBeFound("departmentName.notEquals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByDepartmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where departmentName in DEFAULT_DEPARTMENT_NAME or UPDATED_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldBeFound("departmentName.in=" + DEFAULT_DEPARTMENT_NAME + "," + UPDATED_DEPARTMENT_NAME);

        // Get all the employerDepartmentList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldNotBeFound("departmentName.in=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByDepartmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where departmentName is not null
        defaultEmployerDepartmentShouldBeFound("departmentName.specified=true");

        // Get all the employerDepartmentList where departmentName is null
        defaultEmployerDepartmentShouldNotBeFound("departmentName.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployerDepartmentsByDepartmentNameContainsSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where departmentName contains DEFAULT_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldBeFound("departmentName.contains=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the employerDepartmentList where departmentName contains UPDATED_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldNotBeFound("departmentName.contains=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByDepartmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where departmentName does not contain DEFAULT_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldNotBeFound("departmentName.doesNotContain=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the employerDepartmentList where departmentName does not contain UPDATED_DEPARTMENT_NAME
        defaultEmployerDepartmentShouldBeFound("departmentName.doesNotContain=" + UPDATED_DEPARTMENT_NAME);
    }


    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdDate equals to DEFAULT_CREATED_DATE
        defaultEmployerDepartmentShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the employerDepartmentList where createdDate equals to UPDATED_CREATED_DATE
        defaultEmployerDepartmentShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultEmployerDepartmentShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the employerDepartmentList where createdDate not equals to UPDATED_CREATED_DATE
        defaultEmployerDepartmentShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultEmployerDepartmentShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the employerDepartmentList where createdDate equals to UPDATED_CREATED_DATE
        defaultEmployerDepartmentShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdDate is not null
        defaultEmployerDepartmentShouldBeFound("createdDate.specified=true");

        // Get all the employerDepartmentList where createdDate is null
        defaultEmployerDepartmentShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLstModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lstModifiedDate equals to DEFAULT_LST_MODIFIED_DATE
        defaultEmployerDepartmentShouldBeFound("lstModifiedDate.equals=" + DEFAULT_LST_MODIFIED_DATE);

        // Get all the employerDepartmentList where lstModifiedDate equals to UPDATED_LST_MODIFIED_DATE
        defaultEmployerDepartmentShouldNotBeFound("lstModifiedDate.equals=" + UPDATED_LST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLstModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lstModifiedDate not equals to DEFAULT_LST_MODIFIED_DATE
        defaultEmployerDepartmentShouldNotBeFound("lstModifiedDate.notEquals=" + DEFAULT_LST_MODIFIED_DATE);

        // Get all the employerDepartmentList where lstModifiedDate not equals to UPDATED_LST_MODIFIED_DATE
        defaultEmployerDepartmentShouldBeFound("lstModifiedDate.notEquals=" + UPDATED_LST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLstModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lstModifiedDate in DEFAULT_LST_MODIFIED_DATE or UPDATED_LST_MODIFIED_DATE
        defaultEmployerDepartmentShouldBeFound("lstModifiedDate.in=" + DEFAULT_LST_MODIFIED_DATE + "," + UPDATED_LST_MODIFIED_DATE);

        // Get all the employerDepartmentList where lstModifiedDate equals to UPDATED_LST_MODIFIED_DATE
        defaultEmployerDepartmentShouldNotBeFound("lstModifiedDate.in=" + UPDATED_LST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLstModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lstModifiedDate is not null
        defaultEmployerDepartmentShouldBeFound("lstModifiedDate.specified=true");

        // Get all the employerDepartmentList where lstModifiedDate is null
        defaultEmployerDepartmentShouldNotBeFound("lstModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdBy equals to DEFAULT_CREATED_BY
        defaultEmployerDepartmentShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the employerDepartmentList where createdBy equals to UPDATED_CREATED_BY
        defaultEmployerDepartmentShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdBy not equals to DEFAULT_CREATED_BY
        defaultEmployerDepartmentShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the employerDepartmentList where createdBy not equals to UPDATED_CREATED_BY
        defaultEmployerDepartmentShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultEmployerDepartmentShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the employerDepartmentList where createdBy equals to UPDATED_CREATED_BY
        defaultEmployerDepartmentShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdBy is not null
        defaultEmployerDepartmentShouldBeFound("createdBy.specified=true");

        // Get all the employerDepartmentList where createdBy is null
        defaultEmployerDepartmentShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdBy contains DEFAULT_CREATED_BY
        defaultEmployerDepartmentShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the employerDepartmentList where createdBy contains UPDATED_CREATED_BY
        defaultEmployerDepartmentShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where createdBy does not contain DEFAULT_CREATED_BY
        defaultEmployerDepartmentShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the employerDepartmentList where createdBy does not contain UPDATED_CREATED_BY
        defaultEmployerDepartmentShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerDepartmentList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerDepartmentList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the employerDepartmentList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lastModifiedBy is not null
        defaultEmployerDepartmentShouldBeFound("lastModifiedBy.specified=true");

        // Get all the employerDepartmentList where lastModifiedBy is null
        defaultEmployerDepartmentShouldNotBeFound("lastModifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployerDepartmentsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerDepartmentList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployerDepartmentsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);

        // Get all the employerDepartmentList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerDepartmentList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultEmployerDepartmentShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllEmployerDepartmentsByEmployerIsEqualToSomething() throws Exception {
        // Initialize the database
        employerDepartmentRepository.saveAndFlush(employerDepartment);
        Employer employer = EmployerResourceIT.createEntity(em);
        em.persist(employer);
        em.flush();
        employerDepartment.setEmployer(employer);
        employerDepartmentRepository.saveAndFlush(employerDepartment);
        Long employerId = employer.getId();

        // Get all the employerDepartmentList where employer equals to employerId
        defaultEmployerDepartmentShouldBeFound("employerId.equals=" + employerId);

        // Get all the employerDepartmentList where employer equals to employerId + 1
        defaultEmployerDepartmentShouldNotBeFound("employerId.equals=" + (employerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployerDepartmentShouldBeFound(String filter) throws Exception {
        restEmployerDepartmentMockMvc.perform(get("/api/employer-departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employerDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lstModifiedDate").value(hasItem(DEFAULT_LST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restEmployerDepartmentMockMvc.perform(get("/api/employer-departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployerDepartmentShouldNotBeFound(String filter) throws Exception {
        restEmployerDepartmentMockMvc.perform(get("/api/employer-departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployerDepartmentMockMvc.perform(get("/api/employer-departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEmployerDepartment() throws Exception {
        // Get the employerDepartment
        restEmployerDepartmentMockMvc.perform(get("/api/employer-departments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
