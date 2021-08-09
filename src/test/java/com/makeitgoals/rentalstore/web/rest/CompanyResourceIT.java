package com.makeitgoals.rentalstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.Company;
import com.makeitgoals.rentalstore.repository.CompanyRepository;
import com.makeitgoals.rentalstore.service.criteria.CompanyCriteria;
import com.makeitgoals.rentalstore.service.dto.CompanyDTO;
import com.makeitgoals.rentalstore.service.mapper.CompanyMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEALS_IN = "AAAAAAAAAA";
    private static final String UPDATED_DEALS_IN = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "l@MzES.1k";
    private static final String UPDATED_EMAIL = "+q*X9@Aajb.t|p";

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .companyName(DEFAULT_COMPANY_NAME)
            .dealsIn(DEFAULT_DEALS_IN)
            .officeAddress(DEFAULT_OFFICE_ADDRESS)
            .companyPhoneNumber(DEFAULT_COMPANY_PHONE_NUMBER)
            .email(DEFAULT_EMAIL);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .companyName(UPDATED_COMPANY_NAME)
            .dealsIn(UPDATED_DEALS_IN)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .companyPhoneNumber(UPDATED_COMPANY_PHONE_NUMBER)
            .email(UPDATED_EMAIL);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();
        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompany.getDealsIn()).isEqualTo(DEFAULT_DEALS_IN);
        assertThat(testCompany.getOfficeAddress()).isEqualTo(DEFAULT_OFFICE_ADDRESS);
        assertThat(testCompany.getCompanyPhoneNumber()).isEqualTo(DEFAULT_COMPANY_PHONE_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);
        CompanyDTO companyDTO = companyMapper.toDto(company);

        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyName(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDealsInIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setDealsIn(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].dealsIn").value(hasItem(DEFAULT_DEALS_IN)))
            .andExpect(jsonPath("$.[*].officeAddress").value(hasItem(DEFAULT_OFFICE_ADDRESS)))
            .andExpect(jsonPath("$.[*].companyPhoneNumber").value(hasItem(DEFAULT_COMPANY_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.dealsIn").value(DEFAULT_DEALS_IN))
            .andExpect(jsonPath("$.officeAddress").value(DEFAULT_OFFICE_ADDRESS))
            .andExpect(jsonPath("$.companyPhoneNumber").value(DEFAULT_COMPANY_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        Long id = company.getId();

        defaultCompanyShouldBeFound("id.equals=" + id);
        defaultCompanyShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName equals to DEFAULT_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName not equals to DEFAULT_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.notEquals=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName not equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.notEquals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the companyList where companyName equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName is not null
        defaultCompanyShouldBeFound("companyName.specified=true");

        // Get all the companyList where companyName is null
        defaultCompanyShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName contains DEFAULT_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName contains UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName does not contain UPDATED_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByDealsInIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where dealsIn equals to DEFAULT_DEALS_IN
        defaultCompanyShouldBeFound("dealsIn.equals=" + DEFAULT_DEALS_IN);

        // Get all the companyList where dealsIn equals to UPDATED_DEALS_IN
        defaultCompanyShouldNotBeFound("dealsIn.equals=" + UPDATED_DEALS_IN);
    }

    @Test
    @Transactional
    void getAllCompaniesByDealsInIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where dealsIn not equals to DEFAULT_DEALS_IN
        defaultCompanyShouldNotBeFound("dealsIn.notEquals=" + DEFAULT_DEALS_IN);

        // Get all the companyList where dealsIn not equals to UPDATED_DEALS_IN
        defaultCompanyShouldBeFound("dealsIn.notEquals=" + UPDATED_DEALS_IN);
    }

    @Test
    @Transactional
    void getAllCompaniesByDealsInIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where dealsIn in DEFAULT_DEALS_IN or UPDATED_DEALS_IN
        defaultCompanyShouldBeFound("dealsIn.in=" + DEFAULT_DEALS_IN + "," + UPDATED_DEALS_IN);

        // Get all the companyList where dealsIn equals to UPDATED_DEALS_IN
        defaultCompanyShouldNotBeFound("dealsIn.in=" + UPDATED_DEALS_IN);
    }

    @Test
    @Transactional
    void getAllCompaniesByDealsInIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where dealsIn is not null
        defaultCompanyShouldBeFound("dealsIn.specified=true");

        // Get all the companyList where dealsIn is null
        defaultCompanyShouldNotBeFound("dealsIn.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByDealsInContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where dealsIn contains DEFAULT_DEALS_IN
        defaultCompanyShouldBeFound("dealsIn.contains=" + DEFAULT_DEALS_IN);

        // Get all the companyList where dealsIn contains UPDATED_DEALS_IN
        defaultCompanyShouldNotBeFound("dealsIn.contains=" + UPDATED_DEALS_IN);
    }

    @Test
    @Transactional
    void getAllCompaniesByDealsInNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where dealsIn does not contain DEFAULT_DEALS_IN
        defaultCompanyShouldNotBeFound("dealsIn.doesNotContain=" + DEFAULT_DEALS_IN);

        // Get all the companyList where dealsIn does not contain UPDATED_DEALS_IN
        defaultCompanyShouldBeFound("dealsIn.doesNotContain=" + UPDATED_DEALS_IN);
    }

    @Test
    @Transactional
    void getAllCompaniesByOfficeAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where officeAddress equals to DEFAULT_OFFICE_ADDRESS
        defaultCompanyShouldBeFound("officeAddress.equals=" + DEFAULT_OFFICE_ADDRESS);

        // Get all the companyList where officeAddress equals to UPDATED_OFFICE_ADDRESS
        defaultCompanyShouldNotBeFound("officeAddress.equals=" + UPDATED_OFFICE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByOfficeAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where officeAddress not equals to DEFAULT_OFFICE_ADDRESS
        defaultCompanyShouldNotBeFound("officeAddress.notEquals=" + DEFAULT_OFFICE_ADDRESS);

        // Get all the companyList where officeAddress not equals to UPDATED_OFFICE_ADDRESS
        defaultCompanyShouldBeFound("officeAddress.notEquals=" + UPDATED_OFFICE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByOfficeAddressIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where officeAddress in DEFAULT_OFFICE_ADDRESS or UPDATED_OFFICE_ADDRESS
        defaultCompanyShouldBeFound("officeAddress.in=" + DEFAULT_OFFICE_ADDRESS + "," + UPDATED_OFFICE_ADDRESS);

        // Get all the companyList where officeAddress equals to UPDATED_OFFICE_ADDRESS
        defaultCompanyShouldNotBeFound("officeAddress.in=" + UPDATED_OFFICE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByOfficeAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where officeAddress is not null
        defaultCompanyShouldBeFound("officeAddress.specified=true");

        // Get all the companyList where officeAddress is null
        defaultCompanyShouldNotBeFound("officeAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByOfficeAddressContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where officeAddress contains DEFAULT_OFFICE_ADDRESS
        defaultCompanyShouldBeFound("officeAddress.contains=" + DEFAULT_OFFICE_ADDRESS);

        // Get all the companyList where officeAddress contains UPDATED_OFFICE_ADDRESS
        defaultCompanyShouldNotBeFound("officeAddress.contains=" + UPDATED_OFFICE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByOfficeAddressNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where officeAddress does not contain DEFAULT_OFFICE_ADDRESS
        defaultCompanyShouldNotBeFound("officeAddress.doesNotContain=" + DEFAULT_OFFICE_ADDRESS);

        // Get all the companyList where officeAddress does not contain UPDATED_OFFICE_ADDRESS
        defaultCompanyShouldBeFound("officeAddress.doesNotContain=" + UPDATED_OFFICE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyPhoneNumber equals to DEFAULT_COMPANY_PHONE_NUMBER
        defaultCompanyShouldBeFound("companyPhoneNumber.equals=" + DEFAULT_COMPANY_PHONE_NUMBER);

        // Get all the companyList where companyPhoneNumber equals to UPDATED_COMPANY_PHONE_NUMBER
        defaultCompanyShouldNotBeFound("companyPhoneNumber.equals=" + UPDATED_COMPANY_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyPhoneNumber not equals to DEFAULT_COMPANY_PHONE_NUMBER
        defaultCompanyShouldNotBeFound("companyPhoneNumber.notEquals=" + DEFAULT_COMPANY_PHONE_NUMBER);

        // Get all the companyList where companyPhoneNumber not equals to UPDATED_COMPANY_PHONE_NUMBER
        defaultCompanyShouldBeFound("companyPhoneNumber.notEquals=" + UPDATED_COMPANY_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyPhoneNumber in DEFAULT_COMPANY_PHONE_NUMBER or UPDATED_COMPANY_PHONE_NUMBER
        defaultCompanyShouldBeFound("companyPhoneNumber.in=" + DEFAULT_COMPANY_PHONE_NUMBER + "," + UPDATED_COMPANY_PHONE_NUMBER);

        // Get all the companyList where companyPhoneNumber equals to UPDATED_COMPANY_PHONE_NUMBER
        defaultCompanyShouldNotBeFound("companyPhoneNumber.in=" + UPDATED_COMPANY_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyPhoneNumber is not null
        defaultCompanyShouldBeFound("companyPhoneNumber.specified=true");

        // Get all the companyList where companyPhoneNumber is null
        defaultCompanyShouldNotBeFound("companyPhoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyPhoneNumber contains DEFAULT_COMPANY_PHONE_NUMBER
        defaultCompanyShouldBeFound("companyPhoneNumber.contains=" + DEFAULT_COMPANY_PHONE_NUMBER);

        // Get all the companyList where companyPhoneNumber contains UPDATED_COMPANY_PHONE_NUMBER
        defaultCompanyShouldNotBeFound("companyPhoneNumber.contains=" + UPDATED_COMPANY_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyPhoneNumber does not contain DEFAULT_COMPANY_PHONE_NUMBER
        defaultCompanyShouldNotBeFound("companyPhoneNumber.doesNotContain=" + DEFAULT_COMPANY_PHONE_NUMBER);

        // Get all the companyList where companyPhoneNumber does not contain UPDATED_COMPANY_PHONE_NUMBER
        defaultCompanyShouldBeFound("companyPhoneNumber.doesNotContain=" + UPDATED_COMPANY_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email equals to DEFAULT_EMAIL
        defaultCompanyShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email not equals to DEFAULT_EMAIL
        defaultCompanyShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the companyList where email not equals to UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email is not null
        defaultCompanyShouldBeFound("email.specified=true");

        // Get all the companyList where email is null
        defaultCompanyShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email contains DEFAULT_EMAIL
        defaultCompanyShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the companyList where email contains UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email does not contain DEFAULT_EMAIL
        defaultCompanyShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the companyList where email does not contain UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].dealsIn").value(hasItem(DEFAULT_DEALS_IN)))
            .andExpect(jsonPath("$.[*].officeAddress").value(hasItem(DEFAULT_OFFICE_ADDRESS)))
            .andExpect(jsonPath("$.[*].companyPhoneNumber").value(hasItem(DEFAULT_COMPANY_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .dealsIn(UPDATED_DEALS_IN)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .companyPhoneNumber(UPDATED_COMPANY_PHONE_NUMBER)
            .email(UPDATED_EMAIL);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getDealsIn()).isEqualTo(UPDATED_DEALS_IN);
        assertThat(testCompany.getOfficeAddress()).isEqualTo(UPDATED_OFFICE_ADDRESS);
        assertThat(testCompany.getCompanyPhoneNumber()).isEqualTo(UPDATED_COMPANY_PHONE_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .companyPhoneNumber(UPDATED_COMPANY_PHONE_NUMBER)
            .email(UPDATED_EMAIL);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getDealsIn()).isEqualTo(DEFAULT_DEALS_IN);
        assertThat(testCompany.getOfficeAddress()).isEqualTo(UPDATED_OFFICE_ADDRESS);
        assertThat(testCompany.getCompanyPhoneNumber()).isEqualTo(UPDATED_COMPANY_PHONE_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .dealsIn(UPDATED_DEALS_IN)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .companyPhoneNumber(UPDATED_COMPANY_PHONE_NUMBER)
            .email(UPDATED_EMAIL);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getDealsIn()).isEqualTo(UPDATED_DEALS_IN);
        assertThat(testCompany.getOfficeAddress()).isEqualTo(UPDATED_OFFICE_ADDRESS);
        assertThat(testCompany.getCompanyPhoneNumber()).isEqualTo(UPDATED_COMPANY_PHONE_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
