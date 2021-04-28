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
import com.vts.clientcenter.web.rest.errors.ExceptionTranslator;
import com.vts.clientcenter.service.dto.EmployerCriteria;
import com.vts.clientcenter.service.EmployerQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.vts.clientcenter.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EmployerResource} REST controller.
 */
@SpringBootTest(classes = {ClientCenterServiceApp.class, TestSecurityConfiguration.class})
@ExtendWith(RedisTestContainerExtension.class)
public class EmployerResourceIT {

    private static final String DEFAULT_EMPLOYER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY = "BBBBBBBBBB";

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
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restEmployerMockMvc;

    private Employer employer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployerResource employerResource = new EmployerResource(employerService, employerQueryService);
        this.restEmployerMockMvc = MockMvcBuilders.standaloneSetup(employerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

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
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .street(DEFAULT_STREET)
            .city(DEFAULT_CITY)
            .county(DEFAULT_COUNTY)
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
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .county(UPDATED_COUNTY)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId().intValue())))
            .andExpect(jsonPath("$.[*].employerKey").value(hasItem(DEFAULT_EMPLOYER_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY)))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employer.getId().intValue()))
            .andExpect(jsonPath("$.employerKey").value(DEFAULT_EMPLOYER_KEY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.county").value(DEFAULT_COUNTY))
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
    public void getAllEmployersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where email equals to DEFAULT_EMAIL
        defaultEmployerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the employerList where email equals to UPDATED_EMAIL
        defaultEmployerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where email not equals to DEFAULT_EMAIL
        defaultEmployerShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the employerList where email not equals to UPDATED_EMAIL
        defaultEmployerShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmployerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the employerList where email equals to UPDATED_EMAIL
        defaultEmployerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where email is not null
        defaultEmployerShouldBeFound("email.specified=true");

        // Get all the employerList where email is null
        defaultEmployerShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByEmailContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where email contains DEFAULT_EMAIL
        defaultEmployerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the employerList where email contains UPDATED_EMAIL
        defaultEmployerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where email does not contain DEFAULT_EMAIL
        defaultEmployerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the employerList where email does not contain UPDATED_EMAIL
        defaultEmployerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllEmployersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where phone equals to DEFAULT_PHONE
        defaultEmployerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the employerList where phone equals to UPDATED_PHONE
        defaultEmployerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where phone not equals to DEFAULT_PHONE
        defaultEmployerShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the employerList where phone not equals to UPDATED_PHONE
        defaultEmployerShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultEmployerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the employerList where phone equals to UPDATED_PHONE
        defaultEmployerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where phone is not null
        defaultEmployerShouldBeFound("phone.specified=true");

        // Get all the employerList where phone is null
        defaultEmployerShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where phone contains DEFAULT_PHONE
        defaultEmployerShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the employerList where phone contains UPDATED_PHONE
        defaultEmployerShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where phone does not contain DEFAULT_PHONE
        defaultEmployerShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the employerList where phone does not contain UPDATED_PHONE
        defaultEmployerShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
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
    public void getAllEmployersByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where street equals to DEFAULT_STREET
        defaultEmployerShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the employerList where street equals to UPDATED_STREET
        defaultEmployerShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployersByStreetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where street not equals to DEFAULT_STREET
        defaultEmployerShouldNotBeFound("street.notEquals=" + DEFAULT_STREET);

        // Get all the employerList where street not equals to UPDATED_STREET
        defaultEmployerShouldBeFound("street.notEquals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployersByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where street in DEFAULT_STREET or UPDATED_STREET
        defaultEmployerShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the employerList where street equals to UPDATED_STREET
        defaultEmployerShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployersByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where street is not null
        defaultEmployerShouldBeFound("street.specified=true");

        // Get all the employerList where street is null
        defaultEmployerShouldNotBeFound("street.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByStreetContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where street contains DEFAULT_STREET
        defaultEmployerShouldBeFound("street.contains=" + DEFAULT_STREET);

        // Get all the employerList where street contains UPDATED_STREET
        defaultEmployerShouldNotBeFound("street.contains=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployersByStreetNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where street does not contain DEFAULT_STREET
        defaultEmployerShouldNotBeFound("street.doesNotContain=" + DEFAULT_STREET);

        // Get all the employerList where street does not contain UPDATED_STREET
        defaultEmployerShouldBeFound("street.doesNotContain=" + UPDATED_STREET);
    }


    @Test
    @Transactional
    public void getAllEmployersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where city equals to DEFAULT_CITY
        defaultEmployerShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the employerList where city equals to UPDATED_CITY
        defaultEmployerShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where city not equals to DEFAULT_CITY
        defaultEmployerShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the employerList where city not equals to UPDATED_CITY
        defaultEmployerShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where city in DEFAULT_CITY or UPDATED_CITY
        defaultEmployerShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the employerList where city equals to UPDATED_CITY
        defaultEmployerShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where city is not null
        defaultEmployerShouldBeFound("city.specified=true");

        // Get all the employerList where city is null
        defaultEmployerShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByCityContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where city contains DEFAULT_CITY
        defaultEmployerShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the employerList where city contains UPDATED_CITY
        defaultEmployerShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where city does not contain DEFAULT_CITY
        defaultEmployerShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the employerList where city does not contain UPDATED_CITY
        defaultEmployerShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllEmployersByCountyIsEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where county equals to DEFAULT_COUNTY
        defaultEmployerShouldBeFound("county.equals=" + DEFAULT_COUNTY);

        // Get all the employerList where county equals to UPDATED_COUNTY
        defaultEmployerShouldNotBeFound("county.equals=" + UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCountyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where county not equals to DEFAULT_COUNTY
        defaultEmployerShouldNotBeFound("county.notEquals=" + DEFAULT_COUNTY);

        // Get all the employerList where county not equals to UPDATED_COUNTY
        defaultEmployerShouldBeFound("county.notEquals=" + UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCountyIsInShouldWork() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where county in DEFAULT_COUNTY or UPDATED_COUNTY
        defaultEmployerShouldBeFound("county.in=" + DEFAULT_COUNTY + "," + UPDATED_COUNTY);

        // Get all the employerList where county equals to UPDATED_COUNTY
        defaultEmployerShouldNotBeFound("county.in=" + UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCountyIsNullOrNotNull() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where county is not null
        defaultEmployerShouldBeFound("county.specified=true");

        // Get all the employerList where county is null
        defaultEmployerShouldNotBeFound("county.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployersByCountyContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where county contains DEFAULT_COUNTY
        defaultEmployerShouldBeFound("county.contains=" + DEFAULT_COUNTY);

        // Get all the employerList where county contains UPDATED_COUNTY
        defaultEmployerShouldNotBeFound("county.contains=" + UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void getAllEmployersByCountyNotContainsSomething() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList where county does not contain DEFAULT_COUNTY
        defaultEmployerShouldNotBeFound("county.doesNotContain=" + DEFAULT_COUNTY);

        // Get all the employerList where county does not contain UPDATED_COUNTY
        defaultEmployerShouldBeFound("county.doesNotContain=" + UPDATED_COUNTY);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId().intValue())))
            .andExpect(jsonPath("$.[*].employerKey").value(hasItem(DEFAULT_EMPLOYER_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restEmployerMockMvc.perform(get("/api/employers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployerShouldNotBeFound(String filter) throws Exception {
        restEmployerMockMvc.perform(get("/api/employers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployerMockMvc.perform(get("/api/employers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
