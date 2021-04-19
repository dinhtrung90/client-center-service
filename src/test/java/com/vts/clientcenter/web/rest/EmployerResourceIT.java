package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.Employer;
import com.vts.clientcenter.domain.EmployerDepartment;
import com.vts.clientcenter.domain.Employee;
import com.vts.clientcenter.domain.EmployerBrand;
import com.vts.clientcenter.repository.EmployerRepository;
import com.vts.clientcenter.service.EmployerService;
import com.vts.clientcenter.service.dto.EmployerDTO;
import com.vts.clientcenter.service.mapper.EmployerMapper;
import com.vts.clientcenter.service.dto.EmployerCriteria;
import com.vts.clientcenter.service.EmployerQueryService;

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
 * Integration tests for the {@link EmployerResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class EmployerResourceIT {

    private static final String DEFAULT_EMPLOYER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EmployerMapper employerMapper;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private EmployerQueryService employerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployerMockMvc;

    private Employer employer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employer createEntity(EntityManager em) {
        Employer employer = new Employer()
            .employerKey(DEFAULT_EMPLOYER_KEY)
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return employer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employer createUpdatedEntity(EntityManager em) {
        Employer employer = new Employer()
            .employerKey(UPDATED_EMPLOYER_KEY)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return employer;
    }

    @BeforeEach
    public void initTest() {
        employer = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllEmployers() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList
        restEmployerMockMvc.perform(get("/api/employers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId().intValue())))
            .andExpect(jsonPath("$.[*].employerKey").value(hasItem(DEFAULT_EMPLOYER_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get the employer
        restEmployerMockMvc.perform(get("/api/employers/{id}", employer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employer.getId().intValue()))
            .andExpect(jsonPath("$.employerKey").value(DEFAULT_EMPLOYER_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getEmployersByIdFiltering() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        Long id = employer.getId();

        defaultEmployerShouldBeFound("id.equals=" + id);
        defaultEmployerShouldNotBeFound("id.notEquals=" + id);

        defaultEmployerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployerShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmployersByEmployerKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where employerKey equals to DEFAULT_EMPLOYER_KEY
        defaultEmployerShouldBeFound("employerKey.equals=" + DEFAULT_EMPLOYER_KEY);

        // Get all the employerList where employerKey equals to UPDATED_EMPLOYER_KEY
        defaultEmployerShouldNotBeFound("employerKey.equals=" + UPDATED_EMPLOYER_KEY);
    }

    @Test
    @Transactional
    public void getAllEmployersByEmployerKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where employerKey not equals to DEFAULT_EMPLOYER_KEY
        defaultEmployerShouldNotBeFound("employerKey.notEquals=" + DEFAULT_EMPLOYER_KEY);

        // Get all the employerList where employerKey not equals to UPDATED_EMPLOYER_KEY
        defaultEmployerShouldBeFound("employerKey.notEquals=" + UPDATED_EMPLOYER_KEY);
    }

    @Test
    @Transactional
    public void getAllEmployersByEmployerKeyIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where employerKey in DEFAULT_EMPLOYER_KEY or UPDATED_EMPLOYER_KEY
        defaultEmployerShouldBeFound("employerKey.in=" + DEFAULT_EMPLOYER_KEY + "," + UPDATED_EMPLOYER_KEY);

        // Get all the employerList where employerKey equals to UPDATED_EMPLOYER_KEY
        defaultEmployerShouldNotBeFound("employerKey.in=" + UPDATED_EMPLOYER_KEY);
    }

    @Test
    @Transactional
    public void getAllEmployersByEmployerKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where employerKey is not null
        defaultEmployerShouldBeFound("employerKey.specified=true");

        // Get all the employerList where employerKey is null
        defaultEmployerShouldNotBeFound("employerKey.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByEmployerKeyContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where employerKey contains DEFAULT_EMPLOYER_KEY
        defaultEmployerShouldBeFound("employerKey.contains=" + DEFAULT_EMPLOYER_KEY);

        // Get all the employerList where employerKey contains UPDATED_EMPLOYER_KEY
        defaultEmployerShouldNotBeFound("employerKey.contains=" + UPDATED_EMPLOYER_KEY);
    }

    @Test
    @Transactional
    public void getAllEmployersByEmployerKeyNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where employerKey does not contain DEFAULT_EMPLOYER_KEY
        defaultEmployerShouldNotBeFound("employerKey.doesNotContain=" + DEFAULT_EMPLOYER_KEY);

        // Get all the employerList where employerKey does not contain UPDATED_EMPLOYER_KEY
        defaultEmployerShouldBeFound("employerKey.doesNotContain=" + UPDATED_EMPLOYER_KEY);
    }


    @Test
    @Transactional
    public void getAllEmployersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where name equals to DEFAULT_NAME
        defaultEmployerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the employerList where name equals to UPDATED_NAME
        defaultEmployerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where name not equals to DEFAULT_NAME
        defaultEmployerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the employerList where name not equals to UPDATED_NAME
        defaultEmployerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEmployerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the employerList where name equals to UPDATED_NAME
        defaultEmployerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where name is not null
        defaultEmployerShouldBeFound("name.specified=true");

        // Get all the employerList where name is null
        defaultEmployerShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByNameContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where name contains DEFAULT_NAME
        defaultEmployerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the employerList where name contains UPDATED_NAME
        defaultEmployerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where name does not contain DEFAULT_NAME
        defaultEmployerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the employerList where name does not contain UPDATED_NAME
        defaultEmployerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllEmployersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where address equals to DEFAULT_ADDRESS
        defaultEmployerShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the employerList where address equals to UPDATED_ADDRESS
        defaultEmployerShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where address not equals to DEFAULT_ADDRESS
        defaultEmployerShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the employerList where address not equals to UPDATED_ADDRESS
        defaultEmployerShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultEmployerShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the employerList where address equals to UPDATED_ADDRESS
        defaultEmployerShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where address is not null
        defaultEmployerShouldBeFound("address.specified=true");

        // Get all the employerList where address is null
        defaultEmployerShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByAddressContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where address contains DEFAULT_ADDRESS
        defaultEmployerShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the employerList where address contains UPDATED_ADDRESS
        defaultEmployerShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where address does not contain DEFAULT_ADDRESS
        defaultEmployerShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the employerList where address does not contain UPDATED_ADDRESS
        defaultEmployerShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllEmployersByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where longitude equals to DEFAULT_LONGITUDE
        defaultEmployerShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the employerList where longitude equals to UPDATED_LONGITUDE
        defaultEmployerShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where longitude not equals to DEFAULT_LONGITUDE
        defaultEmployerShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the employerList where longitude not equals to UPDATED_LONGITUDE
        defaultEmployerShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultEmployerShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the employerList where longitude equals to UPDATED_LONGITUDE
        defaultEmployerShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where longitude is not null
        defaultEmployerShouldBeFound("longitude.specified=true");

        // Get all the employerList where longitude is null
        defaultEmployerShouldNotBeFound("longitude.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where longitude contains DEFAULT_LONGITUDE
        defaultEmployerShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the employerList where longitude contains UPDATED_LONGITUDE
        defaultEmployerShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where longitude does not contain DEFAULT_LONGITUDE
        defaultEmployerShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the employerList where longitude does not contain UPDATED_LONGITUDE
        defaultEmployerShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }


    @Test
    @Transactional
    public void getAllEmployersByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where latitude equals to DEFAULT_LATITUDE
        defaultEmployerShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the employerList where latitude equals to UPDATED_LATITUDE
        defaultEmployerShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where latitude not equals to DEFAULT_LATITUDE
        defaultEmployerShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the employerList where latitude not equals to UPDATED_LATITUDE
        defaultEmployerShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultEmployerShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the employerList where latitude equals to UPDATED_LATITUDE
        defaultEmployerShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where latitude is not null
        defaultEmployerShouldBeFound("latitude.specified=true");

        // Get all the employerList where latitude is null
        defaultEmployerShouldNotBeFound("latitude.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where latitude contains DEFAULT_LATITUDE
        defaultEmployerShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the employerList where latitude contains UPDATED_LATITUDE
        defaultEmployerShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where latitude does not contain DEFAULT_LATITUDE
        defaultEmployerShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the employerList where latitude does not contain UPDATED_LATITUDE
        defaultEmployerShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllEmployersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdDate equals to DEFAULT_CREATED_DATE
        defaultEmployerShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the employerList where createdDate equals to UPDATED_CREATED_DATE
        defaultEmployerShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployersByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultEmployerShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the employerList where createdDate not equals to UPDATED_CREATED_DATE
        defaultEmployerShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultEmployerShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the employerList where createdDate equals to UPDATED_CREATED_DATE
        defaultEmployerShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdDate is not null
        defaultEmployerShouldBeFound("createdDate.specified=true");

        // Get all the employerList where createdDate is null
        defaultEmployerShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultEmployerShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the employerList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultEmployerShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultEmployerShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the employerList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultEmployerShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultEmployerShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the employerList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultEmployerShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedDate is not null
        defaultEmployerShouldBeFound("lastModifiedDate.specified=true");

        // Get all the employerList where lastModifiedDate is null
        defaultEmployerShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdBy equals to DEFAULT_CREATED_BY
        defaultEmployerShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the employerList where createdBy equals to UPDATED_CREATED_BY
        defaultEmployerShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdBy not equals to DEFAULT_CREATED_BY
        defaultEmployerShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the employerList where createdBy not equals to UPDATED_CREATED_BY
        defaultEmployerShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultEmployerShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the employerList where createdBy equals to UPDATED_CREATED_BY
        defaultEmployerShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdBy is not null
        defaultEmployerShouldBeFound("createdBy.specified=true");

        // Get all the employerList where createdBy is null
        defaultEmployerShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdBy contains DEFAULT_CREATED_BY
        defaultEmployerShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the employerList where createdBy contains UPDATED_CREATED_BY
        defaultEmployerShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where createdBy does not contain DEFAULT_CREATED_BY
        defaultEmployerShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the employerList where createdBy does not contain UPDATED_CREATED_BY
        defaultEmployerShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllEmployersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultEmployerShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployersByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultEmployerShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultEmployerShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the employerList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEmployerShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedBy is not null
        defaultEmployerShouldBeFound("lastModifiedBy.specified=true");

        // Get all the employerList where lastModifiedBy is null
        defaultEmployerShouldNotBeFound("lastModifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultEmployerShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultEmployerShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultEmployerShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the employerList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultEmployerShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllEmployersByEmployerDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);
        EmployerDepartment employerDepartment = EmployerDepartmentResourceIT.createEntity(em);
        em.persist(employerDepartment);
        em.flush();
        employer.addEmployerDepartment(employerDepartment);
        employerRepository.saveAndFlush(employer);
        Long employerDepartmentId = employerDepartment.getId();

        // Get all the employerList where employerDepartment equals to employerDepartmentId
        defaultEmployerShouldBeFound("employerDepartmentId.equals=" + employerDepartmentId);

        // Get all the employerList where employerDepartment equals to employerDepartmentId + 1
        defaultEmployerShouldNotBeFound("employerDepartmentId.equals=" + (employerDepartmentId + 1));
    }


    @Test
    @Transactional
    public void getAllEmployersByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        employer.addEmployee(employee);
        employerRepository.saveAndFlush(employer);
        Long employeeId = employee.getId();

        // Get all the employerList where employee equals to employeeId
        defaultEmployerShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the employerList where employee equals to employeeId + 1
        defaultEmployerShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }


    @Test
    @Transactional
    public void getAllEmployersByEmployerBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);
        EmployerBrand employerBrand = EmployerBrandResourceIT.createEntity(em);
        em.persist(employerBrand);
        em.flush();
        employer.addEmployerBrand(employerBrand);
        employerRepository.saveAndFlush(employer);
        Long employerBrandId = employerBrand.getId();

        // Get all the employerList where employerBrand equals to employerBrandId
        defaultEmployerShouldBeFound("employerBrandId.equals=" + employerBrandId);

        // Get all the employerList where employerBrand equals to employerBrandId + 1
        defaultEmployerShouldNotBeFound("employerBrandId.equals=" + (employerBrandId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployerShouldBeFound(String filter) throws Exception {
        restEmployerMockMvc.perform(get("/api/employers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId().intValue())))
            .andExpect(jsonPath("$.[*].employerKey").value(hasItem(DEFAULT_EMPLOYER_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restEmployerMockMvc.perform(get("/api/employers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployerShouldNotBeFound(String filter) throws Exception {
        restEmployerMockMvc.perform(get("/api/employers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployerMockMvc.perform(get("/api/employers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEmployer() throws Exception {
        // Get the employer
        restEmployerMockMvc.perform(get("/api/employers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
