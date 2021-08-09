package com.makeitgoals.rentalstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.Customer;
import com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer;
import com.makeitgoals.rentalstore.domain.OrderItem;
import com.makeitgoals.rentalstore.domain.Product;
import com.makeitgoals.rentalstore.repository.ItemBalanceByCustomerRepository;
import com.makeitgoals.rentalstore.service.criteria.ItemBalanceByCustomerCriteria;
import com.makeitgoals.rentalstore.service.dto.ItemBalanceByCustomerDTO;
import com.makeitgoals.rentalstore.service.mapper.ItemBalanceByCustomerMapper;
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
 * Integration tests for the {@link ItemBalanceByCustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemBalanceByCustomerResourceIT {

    private static final Integer DEFAULT_OUTSTANDING_BALANCE = 0;
    private static final Integer UPDATED_OUTSTANDING_BALANCE = 1;
    private static final Integer SMALLER_OUTSTANDING_BALANCE = 0 - 1;

    private static final String ENTITY_API_URL = "/api/item-balance-by-customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemBalanceByCustomerRepository itemBalanceByCustomerRepository;

    @Autowired
    private ItemBalanceByCustomerMapper itemBalanceByCustomerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemBalanceByCustomerMockMvc;

    private ItemBalanceByCustomer itemBalanceByCustomer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemBalanceByCustomer createEntity(EntityManager em) {
        ItemBalanceByCustomer itemBalanceByCustomer = new ItemBalanceByCustomer().outstandingBalance(DEFAULT_OUTSTANDING_BALANCE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        itemBalanceByCustomer.setProduct(product);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).get(0);
        }
        itemBalanceByCustomer.setOrderItem(orderItem);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        itemBalanceByCustomer.setCustomer(customer);
        return itemBalanceByCustomer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemBalanceByCustomer createUpdatedEntity(EntityManager em) {
        ItemBalanceByCustomer itemBalanceByCustomer = new ItemBalanceByCustomer().outstandingBalance(UPDATED_OUTSTANDING_BALANCE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        itemBalanceByCustomer.setProduct(product);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createUpdatedEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).get(0);
        }
        itemBalanceByCustomer.setOrderItem(orderItem);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        itemBalanceByCustomer.setCustomer(customer);
        return itemBalanceByCustomer;
    }

    @BeforeEach
    public void initTest() {
        itemBalanceByCustomer = createEntity(em);
    }

    @Test
    @Transactional
    void createItemBalanceByCustomer() throws Exception {
        int databaseSizeBeforeCreate = itemBalanceByCustomerRepository.findAll().size();
        // Create the ItemBalanceByCustomer
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);
        restItemBalanceByCustomerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeCreate + 1);
        ItemBalanceByCustomer testItemBalanceByCustomer = itemBalanceByCustomerList.get(itemBalanceByCustomerList.size() - 1);
        assertThat(testItemBalanceByCustomer.getOutstandingBalance()).isEqualTo(DEFAULT_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void createItemBalanceByCustomerWithExistingId() throws Exception {
        // Create the ItemBalanceByCustomer with an existing ID
        itemBalanceByCustomer.setId(1L);
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);

        int databaseSizeBeforeCreate = itemBalanceByCustomerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemBalanceByCustomerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOutstandingBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemBalanceByCustomerRepository.findAll().size();
        // set the field null
        itemBalanceByCustomer.setOutstandingBalance(null);

        // Create the ItemBalanceByCustomer, which fails.
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);

        restItemBalanceByCustomerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomers() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList
        restItemBalanceByCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemBalanceByCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].outstandingBalance").value(hasItem(DEFAULT_OUTSTANDING_BALANCE)));
    }

    @Test
    @Transactional
    void getItemBalanceByCustomer() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get the itemBalanceByCustomer
        restItemBalanceByCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, itemBalanceByCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemBalanceByCustomer.getId().intValue()))
            .andExpect(jsonPath("$.outstandingBalance").value(DEFAULT_OUTSTANDING_BALANCE));
    }

    @Test
    @Transactional
    void getItemBalanceByCustomersByIdFiltering() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        Long id = itemBalanceByCustomer.getId();

        defaultItemBalanceByCustomerShouldBeFound("id.equals=" + id);
        defaultItemBalanceByCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultItemBalanceByCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultItemBalanceByCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultItemBalanceByCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultItemBalanceByCustomerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOutstandingBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList where outstandingBalance equals to DEFAULT_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldBeFound("outstandingBalance.equals=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the itemBalanceByCustomerList where outstandingBalance equals to UPDATED_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldNotBeFound("outstandingBalance.equals=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOutstandingBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList where outstandingBalance not equals to DEFAULT_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldNotBeFound("outstandingBalance.notEquals=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the itemBalanceByCustomerList where outstandingBalance not equals to UPDATED_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldBeFound("outstandingBalance.notEquals=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOutstandingBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList where outstandingBalance in DEFAULT_OUTSTANDING_BALANCE or UPDATED_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldBeFound(
            "outstandingBalance.in=" + DEFAULT_OUTSTANDING_BALANCE + "," + UPDATED_OUTSTANDING_BALANCE
        );

        // Get all the itemBalanceByCustomerList where outstandingBalance equals to UPDATED_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldNotBeFound("outstandingBalance.in=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOutstandingBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList where outstandingBalance is not null
        defaultItemBalanceByCustomerShouldBeFound("outstandingBalance.specified=true");

        // Get all the itemBalanceByCustomerList where outstandingBalance is null
        defaultItemBalanceByCustomerShouldNotBeFound("outstandingBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOutstandingBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList where outstandingBalance is greater than or equal to DEFAULT_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldBeFound("outstandingBalance.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the itemBalanceByCustomerList where outstandingBalance is greater than or equal to UPDATED_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldNotBeFound("outstandingBalance.greaterThanOrEqual=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOutstandingBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList where outstandingBalance is less than or equal to DEFAULT_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldBeFound("outstandingBalance.lessThanOrEqual=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the itemBalanceByCustomerList where outstandingBalance is less than or equal to SMALLER_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldNotBeFound("outstandingBalance.lessThanOrEqual=" + SMALLER_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOutstandingBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList where outstandingBalance is less than DEFAULT_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldNotBeFound("outstandingBalance.lessThan=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the itemBalanceByCustomerList where outstandingBalance is less than UPDATED_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldBeFound("outstandingBalance.lessThan=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOutstandingBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        // Get all the itemBalanceByCustomerList where outstandingBalance is greater than DEFAULT_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldNotBeFound("outstandingBalance.greaterThan=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the itemBalanceByCustomerList where outstandingBalance is greater than SMALLER_OUTSTANDING_BALANCE
        defaultItemBalanceByCustomerShouldBeFound("outstandingBalance.greaterThan=" + SMALLER_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        itemBalanceByCustomer.setProduct(product);
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);
        Long productId = product.getId();

        // Get all the itemBalanceByCustomerList where product equals to productId
        defaultItemBalanceByCustomerShouldBeFound("productId.equals=" + productId);

        // Get all the itemBalanceByCustomerList where product equals to (productId + 1)
        defaultItemBalanceByCustomerShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByOrderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);
        OrderItem orderItem = OrderItemResourceIT.createEntity(em);
        em.persist(orderItem);
        em.flush();
        itemBalanceByCustomer.setOrderItem(orderItem);
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);
        Long orderItemId = orderItem.getId();

        // Get all the itemBalanceByCustomerList where orderItem equals to orderItemId
        defaultItemBalanceByCustomerShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the itemBalanceByCustomerList where orderItem equals to (orderItemId + 1)
        defaultItemBalanceByCustomerShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllItemBalanceByCustomersByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        itemBalanceByCustomer.setCustomer(customer);
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);
        Long customerId = customer.getId();

        // Get all the itemBalanceByCustomerList where customer equals to customerId
        defaultItemBalanceByCustomerShouldBeFound("customerId.equals=" + customerId);

        // Get all the itemBalanceByCustomerList where customer equals to (customerId + 1)
        defaultItemBalanceByCustomerShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemBalanceByCustomerShouldBeFound(String filter) throws Exception {
        restItemBalanceByCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemBalanceByCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].outstandingBalance").value(hasItem(DEFAULT_OUTSTANDING_BALANCE)));

        // Check, that the count call also returns 1
        restItemBalanceByCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemBalanceByCustomerShouldNotBeFound(String filter) throws Exception {
        restItemBalanceByCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemBalanceByCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingItemBalanceByCustomer() throws Exception {
        // Get the itemBalanceByCustomer
        restItemBalanceByCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemBalanceByCustomer() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();

        // Update the itemBalanceByCustomer
        ItemBalanceByCustomer updatedItemBalanceByCustomer = itemBalanceByCustomerRepository.findById(itemBalanceByCustomer.getId()).get();
        // Disconnect from session so that the updates on updatedItemBalanceByCustomer are not directly saved in db
        em.detach(updatedItemBalanceByCustomer);
        updatedItemBalanceByCustomer.outstandingBalance(UPDATED_OUTSTANDING_BALANCE);
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(updatedItemBalanceByCustomer);

        restItemBalanceByCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemBalanceByCustomerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
        ItemBalanceByCustomer testItemBalanceByCustomer = itemBalanceByCustomerList.get(itemBalanceByCustomerList.size() - 1);
        assertThat(testItemBalanceByCustomer.getOutstandingBalance()).isEqualTo(UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingItemBalanceByCustomer() throws Exception {
        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();
        itemBalanceByCustomer.setId(count.incrementAndGet());

        // Create the ItemBalanceByCustomer
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemBalanceByCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemBalanceByCustomerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemBalanceByCustomer() throws Exception {
        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();
        itemBalanceByCustomer.setId(count.incrementAndGet());

        // Create the ItemBalanceByCustomer
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemBalanceByCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemBalanceByCustomer() throws Exception {
        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();
        itemBalanceByCustomer.setId(count.incrementAndGet());

        // Create the ItemBalanceByCustomer
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemBalanceByCustomerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemBalanceByCustomerWithPatch() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();

        // Update the itemBalanceByCustomer using partial update
        ItemBalanceByCustomer partialUpdatedItemBalanceByCustomer = new ItemBalanceByCustomer();
        partialUpdatedItemBalanceByCustomer.setId(itemBalanceByCustomer.getId());

        restItemBalanceByCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemBalanceByCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemBalanceByCustomer))
            )
            .andExpect(status().isOk());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
        ItemBalanceByCustomer testItemBalanceByCustomer = itemBalanceByCustomerList.get(itemBalanceByCustomerList.size() - 1);
        assertThat(testItemBalanceByCustomer.getOutstandingBalance()).isEqualTo(DEFAULT_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateItemBalanceByCustomerWithPatch() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();

        // Update the itemBalanceByCustomer using partial update
        ItemBalanceByCustomer partialUpdatedItemBalanceByCustomer = new ItemBalanceByCustomer();
        partialUpdatedItemBalanceByCustomer.setId(itemBalanceByCustomer.getId());

        partialUpdatedItemBalanceByCustomer.outstandingBalance(UPDATED_OUTSTANDING_BALANCE);

        restItemBalanceByCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemBalanceByCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemBalanceByCustomer))
            )
            .andExpect(status().isOk());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
        ItemBalanceByCustomer testItemBalanceByCustomer = itemBalanceByCustomerList.get(itemBalanceByCustomerList.size() - 1);
        assertThat(testItemBalanceByCustomer.getOutstandingBalance()).isEqualTo(UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingItemBalanceByCustomer() throws Exception {
        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();
        itemBalanceByCustomer.setId(count.incrementAndGet());

        // Create the ItemBalanceByCustomer
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemBalanceByCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemBalanceByCustomerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemBalanceByCustomer() throws Exception {
        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();
        itemBalanceByCustomer.setId(count.incrementAndGet());

        // Create the ItemBalanceByCustomer
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemBalanceByCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemBalanceByCustomer() throws Exception {
        int databaseSizeBeforeUpdate = itemBalanceByCustomerRepository.findAll().size();
        itemBalanceByCustomer.setId(count.incrementAndGet());

        // Create the ItemBalanceByCustomer
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemBalanceByCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemBalanceByCustomerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemBalanceByCustomer in the database
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemBalanceByCustomer() throws Exception {
        // Initialize the database
        itemBalanceByCustomerRepository.saveAndFlush(itemBalanceByCustomer);

        int databaseSizeBeforeDelete = itemBalanceByCustomerRepository.findAll().size();

        // Delete the itemBalanceByCustomer
        restItemBalanceByCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemBalanceByCustomer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemBalanceByCustomer> itemBalanceByCustomerList = itemBalanceByCustomerRepository.findAll();
        assertThat(itemBalanceByCustomerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
