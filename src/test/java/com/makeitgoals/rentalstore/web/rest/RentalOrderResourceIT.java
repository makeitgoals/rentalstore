package com.makeitgoals.rentalstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.BillLineItem;
import com.makeitgoals.rentalstore.domain.Customer;
import com.makeitgoals.rentalstore.domain.OrderItem;
import com.makeitgoals.rentalstore.domain.RentalOrder;
import com.makeitgoals.rentalstore.domain.enumeration.OrderStatus;
import com.makeitgoals.rentalstore.repository.RentalOrderRepository;
import com.makeitgoals.rentalstore.service.criteria.RentalOrderCriteria;
import com.makeitgoals.rentalstore.service.dto.RentalOrderDTO;
import com.makeitgoals.rentalstore.service.mapper.RentalOrderMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link RentalOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RentalOrderResourceIT {

    private static final Instant DEFAULT_RENTAL_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RENTAL_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RENTAL_RETURN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RENTAL_RETURN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_RENTAL_ORDER_STATUS = OrderStatus.COMPLETED;
    private static final OrderStatus UPDATED_RENTAL_ORDER_STATUS = OrderStatus.PENDING;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rental-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RentalOrderRepository rentalOrderRepository;

    @Autowired
    private RentalOrderMapper rentalOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentalOrderMockMvc;

    private RentalOrder rentalOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentalOrder createEntity(EntityManager em) {
        RentalOrder rentalOrder = new RentalOrder()
            .rentalIssueDate(DEFAULT_RENTAL_ISSUE_DATE)
            .rentalReturnDate(DEFAULT_RENTAL_RETURN_DATE)
            .rentalOrderStatus(DEFAULT_RENTAL_ORDER_STATUS)
            .code(DEFAULT_CODE);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        rentalOrder.setCustomer(customer);
        return rentalOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentalOrder createUpdatedEntity(EntityManager em) {
        RentalOrder rentalOrder = new RentalOrder()
            .rentalIssueDate(UPDATED_RENTAL_ISSUE_DATE)
            .rentalReturnDate(UPDATED_RENTAL_RETURN_DATE)
            .rentalOrderStatus(UPDATED_RENTAL_ORDER_STATUS)
            .code(UPDATED_CODE);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        rentalOrder.setCustomer(customer);
        return rentalOrder;
    }

    @BeforeEach
    public void initTest() {
        rentalOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createRentalOrder() throws Exception {
        int databaseSizeBeforeCreate = rentalOrderRepository.findAll().size();
        // Create the RentalOrder
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);
        restRentalOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeCreate + 1);
        RentalOrder testRentalOrder = rentalOrderList.get(rentalOrderList.size() - 1);
        assertThat(testRentalOrder.getRentalIssueDate()).isEqualTo(DEFAULT_RENTAL_ISSUE_DATE);
        assertThat(testRentalOrder.getRentalReturnDate()).isEqualTo(DEFAULT_RENTAL_RETURN_DATE);
        assertThat(testRentalOrder.getRentalOrderStatus()).isEqualTo(DEFAULT_RENTAL_ORDER_STATUS);
        assertThat(testRentalOrder.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createRentalOrderWithExistingId() throws Exception {
        // Create the RentalOrder with an existing ID
        rentalOrder.setId(1L);
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        int databaseSizeBeforeCreate = rentalOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRentalOrderStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalOrderRepository.findAll().size();
        // set the field null
        rentalOrder.setRentalOrderStatus(null);

        // Create the RentalOrder, which fails.
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        restRentalOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalOrderRepository.findAll().size();
        // set the field null
        rentalOrder.setCode(null);

        // Create the RentalOrder, which fails.
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        restRentalOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRentalOrders() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList
        restRentalOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentalOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].rentalIssueDate").value(hasItem(DEFAULT_RENTAL_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].rentalReturnDate").value(hasItem(DEFAULT_RENTAL_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].rentalOrderStatus").value(hasItem(DEFAULT_RENTAL_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getRentalOrder() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get the rentalOrder
        restRentalOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, rentalOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rentalOrder.getId().intValue()))
            .andExpect(jsonPath("$.rentalIssueDate").value(DEFAULT_RENTAL_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.rentalReturnDate").value(DEFAULT_RENTAL_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.rentalOrderStatus").value(DEFAULT_RENTAL_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getRentalOrdersByIdFiltering() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        Long id = rentalOrder.getId();

        defaultRentalOrderShouldBeFound("id.equals=" + id);
        defaultRentalOrderShouldNotBeFound("id.notEquals=" + id);

        defaultRentalOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRentalOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultRentalOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRentalOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalIssueDate equals to DEFAULT_RENTAL_ISSUE_DATE
        defaultRentalOrderShouldBeFound("rentalIssueDate.equals=" + DEFAULT_RENTAL_ISSUE_DATE);

        // Get all the rentalOrderList where rentalIssueDate equals to UPDATED_RENTAL_ISSUE_DATE
        defaultRentalOrderShouldNotBeFound("rentalIssueDate.equals=" + UPDATED_RENTAL_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalIssueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalIssueDate not equals to DEFAULT_RENTAL_ISSUE_DATE
        defaultRentalOrderShouldNotBeFound("rentalIssueDate.notEquals=" + DEFAULT_RENTAL_ISSUE_DATE);

        // Get all the rentalOrderList where rentalIssueDate not equals to UPDATED_RENTAL_ISSUE_DATE
        defaultRentalOrderShouldBeFound("rentalIssueDate.notEquals=" + UPDATED_RENTAL_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalIssueDate in DEFAULT_RENTAL_ISSUE_DATE or UPDATED_RENTAL_ISSUE_DATE
        defaultRentalOrderShouldBeFound("rentalIssueDate.in=" + DEFAULT_RENTAL_ISSUE_DATE + "," + UPDATED_RENTAL_ISSUE_DATE);

        // Get all the rentalOrderList where rentalIssueDate equals to UPDATED_RENTAL_ISSUE_DATE
        defaultRentalOrderShouldNotBeFound("rentalIssueDate.in=" + UPDATED_RENTAL_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalIssueDate is not null
        defaultRentalOrderShouldBeFound("rentalIssueDate.specified=true");

        // Get all the rentalOrderList where rentalIssueDate is null
        defaultRentalOrderShouldNotBeFound("rentalIssueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalReturnDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalReturnDate equals to DEFAULT_RENTAL_RETURN_DATE
        defaultRentalOrderShouldBeFound("rentalReturnDate.equals=" + DEFAULT_RENTAL_RETURN_DATE);

        // Get all the rentalOrderList where rentalReturnDate equals to UPDATED_RENTAL_RETURN_DATE
        defaultRentalOrderShouldNotBeFound("rentalReturnDate.equals=" + UPDATED_RENTAL_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalReturnDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalReturnDate not equals to DEFAULT_RENTAL_RETURN_DATE
        defaultRentalOrderShouldNotBeFound("rentalReturnDate.notEquals=" + DEFAULT_RENTAL_RETURN_DATE);

        // Get all the rentalOrderList where rentalReturnDate not equals to UPDATED_RENTAL_RETURN_DATE
        defaultRentalOrderShouldBeFound("rentalReturnDate.notEquals=" + UPDATED_RENTAL_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalReturnDateIsInShouldWork() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalReturnDate in DEFAULT_RENTAL_RETURN_DATE or UPDATED_RENTAL_RETURN_DATE
        defaultRentalOrderShouldBeFound("rentalReturnDate.in=" + DEFAULT_RENTAL_RETURN_DATE + "," + UPDATED_RENTAL_RETURN_DATE);

        // Get all the rentalOrderList where rentalReturnDate equals to UPDATED_RENTAL_RETURN_DATE
        defaultRentalOrderShouldNotBeFound("rentalReturnDate.in=" + UPDATED_RENTAL_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalReturnDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalReturnDate is not null
        defaultRentalOrderShouldBeFound("rentalReturnDate.specified=true");

        // Get all the rentalOrderList where rentalReturnDate is null
        defaultRentalOrderShouldNotBeFound("rentalReturnDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalOrderStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalOrderStatus equals to DEFAULT_RENTAL_ORDER_STATUS
        defaultRentalOrderShouldBeFound("rentalOrderStatus.equals=" + DEFAULT_RENTAL_ORDER_STATUS);

        // Get all the rentalOrderList where rentalOrderStatus equals to UPDATED_RENTAL_ORDER_STATUS
        defaultRentalOrderShouldNotBeFound("rentalOrderStatus.equals=" + UPDATED_RENTAL_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalOrderStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalOrderStatus not equals to DEFAULT_RENTAL_ORDER_STATUS
        defaultRentalOrderShouldNotBeFound("rentalOrderStatus.notEquals=" + DEFAULT_RENTAL_ORDER_STATUS);

        // Get all the rentalOrderList where rentalOrderStatus not equals to UPDATED_RENTAL_ORDER_STATUS
        defaultRentalOrderShouldBeFound("rentalOrderStatus.notEquals=" + UPDATED_RENTAL_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalOrderStatusIsInShouldWork() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalOrderStatus in DEFAULT_RENTAL_ORDER_STATUS or UPDATED_RENTAL_ORDER_STATUS
        defaultRentalOrderShouldBeFound("rentalOrderStatus.in=" + DEFAULT_RENTAL_ORDER_STATUS + "," + UPDATED_RENTAL_ORDER_STATUS);

        // Get all the rentalOrderList where rentalOrderStatus equals to UPDATED_RENTAL_ORDER_STATUS
        defaultRentalOrderShouldNotBeFound("rentalOrderStatus.in=" + UPDATED_RENTAL_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByRentalOrderStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where rentalOrderStatus is not null
        defaultRentalOrderShouldBeFound("rentalOrderStatus.specified=true");

        // Get all the rentalOrderList where rentalOrderStatus is null
        defaultRentalOrderShouldNotBeFound("rentalOrderStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalOrdersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where code equals to DEFAULT_CODE
        defaultRentalOrderShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the rentalOrderList where code equals to UPDATED_CODE
        defaultRentalOrderShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where code not equals to DEFAULT_CODE
        defaultRentalOrderShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the rentalOrderList where code not equals to UPDATED_CODE
        defaultRentalOrderShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where code in DEFAULT_CODE or UPDATED_CODE
        defaultRentalOrderShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the rentalOrderList where code equals to UPDATED_CODE
        defaultRentalOrderShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where code is not null
        defaultRentalOrderShouldBeFound("code.specified=true");

        // Get all the rentalOrderList where code is null
        defaultRentalOrderShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalOrdersByCodeContainsSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where code contains DEFAULT_CODE
        defaultRentalOrderShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the rentalOrderList where code contains UPDATED_CODE
        defaultRentalOrderShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        // Get all the rentalOrderList where code does not contain DEFAULT_CODE
        defaultRentalOrderShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the rentalOrderList where code does not contain UPDATED_CODE
        defaultRentalOrderShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRentalOrdersByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        rentalOrder.setCustomer(customer);
        rentalOrderRepository.saveAndFlush(rentalOrder);
        Long customerId = customer.getId();

        // Get all the rentalOrderList where customer equals to customerId
        defaultRentalOrderShouldBeFound("customerId.equals=" + customerId);

        // Get all the rentalOrderList where customer equals to (customerId + 1)
        defaultRentalOrderShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllRentalOrdersByBillLineItemIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);
        BillLineItem billLineItem = BillLineItemResourceIT.createEntity(em);
        em.persist(billLineItem);
        em.flush();
        rentalOrder.addBillLineItem(billLineItem);
        rentalOrderRepository.saveAndFlush(rentalOrder);
        Long billLineItemId = billLineItem.getId();

        // Get all the rentalOrderList where billLineItem equals to billLineItemId
        defaultRentalOrderShouldBeFound("billLineItemId.equals=" + billLineItemId);

        // Get all the rentalOrderList where billLineItem equals to (billLineItemId + 1)
        defaultRentalOrderShouldNotBeFound("billLineItemId.equals=" + (billLineItemId + 1));
    }

    @Test
    @Transactional
    void getAllRentalOrdersByOrderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);
        OrderItem orderItem = OrderItemResourceIT.createEntity(em);
        em.persist(orderItem);
        em.flush();
        rentalOrder.addOrderItem(orderItem);
        rentalOrderRepository.saveAndFlush(rentalOrder);
        Long orderItemId = orderItem.getId();

        // Get all the rentalOrderList where orderItem equals to orderItemId
        defaultRentalOrderShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the rentalOrderList where orderItem equals to (orderItemId + 1)
        defaultRentalOrderShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRentalOrderShouldBeFound(String filter) throws Exception {
        restRentalOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentalOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].rentalIssueDate").value(hasItem(DEFAULT_RENTAL_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].rentalReturnDate").value(hasItem(DEFAULT_RENTAL_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].rentalOrderStatus").value(hasItem(DEFAULT_RENTAL_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restRentalOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRentalOrderShouldNotBeFound(String filter) throws Exception {
        restRentalOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRentalOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRentalOrder() throws Exception {
        // Get the rentalOrder
        restRentalOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRentalOrder() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();

        // Update the rentalOrder
        RentalOrder updatedRentalOrder = rentalOrderRepository.findById(rentalOrder.getId()).get();
        // Disconnect from session so that the updates on updatedRentalOrder are not directly saved in db
        em.detach(updatedRentalOrder);
        updatedRentalOrder
            .rentalIssueDate(UPDATED_RENTAL_ISSUE_DATE)
            .rentalReturnDate(UPDATED_RENTAL_RETURN_DATE)
            .rentalOrderStatus(UPDATED_RENTAL_ORDER_STATUS)
            .code(UPDATED_CODE);
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(updatedRentalOrder);

        restRentalOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentalOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
        RentalOrder testRentalOrder = rentalOrderList.get(rentalOrderList.size() - 1);
        assertThat(testRentalOrder.getRentalIssueDate()).isEqualTo(UPDATED_RENTAL_ISSUE_DATE);
        assertThat(testRentalOrder.getRentalReturnDate()).isEqualTo(UPDATED_RENTAL_RETURN_DATE);
        assertThat(testRentalOrder.getRentalOrderStatus()).isEqualTo(UPDATED_RENTAL_ORDER_STATUS);
        assertThat(testRentalOrder.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingRentalOrder() throws Exception {
        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();
        rentalOrder.setId(count.incrementAndGet());

        // Create the RentalOrder
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentalOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRentalOrder() throws Exception {
        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();
        rentalOrder.setId(count.incrementAndGet());

        // Create the RentalOrder
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRentalOrder() throws Exception {
        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();
        rentalOrder.setId(count.incrementAndGet());

        // Create the RentalOrder
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRentalOrderWithPatch() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();

        // Update the rentalOrder using partial update
        RentalOrder partialUpdatedRentalOrder = new RentalOrder();
        partialUpdatedRentalOrder.setId(rentalOrder.getId());

        restRentalOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRentalOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRentalOrder))
            )
            .andExpect(status().isOk());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
        RentalOrder testRentalOrder = rentalOrderList.get(rentalOrderList.size() - 1);
        assertThat(testRentalOrder.getRentalIssueDate()).isEqualTo(DEFAULT_RENTAL_ISSUE_DATE);
        assertThat(testRentalOrder.getRentalReturnDate()).isEqualTo(DEFAULT_RENTAL_RETURN_DATE);
        assertThat(testRentalOrder.getRentalOrderStatus()).isEqualTo(DEFAULT_RENTAL_ORDER_STATUS);
        assertThat(testRentalOrder.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateRentalOrderWithPatch() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();

        // Update the rentalOrder using partial update
        RentalOrder partialUpdatedRentalOrder = new RentalOrder();
        partialUpdatedRentalOrder.setId(rentalOrder.getId());

        partialUpdatedRentalOrder
            .rentalIssueDate(UPDATED_RENTAL_ISSUE_DATE)
            .rentalReturnDate(UPDATED_RENTAL_RETURN_DATE)
            .rentalOrderStatus(UPDATED_RENTAL_ORDER_STATUS)
            .code(UPDATED_CODE);

        restRentalOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRentalOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRentalOrder))
            )
            .andExpect(status().isOk());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
        RentalOrder testRentalOrder = rentalOrderList.get(rentalOrderList.size() - 1);
        assertThat(testRentalOrder.getRentalIssueDate()).isEqualTo(UPDATED_RENTAL_ISSUE_DATE);
        assertThat(testRentalOrder.getRentalReturnDate()).isEqualTo(UPDATED_RENTAL_RETURN_DATE);
        assertThat(testRentalOrder.getRentalOrderStatus()).isEqualTo(UPDATED_RENTAL_ORDER_STATUS);
        assertThat(testRentalOrder.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingRentalOrder() throws Exception {
        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();
        rentalOrder.setId(count.incrementAndGet());

        // Create the RentalOrder
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rentalOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRentalOrder() throws Exception {
        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();
        rentalOrder.setId(count.incrementAndGet());

        // Create the RentalOrder
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRentalOrder() throws Exception {
        int databaseSizeBeforeUpdate = rentalOrderRepository.findAll().size();
        rentalOrder.setId(count.incrementAndGet());

        // Create the RentalOrder
        RentalOrderDTO rentalOrderDTO = rentalOrderMapper.toDto(rentalOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalOrderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rentalOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RentalOrder in the database
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRentalOrder() throws Exception {
        // Initialize the database
        rentalOrderRepository.saveAndFlush(rentalOrder);

        int databaseSizeBeforeDelete = rentalOrderRepository.findAll().size();

        // Delete the rentalOrder
        restRentalOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, rentalOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RentalOrder> rentalOrderList = rentalOrderRepository.findAll();
        assertThat(rentalOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
