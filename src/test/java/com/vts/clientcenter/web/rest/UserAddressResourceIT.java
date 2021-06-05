package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.RedisTestContainerExtension;
import com.vts.clientcenter.ClientCenterServiceApp;
import com.vts.clientcenter.config.TestSecurityConfiguration;
import com.vts.clientcenter.domain.UserAddress;
import com.vts.clientcenter.repository.UserAddressRepository;
import com.vts.clientcenter.service.UserAddressService;
import com.vts.clientcenter.service.dto.UserAddressDTO;
import com.vts.clientcenter.service.mapper.UserAddressMapper;
import com.vts.clientcenter.service.dto.UserAddressCriteria;
import com.vts.clientcenter.service.UserAddressQueryService;

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
 * Integration tests for the {@link UserAddressResource} REST controller.
 */
@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class UserAddressResourceIT {

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

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
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private UserAddressQueryService userAddressQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAddressMockMvc;

    private UserAddress userAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAddress createEntity(EntityManager em) {
        UserAddress userAddress = new UserAddress()
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .zipCode(DEFAULT_ZIP_CODE)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return userAddress;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAddress createUpdatedEntity(EntityManager em) {
        UserAddress userAddress = new UserAddress()
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .zipCode(UPDATED_ZIP_CODE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return userAddress;
    }

    @BeforeEach
    public void initTest() {
        userAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllUserAddresses() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList
        restUserAddressMockMvc.perform(get("/api/user-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get the userAddress
        restUserAddressMockMvc.perform(get("/api/user-addresses/{id}", userAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAddress.getId().intValue()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getUserAddressesByIdFiltering() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        Long id = userAddress.getId();

        defaultUserAddressShouldBeFound("id.equals=" + id);
        defaultUserAddressShouldNotBeFound("id.notEquals=" + id);

        defaultUserAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultUserAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserAddressShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultUserAddressShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userAddressList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultUserAddressShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine1 not equals to DEFAULT_ADDRESS_LINE_1
        defaultUserAddressShouldNotBeFound("addressLine1.notEquals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userAddressList where addressLine1 not equals to UPDATED_ADDRESS_LINE_1
        defaultUserAddressShouldBeFound("addressLine1.notEquals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultUserAddressShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the userAddressList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultUserAddressShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine1 is not null
        defaultUserAddressShouldBeFound("addressLine1.specified=true");

        // Get all the userAddressList where addressLine1 is null
        defaultUserAddressShouldNotBeFound("addressLine1.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultUserAddressShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userAddressList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultUserAddressShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultUserAddressShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userAddressList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultUserAddressShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultUserAddressShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userAddressList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultUserAddressShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine2 not equals to DEFAULT_ADDRESS_LINE_2
        defaultUserAddressShouldNotBeFound("addressLine2.notEquals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userAddressList where addressLine2 not equals to UPDATED_ADDRESS_LINE_2
        defaultUserAddressShouldBeFound("addressLine2.notEquals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultUserAddressShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the userAddressList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultUserAddressShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine2 is not null
        defaultUserAddressShouldBeFound("addressLine2.specified=true");

        // Get all the userAddressList where addressLine2 is null
        defaultUserAddressShouldNotBeFound("addressLine2.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultUserAddressShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userAddressList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultUserAddressShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultUserAddressShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userAddressList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultUserAddressShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city equals to DEFAULT_CITY
        defaultUserAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the userAddressList where city equals to UPDATED_CITY
        defaultUserAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city not equals to DEFAULT_CITY
        defaultUserAddressShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the userAddressList where city not equals to UPDATED_CITY
        defaultUserAddressShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultUserAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the userAddressList where city equals to UPDATED_CITY
        defaultUserAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city is not null
        defaultUserAddressShouldBeFound("city.specified=true");

        // Get all the userAddressList where city is null
        defaultUserAddressShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city contains DEFAULT_CITY
        defaultUserAddressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the userAddressList where city contains UPDATED_CITY
        defaultUserAddressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city does not contain DEFAULT_CITY
        defaultUserAddressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the userAddressList where city does not contain UPDATED_CITY
        defaultUserAddressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country equals to DEFAULT_COUNTRY
        defaultUserAddressShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the userAddressList where country equals to UPDATED_COUNTRY
        defaultUserAddressShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country not equals to DEFAULT_COUNTRY
        defaultUserAddressShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the userAddressList where country not equals to UPDATED_COUNTRY
        defaultUserAddressShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultUserAddressShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the userAddressList where country equals to UPDATED_COUNTRY
        defaultUserAddressShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country is not null
        defaultUserAddressShouldBeFound("country.specified=true");

        // Get all the userAddressList where country is null
        defaultUserAddressShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByCountryContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country contains DEFAULT_COUNTRY
        defaultUserAddressShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the userAddressList where country contains UPDATED_COUNTRY
        defaultUserAddressShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country does not contain DEFAULT_COUNTRY
        defaultUserAddressShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the userAddressList where country does not contain UPDATED_COUNTRY
        defaultUserAddressShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode equals to DEFAULT_ZIP_CODE
        defaultUserAddressShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the userAddressList where zipCode equals to UPDATED_ZIP_CODE
        defaultUserAddressShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByZipCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode not equals to DEFAULT_ZIP_CODE
        defaultUserAddressShouldNotBeFound("zipCode.notEquals=" + DEFAULT_ZIP_CODE);

        // Get all the userAddressList where zipCode not equals to UPDATED_ZIP_CODE
        defaultUserAddressShouldBeFound("zipCode.notEquals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultUserAddressShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the userAddressList where zipCode equals to UPDATED_ZIP_CODE
        defaultUserAddressShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode is not null
        defaultUserAddressShouldBeFound("zipCode.specified=true");

        // Get all the userAddressList where zipCode is null
        defaultUserAddressShouldNotBeFound("zipCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode contains DEFAULT_ZIP_CODE
        defaultUserAddressShouldBeFound("zipCode.contains=" + DEFAULT_ZIP_CODE);

        // Get all the userAddressList where zipCode contains UPDATED_ZIP_CODE
        defaultUserAddressShouldNotBeFound("zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode does not contain DEFAULT_ZIP_CODE
        defaultUserAddressShouldNotBeFound("zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);

        // Get all the userAddressList where zipCode does not contain UPDATED_ZIP_CODE
        defaultUserAddressShouldBeFound("zipCode.doesNotContain=" + UPDATED_ZIP_CODE);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where longitude equals to DEFAULT_LONGITUDE
        defaultUserAddressShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the userAddressList where longitude equals to UPDATED_LONGITUDE
        defaultUserAddressShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where longitude not equals to DEFAULT_LONGITUDE
        defaultUserAddressShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the userAddressList where longitude not equals to UPDATED_LONGITUDE
        defaultUserAddressShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultUserAddressShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the userAddressList where longitude equals to UPDATED_LONGITUDE
        defaultUserAddressShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where longitude is not null
        defaultUserAddressShouldBeFound("longitude.specified=true");

        // Get all the userAddressList where longitude is null
        defaultUserAddressShouldNotBeFound("longitude.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where longitude contains DEFAULT_LONGITUDE
        defaultUserAddressShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the userAddressList where longitude contains UPDATED_LONGITUDE
        defaultUserAddressShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where longitude does not contain DEFAULT_LONGITUDE
        defaultUserAddressShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the userAddressList where longitude does not contain UPDATED_LONGITUDE
        defaultUserAddressShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where latitude equals to DEFAULT_LATITUDE
        defaultUserAddressShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the userAddressList where latitude equals to UPDATED_LATITUDE
        defaultUserAddressShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where latitude not equals to DEFAULT_LATITUDE
        defaultUserAddressShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the userAddressList where latitude not equals to UPDATED_LATITUDE
        defaultUserAddressShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultUserAddressShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the userAddressList where latitude equals to UPDATED_LATITUDE
        defaultUserAddressShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where latitude is not null
        defaultUserAddressShouldBeFound("latitude.specified=true");

        // Get all the userAddressList where latitude is null
        defaultUserAddressShouldNotBeFound("latitude.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where latitude contains DEFAULT_LATITUDE
        defaultUserAddressShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the userAddressList where latitude contains UPDATED_LATITUDE
        defaultUserAddressShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where latitude does not contain DEFAULT_LATITUDE
        defaultUserAddressShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the userAddressList where latitude does not contain UPDATED_LATITUDE
        defaultUserAddressShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdDate equals to DEFAULT_CREATED_DATE
        defaultUserAddressShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the userAddressList where createdDate equals to UPDATED_CREATED_DATE
        defaultUserAddressShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultUserAddressShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the userAddressList where createdDate not equals to UPDATED_CREATED_DATE
        defaultUserAddressShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultUserAddressShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the userAddressList where createdDate equals to UPDATED_CREATED_DATE
        defaultUserAddressShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdDate is not null
        defaultUserAddressShouldBeFound("createdDate.specified=true");

        // Get all the userAddressList where createdDate is null
        defaultUserAddressShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultUserAddressShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the userAddressList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultUserAddressShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultUserAddressShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the userAddressList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultUserAddressShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultUserAddressShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the userAddressList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultUserAddressShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedDate is not null
        defaultUserAddressShouldBeFound("lastModifiedDate.specified=true");

        // Get all the userAddressList where lastModifiedDate is null
        defaultUserAddressShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdBy equals to DEFAULT_CREATED_BY
        defaultUserAddressShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the userAddressList where createdBy equals to UPDATED_CREATED_BY
        defaultUserAddressShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdBy not equals to DEFAULT_CREATED_BY
        defaultUserAddressShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the userAddressList where createdBy not equals to UPDATED_CREATED_BY
        defaultUserAddressShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultUserAddressShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the userAddressList where createdBy equals to UPDATED_CREATED_BY
        defaultUserAddressShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdBy is not null
        defaultUserAddressShouldBeFound("createdBy.specified=true");

        // Get all the userAddressList where createdBy is null
        defaultUserAddressShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdBy contains DEFAULT_CREATED_BY
        defaultUserAddressShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the userAddressList where createdBy contains UPDATED_CREATED_BY
        defaultUserAddressShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdBy does not contain DEFAULT_CREATED_BY
        defaultUserAddressShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the userAddressList where createdBy does not contain UPDATED_CREATED_BY
        defaultUserAddressShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultUserAddressShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the userAddressList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultUserAddressShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultUserAddressShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the userAddressList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultUserAddressShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultUserAddressShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the userAddressList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultUserAddressShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedBy is not null
        defaultUserAddressShouldBeFound("lastModifiedBy.specified=true");

        // Get all the userAddressList where lastModifiedBy is null
        defaultUserAddressShouldNotBeFound("lastModifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultUserAddressShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the userAddressList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultUserAddressShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllUserAddressesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultUserAddressShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the userAddressList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultUserAddressShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserAddressShouldBeFound(String filter) throws Exception {
        restUserAddressMockMvc.perform(get("/api/user-addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restUserAddressMockMvc.perform(get("/api/user-addresses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserAddressShouldNotBeFound(String filter) throws Exception {
        restUserAddressMockMvc.perform(get("/api/user-addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserAddressMockMvc.perform(get("/api/user-addresses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserAddress() throws Exception {
        // Get the userAddress
        restUserAddressMockMvc.perform(get("/api/user-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
