package com.vts.clientcenter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.Employee;
import com.vts.clientcenter.repository.EmployeeRepository;
import com.vts.clientcenter.service.EmployeeQueryService;
import com.vts.clientcenter.service.EmployeeService;
import com.vts.clientcenter.service.dto.EmployeeCriteria;
import com.vts.clientcenter.service.dto.EmployeeDTO;
import com.vts.clientcenter.service.mapper.EmployeeMapper;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class EmployeeResourceIT {
    private static final String DEFAULT_EMPLOYEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_INITIAL = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_INITIAL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP = "AAAAAAAAAA";
    private static final String UPDATED_ZIP = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_SOCIAL_SECURITY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_SECURITY_NUMBER = "BBBBBBBBBB";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeQueryService employeeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .sourceId(DEFAULT_SOURCE_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .middleInitial(DEFAULT_MIDDLE_INITIAL)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .numberPhone(DEFAULT_NUMBER_PHONE)
            .street(DEFAULT_STREET)
            .city(DEFAULT_CITY)
            .stateCode(DEFAULT_STATE_CODE)
            .zip(DEFAULT_ZIP)
            .birthDate(DEFAULT_BIRTH_DATE)
            .department(DEFAULT_DEPARTMENT)
            .socialSecurityNumber(DEFAULT_SOCIAL_SECURITY_NUMBER);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeId(UPDATED_EMPLOYEE_ID)
            .sourceId(UPDATED_SOURCE_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleInitial(UPDATED_MIDDLE_INITIAL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .numberPhone(UPDATED_NUMBER_PHONE)
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .stateCode(UPDATED_STATE_CODE)
            .zip(UPDATED_ZIP)
            .birthDate(UPDATED_BIRTH_DATE)
            .department(UPDATED_DEPARTMENT)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get("/api/employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleInitial").value(hasItem(DEFAULT_MIDDLE_INITIAL)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].numberPhone").value(hasItem(DEFAULT_NUMBER_PHONE)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE)))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].socialSecurityNumber").value(hasItem(DEFAULT_SOCIAL_SECURITY_NUMBER)));
    }

    @Test
    @Transactional
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.middleInitial").value(DEFAULT_MIDDLE_INITIAL))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.numberPhone").value(DEFAULT_NUMBER_PHONE))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateCode").value(DEFAULT_STATE_CODE))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.socialSecurityNumber").value(DEFAULT_SOCIAL_SECURITY_NUMBER));
    }

    @Test
    @Transactional
    public void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId not equals to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.notEquals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId not equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.notEquals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the employeeList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is not null
        defaultEmployeeShouldBeFound("employeeId.specified=true");

        // Get all the employeeList where employeeId is null
        defaultEmployeeShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeIdContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId contains DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.contains=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId contains UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.contains=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeIdNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId does not contain DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.doesNotContain=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId does not contain UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.doesNotContain=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySourceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sourceId equals to DEFAULT_SOURCE_ID
        defaultEmployeeShouldBeFound("sourceId.equals=" + DEFAULT_SOURCE_ID);

        // Get all the employeeList where sourceId equals to UPDATED_SOURCE_ID
        defaultEmployeeShouldNotBeFound("sourceId.equals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySourceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sourceId not equals to DEFAULT_SOURCE_ID
        defaultEmployeeShouldNotBeFound("sourceId.notEquals=" + DEFAULT_SOURCE_ID);

        // Get all the employeeList where sourceId not equals to UPDATED_SOURCE_ID
        defaultEmployeeShouldBeFound("sourceId.notEquals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySourceIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sourceId in DEFAULT_SOURCE_ID or UPDATED_SOURCE_ID
        defaultEmployeeShouldBeFound("sourceId.in=" + DEFAULT_SOURCE_ID + "," + UPDATED_SOURCE_ID);

        // Get all the employeeList where sourceId equals to UPDATED_SOURCE_ID
        defaultEmployeeShouldNotBeFound("sourceId.in=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySourceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sourceId is not null
        defaultEmployeeShouldBeFound("sourceId.specified=true");

        // Get all the employeeList where sourceId is null
        defaultEmployeeShouldNotBeFound("sourceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesBySourceIdContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sourceId contains DEFAULT_SOURCE_ID
        defaultEmployeeShouldBeFound("sourceId.contains=" + DEFAULT_SOURCE_ID);

        // Get all the employeeList where sourceId contains UPDATED_SOURCE_ID
        defaultEmployeeShouldNotBeFound("sourceId.contains=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySourceIdNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sourceId does not contain DEFAULT_SOURCE_ID
        defaultEmployeeShouldNotBeFound("sourceId.doesNotContain=" + DEFAULT_SOURCE_ID);

        // Get all the employeeList where sourceId does not contain UPDATED_SOURCE_ID
        defaultEmployeeShouldBeFound("sourceId.doesNotContain=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllEmployeesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName not equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName not equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName is not null
        defaultEmployeeShouldBeFound("firstName.specified=true");

        // Get all the employeeList where firstName is null
        defaultEmployeeShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName contains DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName contains UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName does not contain DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName does not contain UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName not equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName not equals to UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName is not null
        defaultEmployeeShouldBeFound("lastName.specified=true");

        // Get all the employeeList where lastName is null
        defaultEmployeeShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName contains DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName contains UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName does not contain DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName does not contain UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByMiddleInitialIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleInitial equals to DEFAULT_MIDDLE_INITIAL
        defaultEmployeeShouldBeFound("middleInitial.equals=" + DEFAULT_MIDDLE_INITIAL);

        // Get all the employeeList where middleInitial equals to UPDATED_MIDDLE_INITIAL
        defaultEmployeeShouldNotBeFound("middleInitial.equals=" + UPDATED_MIDDLE_INITIAL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByMiddleInitialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleInitial not equals to DEFAULT_MIDDLE_INITIAL
        defaultEmployeeShouldNotBeFound("middleInitial.notEquals=" + DEFAULT_MIDDLE_INITIAL);

        // Get all the employeeList where middleInitial not equals to UPDATED_MIDDLE_INITIAL
        defaultEmployeeShouldBeFound("middleInitial.notEquals=" + UPDATED_MIDDLE_INITIAL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByMiddleInitialIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleInitial in DEFAULT_MIDDLE_INITIAL or UPDATED_MIDDLE_INITIAL
        defaultEmployeeShouldBeFound("middleInitial.in=" + DEFAULT_MIDDLE_INITIAL + "," + UPDATED_MIDDLE_INITIAL);

        // Get all the employeeList where middleInitial equals to UPDATED_MIDDLE_INITIAL
        defaultEmployeeShouldNotBeFound("middleInitial.in=" + UPDATED_MIDDLE_INITIAL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByMiddleInitialIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleInitial is not null
        defaultEmployeeShouldBeFound("middleInitial.specified=true");

        // Get all the employeeList where middleInitial is null
        defaultEmployeeShouldNotBeFound("middleInitial.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByMiddleInitialContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleInitial contains DEFAULT_MIDDLE_INITIAL
        defaultEmployeeShouldBeFound("middleInitial.contains=" + DEFAULT_MIDDLE_INITIAL);

        // Get all the employeeList where middleInitial contains UPDATED_MIDDLE_INITIAL
        defaultEmployeeShouldNotBeFound("middleInitial.contains=" + UPDATED_MIDDLE_INITIAL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByMiddleInitialNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleInitial does not contain DEFAULT_MIDDLE_INITIAL
        defaultEmployeeShouldNotBeFound("middleInitial.doesNotContain=" + DEFAULT_MIDDLE_INITIAL);

        // Get all the employeeList where middleInitial does not contain UPDATED_MIDDLE_INITIAL
        defaultEmployeeShouldBeFound("middleInitial.doesNotContain=" + UPDATED_MIDDLE_INITIAL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultEmployeeShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the employeeList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultEmployeeShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmailAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where emailAddress not equals to DEFAULT_EMAIL_ADDRESS
        defaultEmployeeShouldNotBeFound("emailAddress.notEquals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the employeeList where emailAddress not equals to UPDATED_EMAIL_ADDRESS
        defaultEmployeeShouldBeFound("emailAddress.notEquals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultEmployeeShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the employeeList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultEmployeeShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where emailAddress is not null
        defaultEmployeeShouldBeFound("emailAddress.specified=true");

        // Get all the employeeList where emailAddress is null
        defaultEmployeeShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultEmployeeShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the employeeList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultEmployeeShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultEmployeeShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the employeeList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultEmployeeShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByNumberPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where numberPhone equals to DEFAULT_NUMBER_PHONE
        defaultEmployeeShouldBeFound("numberPhone.equals=" + DEFAULT_NUMBER_PHONE);

        // Get all the employeeList where numberPhone equals to UPDATED_NUMBER_PHONE
        defaultEmployeeShouldNotBeFound("numberPhone.equals=" + UPDATED_NUMBER_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByNumberPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where numberPhone not equals to DEFAULT_NUMBER_PHONE
        defaultEmployeeShouldNotBeFound("numberPhone.notEquals=" + DEFAULT_NUMBER_PHONE);

        // Get all the employeeList where numberPhone not equals to UPDATED_NUMBER_PHONE
        defaultEmployeeShouldBeFound("numberPhone.notEquals=" + UPDATED_NUMBER_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByNumberPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where numberPhone in DEFAULT_NUMBER_PHONE or UPDATED_NUMBER_PHONE
        defaultEmployeeShouldBeFound("numberPhone.in=" + DEFAULT_NUMBER_PHONE + "," + UPDATED_NUMBER_PHONE);

        // Get all the employeeList where numberPhone equals to UPDATED_NUMBER_PHONE
        defaultEmployeeShouldNotBeFound("numberPhone.in=" + UPDATED_NUMBER_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByNumberPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where numberPhone is not null
        defaultEmployeeShouldBeFound("numberPhone.specified=true");

        // Get all the employeeList where numberPhone is null
        defaultEmployeeShouldNotBeFound("numberPhone.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByNumberPhoneContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where numberPhone contains DEFAULT_NUMBER_PHONE
        defaultEmployeeShouldBeFound("numberPhone.contains=" + DEFAULT_NUMBER_PHONE);

        // Get all the employeeList where numberPhone contains UPDATED_NUMBER_PHONE
        defaultEmployeeShouldNotBeFound("numberPhone.contains=" + UPDATED_NUMBER_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByNumberPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where numberPhone does not contain DEFAULT_NUMBER_PHONE
        defaultEmployeeShouldNotBeFound("numberPhone.doesNotContain=" + DEFAULT_NUMBER_PHONE);

        // Get all the employeeList where numberPhone does not contain UPDATED_NUMBER_PHONE
        defaultEmployeeShouldBeFound("numberPhone.doesNotContain=" + UPDATED_NUMBER_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where street equals to DEFAULT_STREET
        defaultEmployeeShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the employeeList where street equals to UPDATED_STREET
        defaultEmployeeShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStreetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where street not equals to DEFAULT_STREET
        defaultEmployeeShouldNotBeFound("street.notEquals=" + DEFAULT_STREET);

        // Get all the employeeList where street not equals to UPDATED_STREET
        defaultEmployeeShouldBeFound("street.notEquals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where street in DEFAULT_STREET or UPDATED_STREET
        defaultEmployeeShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the employeeList where street equals to UPDATED_STREET
        defaultEmployeeShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where street is not null
        defaultEmployeeShouldBeFound("street.specified=true");

        // Get all the employeeList where street is null
        defaultEmployeeShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByStreetContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where street contains DEFAULT_STREET
        defaultEmployeeShouldBeFound("street.contains=" + DEFAULT_STREET);

        // Get all the employeeList where street contains UPDATED_STREET
        defaultEmployeeShouldNotBeFound("street.contains=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStreetNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where street does not contain DEFAULT_STREET
        defaultEmployeeShouldNotBeFound("street.doesNotContain=" + DEFAULT_STREET);

        // Get all the employeeList where street does not contain UPDATED_STREET
        defaultEmployeeShouldBeFound("street.doesNotContain=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllEmployeesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city equals to DEFAULT_CITY
        defaultEmployeeShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the employeeList where city equals to UPDATED_CITY
        defaultEmployeeShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployeesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city not equals to DEFAULT_CITY
        defaultEmployeeShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the employeeList where city not equals to UPDATED_CITY
        defaultEmployeeShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployeesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city in DEFAULT_CITY or UPDATED_CITY
        defaultEmployeeShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the employeeList where city equals to UPDATED_CITY
        defaultEmployeeShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployeesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city is not null
        defaultEmployeeShouldBeFound("city.specified=true");

        // Get all the employeeList where city is null
        defaultEmployeeShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByCityContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city contains DEFAULT_CITY
        defaultEmployeeShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the employeeList where city contains UPDATED_CITY
        defaultEmployeeShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployeesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city does not contain DEFAULT_CITY
        defaultEmployeeShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the employeeList where city does not contain UPDATED_CITY
        defaultEmployeeShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStateCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where stateCode equals to DEFAULT_STATE_CODE
        defaultEmployeeShouldBeFound("stateCode.equals=" + DEFAULT_STATE_CODE);

        // Get all the employeeList where stateCode equals to UPDATED_STATE_CODE
        defaultEmployeeShouldNotBeFound("stateCode.equals=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStateCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where stateCode not equals to DEFAULT_STATE_CODE
        defaultEmployeeShouldNotBeFound("stateCode.notEquals=" + DEFAULT_STATE_CODE);

        // Get all the employeeList where stateCode not equals to UPDATED_STATE_CODE
        defaultEmployeeShouldBeFound("stateCode.notEquals=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStateCodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where stateCode in DEFAULT_STATE_CODE or UPDATED_STATE_CODE
        defaultEmployeeShouldBeFound("stateCode.in=" + DEFAULT_STATE_CODE + "," + UPDATED_STATE_CODE);

        // Get all the employeeList where stateCode equals to UPDATED_STATE_CODE
        defaultEmployeeShouldNotBeFound("stateCode.in=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStateCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where stateCode is not null
        defaultEmployeeShouldBeFound("stateCode.specified=true");

        // Get all the employeeList where stateCode is null
        defaultEmployeeShouldNotBeFound("stateCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByStateCodeContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where stateCode contains DEFAULT_STATE_CODE
        defaultEmployeeShouldBeFound("stateCode.contains=" + DEFAULT_STATE_CODE);

        // Get all the employeeList where stateCode contains UPDATED_STATE_CODE
        defaultEmployeeShouldNotBeFound("stateCode.contains=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByStateCodeNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where stateCode does not contain DEFAULT_STATE_CODE
        defaultEmployeeShouldNotBeFound("stateCode.doesNotContain=" + DEFAULT_STATE_CODE);

        // Get all the employeeList where stateCode does not contain UPDATED_STATE_CODE
        defaultEmployeeShouldBeFound("stateCode.doesNotContain=" + UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByZipIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where zip equals to DEFAULT_ZIP
        defaultEmployeeShouldBeFound("zip.equals=" + DEFAULT_ZIP);

        // Get all the employeeList where zip equals to UPDATED_ZIP
        defaultEmployeeShouldNotBeFound("zip.equals=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllEmployeesByZipIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where zip not equals to DEFAULT_ZIP
        defaultEmployeeShouldNotBeFound("zip.notEquals=" + DEFAULT_ZIP);

        // Get all the employeeList where zip not equals to UPDATED_ZIP
        defaultEmployeeShouldBeFound("zip.notEquals=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllEmployeesByZipIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where zip in DEFAULT_ZIP or UPDATED_ZIP
        defaultEmployeeShouldBeFound("zip.in=" + DEFAULT_ZIP + "," + UPDATED_ZIP);

        // Get all the employeeList where zip equals to UPDATED_ZIP
        defaultEmployeeShouldNotBeFound("zip.in=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllEmployeesByZipIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where zip is not null
        defaultEmployeeShouldBeFound("zip.specified=true");

        // Get all the employeeList where zip is null
        defaultEmployeeShouldNotBeFound("zip.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByZipContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where zip contains DEFAULT_ZIP
        defaultEmployeeShouldBeFound("zip.contains=" + DEFAULT_ZIP);

        // Get all the employeeList where zip contains UPDATED_ZIP
        defaultEmployeeShouldNotBeFound("zip.contains=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllEmployeesByZipNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where zip does not contain DEFAULT_ZIP
        defaultEmployeeShouldNotBeFound("zip.doesNotContain=" + DEFAULT_ZIP);

        // Get all the employeeList where zip does not contain UPDATED_ZIP
        defaultEmployeeShouldBeFound("zip.doesNotContain=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the employeeList where birthDate equals to UPDATED_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the employeeList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the employeeList where birthDate equals to UPDATED_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate is not null
        defaultEmployeeShouldBeFound("birthDate.specified=true");

        // Get all the employeeList where birthDate is null
        defaultEmployeeShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where department equals to DEFAULT_DEPARTMENT
        defaultEmployeeShouldBeFound("department.equals=" + DEFAULT_DEPARTMENT);

        // Get all the employeeList where department equals to UPDATED_DEPARTMENT
        defaultEmployeeShouldNotBeFound("department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllEmployeesByDepartmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where department not equals to DEFAULT_DEPARTMENT
        defaultEmployeeShouldNotBeFound("department.notEquals=" + DEFAULT_DEPARTMENT);

        // Get all the employeeList where department not equals to UPDATED_DEPARTMENT
        defaultEmployeeShouldBeFound("department.notEquals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllEmployeesByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where department in DEFAULT_DEPARTMENT or UPDATED_DEPARTMENT
        defaultEmployeeShouldBeFound("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT);

        // Get all the employeeList where department equals to UPDATED_DEPARTMENT
        defaultEmployeeShouldNotBeFound("department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllEmployeesByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where department is not null
        defaultEmployeeShouldBeFound("department.specified=true");

        // Get all the employeeList where department is null
        defaultEmployeeShouldNotBeFound("department.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where department contains DEFAULT_DEPARTMENT
        defaultEmployeeShouldBeFound("department.contains=" + DEFAULT_DEPARTMENT);

        // Get all the employeeList where department contains UPDATED_DEPARTMENT
        defaultEmployeeShouldNotBeFound("department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllEmployeesByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where department does not contain DEFAULT_DEPARTMENT
        defaultEmployeeShouldNotBeFound("department.doesNotContain=" + DEFAULT_DEPARTMENT);

        // Get all the employeeList where department does not contain UPDATED_DEPARTMENT
        defaultEmployeeShouldBeFound("department.doesNotContain=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySocialSecurityNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialSecurityNumber equals to DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldBeFound("socialSecurityNumber.equals=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the employeeList where socialSecurityNumber equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldNotBeFound("socialSecurityNumber.equals=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySocialSecurityNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialSecurityNumber not equals to DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldNotBeFound("socialSecurityNumber.notEquals=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the employeeList where socialSecurityNumber not equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldBeFound("socialSecurityNumber.notEquals=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySocialSecurityNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialSecurityNumber in DEFAULT_SOCIAL_SECURITY_NUMBER or UPDATED_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldBeFound("socialSecurityNumber.in=" + DEFAULT_SOCIAL_SECURITY_NUMBER + "," + UPDATED_SOCIAL_SECURITY_NUMBER);

        // Get all the employeeList where socialSecurityNumber equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldNotBeFound("socialSecurityNumber.in=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySocialSecurityNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialSecurityNumber is not null
        defaultEmployeeShouldBeFound("socialSecurityNumber.specified=true");

        // Get all the employeeList where socialSecurityNumber is null
        defaultEmployeeShouldNotBeFound("socialSecurityNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesBySocialSecurityNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialSecurityNumber contains DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldBeFound("socialSecurityNumber.contains=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the employeeList where socialSecurityNumber contains UPDATED_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldNotBeFound("socialSecurityNumber.contains=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllEmployeesBySocialSecurityNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where socialSecurityNumber does not contain DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldNotBeFound("socialSecurityNumber.doesNotContain=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the employeeList where socialSecurityNumber does not contain UPDATED_SOCIAL_SECURITY_NUMBER
        defaultEmployeeShouldBeFound("socialSecurityNumber.doesNotContain=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get("/api/employees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleInitial").value(hasItem(DEFAULT_MIDDLE_INITIAL)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].numberPhone").value(hasItem(DEFAULT_NUMBER_PHONE)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE)))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].socialSecurityNumber").value(hasItem(DEFAULT_SOCIAL_SECURITY_NUMBER)));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get("/api/employees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get("/api/employees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get("/api/employees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
