package com.makeitgoals.rentalstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.Bill;
import com.makeitgoals.rentalstore.domain.Customer;
import com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer;
import com.makeitgoals.rentalstore.domain.Payment;
import com.makeitgoals.rentalstore.domain.RentalOrder;
import com.makeitgoals.rentalstore.repository.CustomerRepository;
import com.makeitgoals.rentalstore.service.criteria.CustomerCriteria;
import com.makeitgoals.rentalstore.service.dto.CustomerDTO;
import com.makeitgoals.rentalstore.service.mapper.CustomerMapper;
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
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_SITE_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "E&7R(@dZ,.2WD";
    private static final String UPDATED_EMAIL = "k\"@L:m1D.)R";

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .customerName(DEFAULT_CUSTOMER_NAME)
            .contactName(DEFAULT_CONTACT_NAME)
            .fatherName(DEFAULT_FATHER_NAME)
            .ownerName(DEFAULT_OWNER_NAME)
            .siteAddress(DEFAULT_SITE_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .customerName(UPDATED_CUSTOMER_NAME)
            .contactName(UPDATED_CONTACT_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .ownerName(UPDATED_OWNER_NAME)
            .siteAddress(UPDATED_SITE_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomer.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testCustomer.getFatherName()).isEqualTo(DEFAULT_FATHER_NAME);
        assertThat(testCustomer.getOwnerName()).isEqualTo(DEFAULT_OWNER_NAME);
        assertThat(testCustomer.getSiteAddress()).isEqualTo(DEFAULT_SITE_ADDRESS);
        assertThat(testCustomer.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerName(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].siteAddress").value(hasItem(DEFAULT_SITE_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME))
            .andExpect(jsonPath("$.siteAddress").value(DEFAULT_SITE_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerShouldBeFound("id.equals=" + id);
        defaultCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName not equals to DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.notEquals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName not equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.notEquals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the customerList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName is not null
        defaultCustomerShouldBeFound("customerName.specified=true");

        // Get all the customerList where customerName is null
        defaultCustomerShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName contains UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactName equals to DEFAULT_CONTACT_NAME
        defaultCustomerShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the customerList where contactName equals to UPDATED_CONTACT_NAME
        defaultCustomerShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultCustomerShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the customerList where contactName not equals to UPDATED_CONTACT_NAME
        defaultCustomerShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultCustomerShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the customerList where contactName equals to UPDATED_CONTACT_NAME
        defaultCustomerShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactName is not null
        defaultCustomerShouldBeFound("contactName.specified=true");

        // Get all the customerList where contactName is null
        defaultCustomerShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByContactNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactName contains DEFAULT_CONTACT_NAME
        defaultCustomerShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the customerList where contactName contains UPDATED_CONTACT_NAME
        defaultCustomerShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultCustomerShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the customerList where contactName does not contain UPDATED_CONTACT_NAME
        defaultCustomerShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFatherNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fatherName equals to DEFAULT_FATHER_NAME
        defaultCustomerShouldBeFound("fatherName.equals=" + DEFAULT_FATHER_NAME);

        // Get all the customerList where fatherName equals to UPDATED_FATHER_NAME
        defaultCustomerShouldNotBeFound("fatherName.equals=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFatherNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fatherName not equals to DEFAULT_FATHER_NAME
        defaultCustomerShouldNotBeFound("fatherName.notEquals=" + DEFAULT_FATHER_NAME);

        // Get all the customerList where fatherName not equals to UPDATED_FATHER_NAME
        defaultCustomerShouldBeFound("fatherName.notEquals=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFatherNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fatherName in DEFAULT_FATHER_NAME or UPDATED_FATHER_NAME
        defaultCustomerShouldBeFound("fatherName.in=" + DEFAULT_FATHER_NAME + "," + UPDATED_FATHER_NAME);

        // Get all the customerList where fatherName equals to UPDATED_FATHER_NAME
        defaultCustomerShouldNotBeFound("fatherName.in=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFatherNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fatherName is not null
        defaultCustomerShouldBeFound("fatherName.specified=true");

        // Get all the customerList where fatherName is null
        defaultCustomerShouldNotBeFound("fatherName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByFatherNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fatherName contains DEFAULT_FATHER_NAME
        defaultCustomerShouldBeFound("fatherName.contains=" + DEFAULT_FATHER_NAME);

        // Get all the customerList where fatherName contains UPDATED_FATHER_NAME
        defaultCustomerShouldNotBeFound("fatherName.contains=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFatherNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fatherName does not contain DEFAULT_FATHER_NAME
        defaultCustomerShouldNotBeFound("fatherName.doesNotContain=" + DEFAULT_FATHER_NAME);

        // Get all the customerList where fatherName does not contain UPDATED_FATHER_NAME
        defaultCustomerShouldBeFound("fatherName.doesNotContain=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByOwnerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where ownerName equals to DEFAULT_OWNER_NAME
        defaultCustomerShouldBeFound("ownerName.equals=" + DEFAULT_OWNER_NAME);

        // Get all the customerList where ownerName equals to UPDATED_OWNER_NAME
        defaultCustomerShouldNotBeFound("ownerName.equals=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByOwnerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where ownerName not equals to DEFAULT_OWNER_NAME
        defaultCustomerShouldNotBeFound("ownerName.notEquals=" + DEFAULT_OWNER_NAME);

        // Get all the customerList where ownerName not equals to UPDATED_OWNER_NAME
        defaultCustomerShouldBeFound("ownerName.notEquals=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByOwnerNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where ownerName in DEFAULT_OWNER_NAME or UPDATED_OWNER_NAME
        defaultCustomerShouldBeFound("ownerName.in=" + DEFAULT_OWNER_NAME + "," + UPDATED_OWNER_NAME);

        // Get all the customerList where ownerName equals to UPDATED_OWNER_NAME
        defaultCustomerShouldNotBeFound("ownerName.in=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByOwnerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where ownerName is not null
        defaultCustomerShouldBeFound("ownerName.specified=true");

        // Get all the customerList where ownerName is null
        defaultCustomerShouldNotBeFound("ownerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByOwnerNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where ownerName contains DEFAULT_OWNER_NAME
        defaultCustomerShouldBeFound("ownerName.contains=" + DEFAULT_OWNER_NAME);

        // Get all the customerList where ownerName contains UPDATED_OWNER_NAME
        defaultCustomerShouldNotBeFound("ownerName.contains=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByOwnerNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where ownerName does not contain DEFAULT_OWNER_NAME
        defaultCustomerShouldNotBeFound("ownerName.doesNotContain=" + DEFAULT_OWNER_NAME);

        // Get all the customerList where ownerName does not contain UPDATED_OWNER_NAME
        defaultCustomerShouldBeFound("ownerName.doesNotContain=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersBySiteAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where siteAddress equals to DEFAULT_SITE_ADDRESS
        defaultCustomerShouldBeFound("siteAddress.equals=" + DEFAULT_SITE_ADDRESS);

        // Get all the customerList where siteAddress equals to UPDATED_SITE_ADDRESS
        defaultCustomerShouldNotBeFound("siteAddress.equals=" + UPDATED_SITE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersBySiteAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where siteAddress not equals to DEFAULT_SITE_ADDRESS
        defaultCustomerShouldNotBeFound("siteAddress.notEquals=" + DEFAULT_SITE_ADDRESS);

        // Get all the customerList where siteAddress not equals to UPDATED_SITE_ADDRESS
        defaultCustomerShouldBeFound("siteAddress.notEquals=" + UPDATED_SITE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersBySiteAddressIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where siteAddress in DEFAULT_SITE_ADDRESS or UPDATED_SITE_ADDRESS
        defaultCustomerShouldBeFound("siteAddress.in=" + DEFAULT_SITE_ADDRESS + "," + UPDATED_SITE_ADDRESS);

        // Get all the customerList where siteAddress equals to UPDATED_SITE_ADDRESS
        defaultCustomerShouldNotBeFound("siteAddress.in=" + UPDATED_SITE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersBySiteAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where siteAddress is not null
        defaultCustomerShouldBeFound("siteAddress.specified=true");

        // Get all the customerList where siteAddress is null
        defaultCustomerShouldNotBeFound("siteAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersBySiteAddressContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where siteAddress contains DEFAULT_SITE_ADDRESS
        defaultCustomerShouldBeFound("siteAddress.contains=" + DEFAULT_SITE_ADDRESS);

        // Get all the customerList where siteAddress contains UPDATED_SITE_ADDRESS
        defaultCustomerShouldNotBeFound("siteAddress.contains=" + UPDATED_SITE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersBySiteAddressNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where siteAddress does not contain DEFAULT_SITE_ADDRESS
        defaultCustomerShouldNotBeFound("siteAddress.doesNotContain=" + DEFAULT_SITE_ADDRESS);

        // Get all the customerList where siteAddress does not contain UPDATED_SITE_ADDRESS
        defaultCustomerShouldBeFound("siteAddress.doesNotContain=" + UPDATED_SITE_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultCustomerShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the customerList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultCustomerShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultCustomerShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the customerList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultCustomerShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultCustomerShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the customerList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultCustomerShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber is not null
        defaultCustomerShouldBeFound("phoneNumber.specified=true");

        // Get all the customerList where phoneNumber is null
        defaultCustomerShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultCustomerShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the customerList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultCustomerShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultCustomerShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the customerList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultCustomerShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email equals to DEFAULT_EMAIL
        defaultCustomerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email not equals to DEFAULT_EMAIL
        defaultCustomerShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the customerList where email not equals to UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email is not null
        defaultCustomerShouldBeFound("email.specified=true");

        // Get all the customerList where email is null
        defaultCustomerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByEmailContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email contains DEFAULT_EMAIL
        defaultCustomerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the customerList where email contains UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email does not contain DEFAULT_EMAIL
        defaultCustomerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the customerList where email does not contain UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByItemBalanceByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        ItemBalanceByCustomer itemBalanceByCustomer = ItemBalanceByCustomerResourceIT.createEntity(em);
        em.persist(itemBalanceByCustomer);
        em.flush();
        customer.addItemBalanceByCustomer(itemBalanceByCustomer);
        customerRepository.saveAndFlush(customer);
        Long itemBalanceByCustomerId = itemBalanceByCustomer.getId();

        // Get all the customerList where itemBalanceByCustomer equals to itemBalanceByCustomerId
        defaultCustomerShouldBeFound("itemBalanceByCustomerId.equals=" + itemBalanceByCustomerId);

        // Get all the customerList where itemBalanceByCustomer equals to (itemBalanceByCustomerId + 1)
        defaultCustomerShouldNotBeFound("itemBalanceByCustomerId.equals=" + (itemBalanceByCustomerId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByRentalOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        RentalOrder rentalOrder = RentalOrderResourceIT.createEntity(em);
        em.persist(rentalOrder);
        em.flush();
        customer.addRentalOrder(rentalOrder);
        customerRepository.saveAndFlush(customer);
        Long rentalOrderId = rentalOrder.getId();

        // Get all the customerList where rentalOrder equals to rentalOrderId
        defaultCustomerShouldBeFound("rentalOrderId.equals=" + rentalOrderId);

        // Get all the customerList where rentalOrder equals to (rentalOrderId + 1)
        defaultCustomerShouldNotBeFound("rentalOrderId.equals=" + (rentalOrderId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByBillIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Bill bill = BillResourceIT.createEntity(em);
        em.persist(bill);
        em.flush();
        customer.addBill(bill);
        customerRepository.saveAndFlush(customer);
        Long billId = bill.getId();

        // Get all the customerList where bill equals to billId
        defaultCustomerShouldBeFound("billId.equals=" + billId);

        // Get all the customerList where bill equals to (billId + 1)
        defaultCustomerShouldNotBeFound("billId.equals=" + (billId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        customer.addPayment(payment);
        customerRepository.saveAndFlush(customer);
        Long paymentId = payment.getId();

        // Get all the customerList where payment equals to paymentId
        defaultCustomerShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the customerList where payment equals to (paymentId + 1)
        defaultCustomerShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].siteAddress").value(hasItem(DEFAULT_SITE_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .customerName(UPDATED_CUSTOMER_NAME)
            .contactName(UPDATED_CONTACT_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .ownerName(UPDATED_OWNER_NAME)
            .siteAddress(UPDATED_SITE_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomer.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testCustomer.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testCustomer.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testCustomer.getSiteAddress()).isEqualTo(UPDATED_SITE_ADDRESS);
        assertThat(testCustomer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .contactName(UPDATED_CONTACT_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .ownerName(UPDATED_OWNER_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomer.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testCustomer.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testCustomer.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testCustomer.getSiteAddress()).isEqualTo(DEFAULT_SITE_ADDRESS);
        assertThat(testCustomer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .customerName(UPDATED_CUSTOMER_NAME)
            .contactName(UPDATED_CONTACT_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .ownerName(UPDATED_OWNER_NAME)
            .siteAddress(UPDATED_SITE_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomer.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testCustomer.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testCustomer.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testCustomer.getSiteAddress()).isEqualTo(UPDATED_SITE_ADDRESS);
        assertThat(testCustomer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
