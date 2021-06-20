//package com.vts.clientcenter.web.rest;
//
//import com.vts.clientcenter.RedisTestContainerExtension;
//import com.vts.clientcenter.ClientCenterServiceApp;
//import com.vts.clientcenter.config.TestSecurityConfiguration;
//import com.vts.clientcenter.domain.UserProfile;
//import com.vts.clientcenter.repository.UserProfileRepository;
//import com.vts.clientcenter.service.UserProfileService;
//import com.vts.clientcenter.service.dto.UserProfileDTO;
//import com.vts.clientcenter.service.mapper.UserProfileMapper;
//import com.vts.clientcenter.service.dto.UserProfileCriteria;
//import com.vts.clientcenter.service.UserProfileQueryService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//import javax.persistence.EntityManager;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.vts.clientcenter.domain.enumeration.Gender;
///**
// * Integration tests for the {@link UserProfileResource} REST controller.
// */
//@SpringBootTest(classes = { ClientCenterServiceApp.class, TestSecurityConfiguration.class })
//@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
//@AutoConfigureMockMvc
//@WithMockUser
//public class UserProfileResourceIT {
//
//    private static final String DEFAULT_ID = "AAAAAAAAAA";
//    private static final String UPDATED_ID = "BBBBBBBBBB";
//
//    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
//    private static final String UPDATED_PHONE = "BBBBBBBBBB";
//
//    private static final Gender DEFAULT_GENDER = Gender.Male;
//    private static final Gender UPDATED_GENDER = Gender.Female;
//
//    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
//    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";
//
//    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
//    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";
//
//    @Autowired
//    private UserProfileRepository userProfileRepository;
//
//    @Autowired
//    private UserProfileMapper userProfileMapper;
//
//    @Autowired
//    private UserProfileService userProfileService;
//
//    @Autowired
//    private UserProfileQueryService userProfileQueryService;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restUserProfileMockMvc;
//
//    private UserProfile userProfile;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static UserProfile createEntity(EntityManager em) {
//        UserProfile userProfile = new UserProfile()
//            .id(DEFAULT_ID)
//            .phone(DEFAULT_PHONE)
//            .gender(DEFAULT_GENDER)
//            .birthDate(DEFAULT_BIRTH_DATE)
//            .createdDate(DEFAULT_CREATED_DATE)
//            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
//            .createdBy(DEFAULT_CREATED_BY)
//            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
//        return userProfile;
//    }
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static UserProfile createUpdatedEntity(EntityManager em) {
//        UserProfile userProfile = new UserProfile()
//            .id(UPDATED_ID)
//            .phone(UPDATED_PHONE)
//            .gender(UPDATED_GENDER)
//            .birthDate(UPDATED_BIRTH_DATE)
//            .createdDate(UPDATED_CREATED_DATE)
//            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
//            .createdBy(UPDATED_CREATED_BY)
//            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
//        return userProfile;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        userProfile = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfiles() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList
//        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
//            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
//            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
//            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
//            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
//            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
//            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
//    }
//
//    @Test
//    @Transactional
//    public void getUserProfile() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get the userProfile
//        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", userProfile.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(DEFAULT_ID))
//            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
//            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
//            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
//            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
//            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
//            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
//            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
//    }
//
//
////    @Test
////    @Transactional
////    public void getUserProfilesByIdFiltering() throws Exception {
////        // Initialize the database
////        userProfileRepository.saveAndFlush(userProfile);
////
////        Long id = userProfile.getId();
////
////        defaultUserProfileShouldBeFound("id.equals=" + id);
////        defaultUserProfileShouldNotBeFound("id.notEquals=" + id);
////
////        defaultUserProfileShouldBeFound("id.greaterThanOrEqual=" + id);
////        defaultUserProfileShouldNotBeFound("id.greaterThan=" + id);
////
////        defaultUserProfileShouldBeFound("id.lessThanOrEqual=" + id);
////        defaultUserProfileShouldNotBeFound("id.lessThan=" + id);
////    }
//
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByIdIsEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where id equals to DEFAULT_ID
//        defaultUserProfileShouldBeFound("id.equals=" + DEFAULT_ID);
//
//        // Get all the userProfileList where id equals to UPDATED_ID
//        defaultUserProfileShouldNotBeFound("id.equals=" + UPDATED_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByIdIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where id not equals to DEFAULT_ID
//        defaultUserProfileShouldNotBeFound("id.notEquals=" + DEFAULT_ID);
//
//        // Get all the userProfileList where id not equals to UPDATED_ID
//        defaultUserProfileShouldBeFound("id.notEquals=" + UPDATED_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByIdIsInShouldWork() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where id in DEFAULT_ID or UPDATED_ID
//        defaultUserProfileShouldBeFound("id.in=" + DEFAULT_ID + "," + UPDATED_ID);
//
//        // Get all the userProfileList where id equals to UPDATED_ID
//        defaultUserProfileShouldNotBeFound("id.in=" + UPDATED_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByIdIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where id is not null
//        defaultUserProfileShouldBeFound("id.specified=true");
//
//        // Get all the userProfileList where id is null
//        defaultUserProfileShouldNotBeFound("id.specified=false");
//    }
//                @Test
//    @Transactional
//    public void getAllUserProfilesByIdContainsSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where id contains DEFAULT_ID
//        defaultUserProfileShouldBeFound("id.contains=" + DEFAULT_ID);
//
//        // Get all the userProfileList where id contains UPDATED_ID
//        defaultUserProfileShouldNotBeFound("id.contains=" + UPDATED_ID);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByIdNotContainsSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where id does not contain DEFAULT_ID
//        defaultUserProfileShouldNotBeFound("id.doesNotContain=" + DEFAULT_ID);
//
//        // Get all the userProfileList where id does not contain UPDATED_ID
//        defaultUserProfileShouldBeFound("id.doesNotContain=" + UPDATED_ID);
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByPhoneIsEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where phone equals to DEFAULT_PHONE
//        defaultUserProfileShouldBeFound("phone.equals=" + DEFAULT_PHONE);
//
//        // Get all the userProfileList where phone equals to UPDATED_PHONE
//        defaultUserProfileShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByPhoneIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where phone not equals to DEFAULT_PHONE
//        defaultUserProfileShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);
//
//        // Get all the userProfileList where phone not equals to UPDATED_PHONE
//        defaultUserProfileShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByPhoneIsInShouldWork() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where phone in DEFAULT_PHONE or UPDATED_PHONE
//        defaultUserProfileShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);
//
//        // Get all the userProfileList where phone equals to UPDATED_PHONE
//        defaultUserProfileShouldNotBeFound("phone.in=" + UPDATED_PHONE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByPhoneIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where phone is not null
//        defaultUserProfileShouldBeFound("phone.specified=true");
//
//        // Get all the userProfileList where phone is null
//        defaultUserProfileShouldNotBeFound("phone.specified=false");
//    }
//                @Test
//    @Transactional
//    public void getAllUserProfilesByPhoneContainsSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where phone contains DEFAULT_PHONE
//        defaultUserProfileShouldBeFound("phone.contains=" + DEFAULT_PHONE);
//
//        // Get all the userProfileList where phone contains UPDATED_PHONE
//        defaultUserProfileShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByPhoneNotContainsSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where phone does not contain DEFAULT_PHONE
//        defaultUserProfileShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);
//
//        // Get all the userProfileList where phone does not contain UPDATED_PHONE
//        defaultUserProfileShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByGenderIsEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where gender equals to DEFAULT_GENDER
//        defaultUserProfileShouldBeFound("gender.equals=" + DEFAULT_GENDER);
//
//        // Get all the userProfileList where gender equals to UPDATED_GENDER
//        defaultUserProfileShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByGenderIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where gender not equals to DEFAULT_GENDER
//        defaultUserProfileShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);
//
//        // Get all the userProfileList where gender not equals to UPDATED_GENDER
//        defaultUserProfileShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByGenderIsInShouldWork() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where gender in DEFAULT_GENDER or UPDATED_GENDER
//        defaultUserProfileShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);
//
//        // Get all the userProfileList where gender equals to UPDATED_GENDER
//        defaultUserProfileShouldNotBeFound("gender.in=" + UPDATED_GENDER);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByGenderIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where gender is not null
//        defaultUserProfileShouldBeFound("gender.specified=true");
//
//        // Get all the userProfileList where gender is null
//        defaultUserProfileShouldNotBeFound("gender.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByBirthDateIsEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where birthDate equals to DEFAULT_BIRTH_DATE
//        defaultUserProfileShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);
//
//        // Get all the userProfileList where birthDate equals to UPDATED_BIRTH_DATE
//        defaultUserProfileShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByBirthDateIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where birthDate not equals to DEFAULT_BIRTH_DATE
//        defaultUserProfileShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);
//
//        // Get all the userProfileList where birthDate not equals to UPDATED_BIRTH_DATE
//        defaultUserProfileShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByBirthDateIsInShouldWork() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
//        defaultUserProfileShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);
//
//        // Get all the userProfileList where birthDate equals to UPDATED_BIRTH_DATE
//        defaultUserProfileShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByBirthDateIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where birthDate is not null
//        defaultUserProfileShouldBeFound("birthDate.specified=true");
//
//        // Get all the userProfileList where birthDate is null
//        defaultUserProfileShouldNotBeFound("birthDate.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedDateIsEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdDate equals to DEFAULT_CREATED_DATE
//        defaultUserProfileShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);
//
//        // Get all the userProfileList where createdDate equals to UPDATED_CREATED_DATE
//        defaultUserProfileShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedDateIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdDate not equals to DEFAULT_CREATED_DATE
//        defaultUserProfileShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);
//
//        // Get all the userProfileList where createdDate not equals to UPDATED_CREATED_DATE
//        defaultUserProfileShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedDateIsInShouldWork() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
//        defaultUserProfileShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);
//
//        // Get all the userProfileList where createdDate equals to UPDATED_CREATED_DATE
//        defaultUserProfileShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedDateIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdDate is not null
//        defaultUserProfileShouldBeFound("createdDate.specified=true");
//
//        // Get all the userProfileList where createdDate is null
//        defaultUserProfileShouldNotBeFound("createdDate.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedDateIsEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
//        defaultUserProfileShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);
//
//        // Get all the userProfileList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
//        defaultUserProfileShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedDateIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
//        defaultUserProfileShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);
//
//        // Get all the userProfileList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
//        defaultUserProfileShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedDateIsInShouldWork() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
//        defaultUserProfileShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);
//
//        // Get all the userProfileList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
//        defaultUserProfileShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedDateIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedDate is not null
//        defaultUserProfileShouldBeFound("lastModifiedDate.specified=true");
//
//        // Get all the userProfileList where lastModifiedDate is null
//        defaultUserProfileShouldNotBeFound("lastModifiedDate.specified=false");
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedByIsEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdBy equals to DEFAULT_CREATED_BY
//        defaultUserProfileShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);
//
//        // Get all the userProfileList where createdBy equals to UPDATED_CREATED_BY
//        defaultUserProfileShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedByIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdBy not equals to DEFAULT_CREATED_BY
//        defaultUserProfileShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);
//
//        // Get all the userProfileList where createdBy not equals to UPDATED_CREATED_BY
//        defaultUserProfileShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedByIsInShouldWork() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
//        defaultUserProfileShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);
//
//        // Get all the userProfileList where createdBy equals to UPDATED_CREATED_BY
//        defaultUserProfileShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedByIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdBy is not null
//        defaultUserProfileShouldBeFound("createdBy.specified=true");
//
//        // Get all the userProfileList where createdBy is null
//        defaultUserProfileShouldNotBeFound("createdBy.specified=false");
//    }
//                @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedByContainsSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdBy contains DEFAULT_CREATED_BY
//        defaultUserProfileShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);
//
//        // Get all the userProfileList where createdBy contains UPDATED_CREATED_BY
//        defaultUserProfileShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByCreatedByNotContainsSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where createdBy does not contain DEFAULT_CREATED_BY
//        defaultUserProfileShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
//
//        // Get all the userProfileList where createdBy does not contain UPDATED_CREATED_BY
//        defaultUserProfileShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
//    }
//
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedByIsEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
//        defaultUserProfileShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);
//
//        // Get all the userProfileList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
//        defaultUserProfileShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedByIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
//        defaultUserProfileShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);
//
//        // Get all the userProfileList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
//        defaultUserProfileShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedByIsInShouldWork() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
//        defaultUserProfileShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);
//
//        // Get all the userProfileList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
//        defaultUserProfileShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedByIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedBy is not null
//        defaultUserProfileShouldBeFound("lastModifiedBy.specified=true");
//
//        // Get all the userProfileList where lastModifiedBy is null
//        defaultUserProfileShouldNotBeFound("lastModifiedBy.specified=false");
//    }
//                @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedByContainsSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
//        defaultUserProfileShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);
//
//        // Get all the userProfileList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
//        defaultUserProfileShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
//    }
//
//    @Test
//    @Transactional
//    public void getAllUserProfilesByLastModifiedByNotContainsSomething() throws Exception {
//        // Initialize the database
//        userProfileRepository.saveAndFlush(userProfile);
//
//        // Get all the userProfileList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
//        defaultUserProfileShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);
//
//        // Get all the userProfileList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
//        defaultUserProfileShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is returned.
//     */
//    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
//        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
//            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
//            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
//            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
//            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
//            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
//            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
//
//        // Check, that the count call also returns 1
//        restUserProfileMockMvc.perform(get("/api/user-profiles/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().string("1"));
//    }
//
//    /**
//     * Executes the search, and checks that the default entity is not returned.
//     */
//    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
//        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$").isArray())
//            .andExpect(jsonPath("$").isEmpty());
//
//        // Check, that the count call also returns 0
//        restUserProfileMockMvc.perform(get("/api/user-profiles/count?sort=id,desc&" + filter))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(content().string("0"));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingUserProfile() throws Exception {
//        // Get the userProfile
//        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//}
