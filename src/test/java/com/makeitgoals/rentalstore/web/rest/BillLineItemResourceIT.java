package com.makeitgoals.rentalstore.web.rest;

import static com.makeitgoals.rentalstore.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.Bill;
import com.makeitgoals.rentalstore.domain.BillLineItem;
import com.makeitgoals.rentalstore.domain.Product;
import com.makeitgoals.rentalstore.domain.RentalOrder;
import com.makeitgoals.rentalstore.repository.BillLineItemRepository;
import com.makeitgoals.rentalstore.service.criteria.BillLineItemCriteria;
import com.makeitgoals.rentalstore.service.dto.BillLineItemDTO;
import com.makeitgoals.rentalstore.service.mapper.BillLineItemMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link BillLineItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillLineItemResourceIT {

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_OUTSTANDING_QUANTITY = 0;
    private static final Integer UPDATED_OUTSTANDING_QUANTITY = 1;
    private static final Integer SMALLER_OUTSTANDING_QUANTITY = 0 - 1;

    private static final BigDecimal DEFAULT_LINE_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_LINE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_LINE_AMOUNT = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/bill-line-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillLineItemRepository billLineItemRepository;

    @Autowired
    private BillLineItemMapper billLineItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillLineItemMockMvc;

    private BillLineItem billLineItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillLineItem createEntity(EntityManager em) {
        BillLineItem billLineItem = new BillLineItem()
            .details(DEFAULT_DETAILS)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .outstandingQuantity(DEFAULT_OUTSTANDING_QUANTITY)
            .lineAmount(DEFAULT_LINE_AMOUNT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        billLineItem.setProduct(product);
        // Add required entity
        Bill bill;
        if (TestUtil.findAll(em, Bill.class).isEmpty()) {
            bill = BillResourceIT.createEntity(em);
            em.persist(bill);
            em.flush();
        } else {
            bill = TestUtil.findAll(em, Bill.class).get(0);
        }
        billLineItem.setBill(bill);
        // Add required entity
        RentalOrder rentalOrder;
        if (TestUtil.findAll(em, RentalOrder.class).isEmpty()) {
            rentalOrder = RentalOrderResourceIT.createEntity(em);
            em.persist(rentalOrder);
            em.flush();
        } else {
            rentalOrder = TestUtil.findAll(em, RentalOrder.class).get(0);
        }
        billLineItem.setRentalOrder(rentalOrder);
        return billLineItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillLineItem createUpdatedEntity(EntityManager em) {
        BillLineItem billLineItem = new BillLineItem()
            .details(UPDATED_DETAILS)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .outstandingQuantity(UPDATED_OUTSTANDING_QUANTITY)
            .lineAmount(UPDATED_LINE_AMOUNT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        billLineItem.setProduct(product);
        // Add required entity
        Bill bill;
        if (TestUtil.findAll(em, Bill.class).isEmpty()) {
            bill = BillResourceIT.createUpdatedEntity(em);
            em.persist(bill);
            em.flush();
        } else {
            bill = TestUtil.findAll(em, Bill.class).get(0);
        }
        billLineItem.setBill(bill);
        // Add required entity
        RentalOrder rentalOrder;
        if (TestUtil.findAll(em, RentalOrder.class).isEmpty()) {
            rentalOrder = RentalOrderResourceIT.createUpdatedEntity(em);
            em.persist(rentalOrder);
            em.flush();
        } else {
            rentalOrder = TestUtil.findAll(em, RentalOrder.class).get(0);
        }
        billLineItem.setRentalOrder(rentalOrder);
        return billLineItem;
    }

    @BeforeEach
    public void initTest() {
        billLineItem = createEntity(em);
    }

    @Test
    @Transactional
    void createBillLineItem() throws Exception {
        int databaseSizeBeforeCreate = billLineItemRepository.findAll().size();
        // Create the BillLineItem
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);
        restBillLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeCreate + 1);
        BillLineItem testBillLineItem = billLineItemList.get(billLineItemList.size() - 1);
        assertThat(testBillLineItem.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testBillLineItem.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testBillLineItem.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testBillLineItem.getOutstandingQuantity()).isEqualTo(DEFAULT_OUTSTANDING_QUANTITY);
        assertThat(testBillLineItem.getLineAmount()).isEqualByComparingTo(DEFAULT_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void createBillLineItemWithExistingId() throws Exception {
        // Create the BillLineItem with an existing ID
        billLineItem.setId(1L);
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        int databaseSizeBeforeCreate = billLineItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = billLineItemRepository.findAll().size();
        // set the field null
        billLineItem.setFromDate(null);

        // Create the BillLineItem, which fails.
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        restBillLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = billLineItemRepository.findAll().size();
        // set the field null
        billLineItem.setToDate(null);

        // Create the BillLineItem, which fails.
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        restBillLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutstandingQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = billLineItemRepository.findAll().size();
        // set the field null
        billLineItem.setOutstandingQuantity(null);

        // Create the BillLineItem, which fails.
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        restBillLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLineAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = billLineItemRepository.findAll().size();
        // set the field null
        billLineItem.setLineAmount(null);

        // Create the BillLineItem, which fails.
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        restBillLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBillLineItems() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList
        restBillLineItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billLineItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].outstandingQuantity").value(hasItem(DEFAULT_OUTSTANDING_QUANTITY)))
            .andExpect(jsonPath("$.[*].lineAmount").value(hasItem(sameNumber(DEFAULT_LINE_AMOUNT))));
    }

    @Test
    @Transactional
    void getBillLineItem() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get the billLineItem
        restBillLineItemMockMvc
            .perform(get(ENTITY_API_URL_ID, billLineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billLineItem.getId().intValue()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.outstandingQuantity").value(DEFAULT_OUTSTANDING_QUANTITY))
            .andExpect(jsonPath("$.lineAmount").value(sameNumber(DEFAULT_LINE_AMOUNT)));
    }

    @Test
    @Transactional
    void getBillLineItemsByIdFiltering() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        Long id = billLineItem.getId();

        defaultBillLineItemShouldBeFound("id.equals=" + id);
        defaultBillLineItemShouldNotBeFound("id.notEquals=" + id);

        defaultBillLineItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBillLineItemShouldNotBeFound("id.greaterThan=" + id);

        defaultBillLineItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBillLineItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where details equals to DEFAULT_DETAILS
        defaultBillLineItemShouldBeFound("details.equals=" + DEFAULT_DETAILS);

        // Get all the billLineItemList where details equals to UPDATED_DETAILS
        defaultBillLineItemShouldNotBeFound("details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where details not equals to DEFAULT_DETAILS
        defaultBillLineItemShouldNotBeFound("details.notEquals=" + DEFAULT_DETAILS);

        // Get all the billLineItemList where details not equals to UPDATED_DETAILS
        defaultBillLineItemShouldBeFound("details.notEquals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where details in DEFAULT_DETAILS or UPDATED_DETAILS
        defaultBillLineItemShouldBeFound("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS);

        // Get all the billLineItemList where details equals to UPDATED_DETAILS
        defaultBillLineItemShouldNotBeFound("details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where details is not null
        defaultBillLineItemShouldBeFound("details.specified=true");

        // Get all the billLineItemList where details is null
        defaultBillLineItemShouldNotBeFound("details.specified=false");
    }

    @Test
    @Transactional
    void getAllBillLineItemsByDetailsContainsSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where details contains DEFAULT_DETAILS
        defaultBillLineItemShouldBeFound("details.contains=" + DEFAULT_DETAILS);

        // Get all the billLineItemList where details contains UPDATED_DETAILS
        defaultBillLineItemShouldNotBeFound("details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where details does not contain DEFAULT_DETAILS
        defaultBillLineItemShouldNotBeFound("details.doesNotContain=" + DEFAULT_DETAILS);

        // Get all the billLineItemList where details does not contain UPDATED_DETAILS
        defaultBillLineItemShouldBeFound("details.doesNotContain=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where fromDate equals to DEFAULT_FROM_DATE
        defaultBillLineItemShouldBeFound("fromDate.equals=" + DEFAULT_FROM_DATE);

        // Get all the billLineItemList where fromDate equals to UPDATED_FROM_DATE
        defaultBillLineItemShouldNotBeFound("fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByFromDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where fromDate not equals to DEFAULT_FROM_DATE
        defaultBillLineItemShouldNotBeFound("fromDate.notEquals=" + DEFAULT_FROM_DATE);

        // Get all the billLineItemList where fromDate not equals to UPDATED_FROM_DATE
        defaultBillLineItemShouldBeFound("fromDate.notEquals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where fromDate in DEFAULT_FROM_DATE or UPDATED_FROM_DATE
        defaultBillLineItemShouldBeFound("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE);

        // Get all the billLineItemList where fromDate equals to UPDATED_FROM_DATE
        defaultBillLineItemShouldNotBeFound("fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where fromDate is not null
        defaultBillLineItemShouldBeFound("fromDate.specified=true");

        // Get all the billLineItemList where fromDate is null
        defaultBillLineItemShouldNotBeFound("fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBillLineItemsByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where toDate equals to DEFAULT_TO_DATE
        defaultBillLineItemShouldBeFound("toDate.equals=" + DEFAULT_TO_DATE);

        // Get all the billLineItemList where toDate equals to UPDATED_TO_DATE
        defaultBillLineItemShouldNotBeFound("toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByToDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where toDate not equals to DEFAULT_TO_DATE
        defaultBillLineItemShouldNotBeFound("toDate.notEquals=" + DEFAULT_TO_DATE);

        // Get all the billLineItemList where toDate not equals to UPDATED_TO_DATE
        defaultBillLineItemShouldBeFound("toDate.notEquals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where toDate in DEFAULT_TO_DATE or UPDATED_TO_DATE
        defaultBillLineItemShouldBeFound("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE);

        // Get all the billLineItemList where toDate equals to UPDATED_TO_DATE
        defaultBillLineItemShouldNotBeFound("toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where toDate is not null
        defaultBillLineItemShouldBeFound("toDate.specified=true");

        // Get all the billLineItemList where toDate is null
        defaultBillLineItemShouldNotBeFound("toDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBillLineItemsByOutstandingQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where outstandingQuantity equals to DEFAULT_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldBeFound("outstandingQuantity.equals=" + DEFAULT_OUTSTANDING_QUANTITY);

        // Get all the billLineItemList where outstandingQuantity equals to UPDATED_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldNotBeFound("outstandingQuantity.equals=" + UPDATED_OUTSTANDING_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByOutstandingQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where outstandingQuantity not equals to DEFAULT_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldNotBeFound("outstandingQuantity.notEquals=" + DEFAULT_OUTSTANDING_QUANTITY);

        // Get all the billLineItemList where outstandingQuantity not equals to UPDATED_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldBeFound("outstandingQuantity.notEquals=" + UPDATED_OUTSTANDING_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByOutstandingQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where outstandingQuantity in DEFAULT_OUTSTANDING_QUANTITY or UPDATED_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldBeFound("outstandingQuantity.in=" + DEFAULT_OUTSTANDING_QUANTITY + "," + UPDATED_OUTSTANDING_QUANTITY);

        // Get all the billLineItemList where outstandingQuantity equals to UPDATED_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldNotBeFound("outstandingQuantity.in=" + UPDATED_OUTSTANDING_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByOutstandingQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where outstandingQuantity is not null
        defaultBillLineItemShouldBeFound("outstandingQuantity.specified=true");

        // Get all the billLineItemList where outstandingQuantity is null
        defaultBillLineItemShouldNotBeFound("outstandingQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllBillLineItemsByOutstandingQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where outstandingQuantity is greater than or equal to DEFAULT_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldBeFound("outstandingQuantity.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_QUANTITY);

        // Get all the billLineItemList where outstandingQuantity is greater than or equal to UPDATED_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldNotBeFound("outstandingQuantity.greaterThanOrEqual=" + UPDATED_OUTSTANDING_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByOutstandingQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where outstandingQuantity is less than or equal to DEFAULT_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldBeFound("outstandingQuantity.lessThanOrEqual=" + DEFAULT_OUTSTANDING_QUANTITY);

        // Get all the billLineItemList where outstandingQuantity is less than or equal to SMALLER_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldNotBeFound("outstandingQuantity.lessThanOrEqual=" + SMALLER_OUTSTANDING_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByOutstandingQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where outstandingQuantity is less than DEFAULT_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldNotBeFound("outstandingQuantity.lessThan=" + DEFAULT_OUTSTANDING_QUANTITY);

        // Get all the billLineItemList where outstandingQuantity is less than UPDATED_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldBeFound("outstandingQuantity.lessThan=" + UPDATED_OUTSTANDING_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByOutstandingQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where outstandingQuantity is greater than DEFAULT_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldNotBeFound("outstandingQuantity.greaterThan=" + DEFAULT_OUTSTANDING_QUANTITY);

        // Get all the billLineItemList where outstandingQuantity is greater than SMALLER_OUTSTANDING_QUANTITY
        defaultBillLineItemShouldBeFound("outstandingQuantity.greaterThan=" + SMALLER_OUTSTANDING_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByLineAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where lineAmount equals to DEFAULT_LINE_AMOUNT
        defaultBillLineItemShouldBeFound("lineAmount.equals=" + DEFAULT_LINE_AMOUNT);

        // Get all the billLineItemList where lineAmount equals to UPDATED_LINE_AMOUNT
        defaultBillLineItemShouldNotBeFound("lineAmount.equals=" + UPDATED_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByLineAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where lineAmount not equals to DEFAULT_LINE_AMOUNT
        defaultBillLineItemShouldNotBeFound("lineAmount.notEquals=" + DEFAULT_LINE_AMOUNT);

        // Get all the billLineItemList where lineAmount not equals to UPDATED_LINE_AMOUNT
        defaultBillLineItemShouldBeFound("lineAmount.notEquals=" + UPDATED_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByLineAmountIsInShouldWork() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where lineAmount in DEFAULT_LINE_AMOUNT or UPDATED_LINE_AMOUNT
        defaultBillLineItemShouldBeFound("lineAmount.in=" + DEFAULT_LINE_AMOUNT + "," + UPDATED_LINE_AMOUNT);

        // Get all the billLineItemList where lineAmount equals to UPDATED_LINE_AMOUNT
        defaultBillLineItemShouldNotBeFound("lineAmount.in=" + UPDATED_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByLineAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where lineAmount is not null
        defaultBillLineItemShouldBeFound("lineAmount.specified=true");

        // Get all the billLineItemList where lineAmount is null
        defaultBillLineItemShouldNotBeFound("lineAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBillLineItemsByLineAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where lineAmount is greater than or equal to DEFAULT_LINE_AMOUNT
        defaultBillLineItemShouldBeFound("lineAmount.greaterThanOrEqual=" + DEFAULT_LINE_AMOUNT);

        // Get all the billLineItemList where lineAmount is greater than or equal to UPDATED_LINE_AMOUNT
        defaultBillLineItemShouldNotBeFound("lineAmount.greaterThanOrEqual=" + UPDATED_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByLineAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where lineAmount is less than or equal to DEFAULT_LINE_AMOUNT
        defaultBillLineItemShouldBeFound("lineAmount.lessThanOrEqual=" + DEFAULT_LINE_AMOUNT);

        // Get all the billLineItemList where lineAmount is less than or equal to SMALLER_LINE_AMOUNT
        defaultBillLineItemShouldNotBeFound("lineAmount.lessThanOrEqual=" + SMALLER_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByLineAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where lineAmount is less than DEFAULT_LINE_AMOUNT
        defaultBillLineItemShouldNotBeFound("lineAmount.lessThan=" + DEFAULT_LINE_AMOUNT);

        // Get all the billLineItemList where lineAmount is less than UPDATED_LINE_AMOUNT
        defaultBillLineItemShouldBeFound("lineAmount.lessThan=" + UPDATED_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByLineAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        // Get all the billLineItemList where lineAmount is greater than DEFAULT_LINE_AMOUNT
        defaultBillLineItemShouldNotBeFound("lineAmount.greaterThan=" + DEFAULT_LINE_AMOUNT);

        // Get all the billLineItemList where lineAmount is greater than SMALLER_LINE_AMOUNT
        defaultBillLineItemShouldBeFound("lineAmount.greaterThan=" + SMALLER_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBillLineItemsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        billLineItem.setProduct(product);
        billLineItemRepository.saveAndFlush(billLineItem);
        Long productId = product.getId();

        // Get all the billLineItemList where product equals to productId
        defaultBillLineItemShouldBeFound("productId.equals=" + productId);

        // Get all the billLineItemList where product equals to (productId + 1)
        defaultBillLineItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllBillLineItemsByBillIsEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);
        Bill bill = BillResourceIT.createEntity(em);
        em.persist(bill);
        em.flush();
        billLineItem.setBill(bill);
        billLineItemRepository.saveAndFlush(billLineItem);
        Long billId = bill.getId();

        // Get all the billLineItemList where bill equals to billId
        defaultBillLineItemShouldBeFound("billId.equals=" + billId);

        // Get all the billLineItemList where bill equals to (billId + 1)
        defaultBillLineItemShouldNotBeFound("billId.equals=" + (billId + 1));
    }

    @Test
    @Transactional
    void getAllBillLineItemsByRentalOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);
        RentalOrder rentalOrder = RentalOrderResourceIT.createEntity(em);
        em.persist(rentalOrder);
        em.flush();
        billLineItem.setRentalOrder(rentalOrder);
        billLineItemRepository.saveAndFlush(billLineItem);
        Long rentalOrderId = rentalOrder.getId();

        // Get all the billLineItemList where rentalOrder equals to rentalOrderId
        defaultBillLineItemShouldBeFound("rentalOrderId.equals=" + rentalOrderId);

        // Get all the billLineItemList where rentalOrder equals to (rentalOrderId + 1)
        defaultBillLineItemShouldNotBeFound("rentalOrderId.equals=" + (rentalOrderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBillLineItemShouldBeFound(String filter) throws Exception {
        restBillLineItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billLineItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].outstandingQuantity").value(hasItem(DEFAULT_OUTSTANDING_QUANTITY)))
            .andExpect(jsonPath("$.[*].lineAmount").value(hasItem(sameNumber(DEFAULT_LINE_AMOUNT))));

        // Check, that the count call also returns 1
        restBillLineItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBillLineItemShouldNotBeFound(String filter) throws Exception {
        restBillLineItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBillLineItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBillLineItem() throws Exception {
        // Get the billLineItem
        restBillLineItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBillLineItem() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();

        // Update the billLineItem
        BillLineItem updatedBillLineItem = billLineItemRepository.findById(billLineItem.getId()).get();
        // Disconnect from session so that the updates on updatedBillLineItem are not directly saved in db
        em.detach(updatedBillLineItem);
        updatedBillLineItem
            .details(UPDATED_DETAILS)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .outstandingQuantity(UPDATED_OUTSTANDING_QUANTITY)
            .lineAmount(UPDATED_LINE_AMOUNT);
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(updatedBillLineItem);

        restBillLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billLineItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
        BillLineItem testBillLineItem = billLineItemList.get(billLineItemList.size() - 1);
        assertThat(testBillLineItem.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testBillLineItem.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testBillLineItem.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testBillLineItem.getOutstandingQuantity()).isEqualTo(UPDATED_OUTSTANDING_QUANTITY);
        assertThat(testBillLineItem.getLineAmount()).isEqualTo(UPDATED_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingBillLineItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();
        billLineItem.setId(count.incrementAndGet());

        // Create the BillLineItem
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billLineItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillLineItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();
        billLineItem.setId(count.incrementAndGet());

        // Create the BillLineItem
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillLineItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();
        billLineItem.setId(count.incrementAndGet());

        // Create the BillLineItem
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillLineItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillLineItemWithPatch() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();

        // Update the billLineItem using partial update
        BillLineItem partialUpdatedBillLineItem = new BillLineItem();
        partialUpdatedBillLineItem.setId(billLineItem.getId());

        partialUpdatedBillLineItem.fromDate(UPDATED_FROM_DATE).lineAmount(UPDATED_LINE_AMOUNT);

        restBillLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillLineItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillLineItem))
            )
            .andExpect(status().isOk());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
        BillLineItem testBillLineItem = billLineItemList.get(billLineItemList.size() - 1);
        assertThat(testBillLineItem.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testBillLineItem.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testBillLineItem.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testBillLineItem.getOutstandingQuantity()).isEqualTo(DEFAULT_OUTSTANDING_QUANTITY);
        assertThat(testBillLineItem.getLineAmount()).isEqualByComparingTo(UPDATED_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateBillLineItemWithPatch() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();

        // Update the billLineItem using partial update
        BillLineItem partialUpdatedBillLineItem = new BillLineItem();
        partialUpdatedBillLineItem.setId(billLineItem.getId());

        partialUpdatedBillLineItem
            .details(UPDATED_DETAILS)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .outstandingQuantity(UPDATED_OUTSTANDING_QUANTITY)
            .lineAmount(UPDATED_LINE_AMOUNT);

        restBillLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillLineItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillLineItem))
            )
            .andExpect(status().isOk());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
        BillLineItem testBillLineItem = billLineItemList.get(billLineItemList.size() - 1);
        assertThat(testBillLineItem.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testBillLineItem.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testBillLineItem.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testBillLineItem.getOutstandingQuantity()).isEqualTo(UPDATED_OUTSTANDING_QUANTITY);
        assertThat(testBillLineItem.getLineAmount()).isEqualByComparingTo(UPDATED_LINE_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingBillLineItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();
        billLineItem.setId(count.incrementAndGet());

        // Create the BillLineItem
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billLineItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillLineItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();
        billLineItem.setId(count.incrementAndGet());

        // Create the BillLineItem
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillLineItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemRepository.findAll().size();
        billLineItem.setId(count.incrementAndGet());

        // Create the BillLineItem
        BillLineItemDTO billLineItemDTO = billLineItemMapper.toDto(billLineItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillLineItem in the database
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillLineItem() throws Exception {
        // Initialize the database
        billLineItemRepository.saveAndFlush(billLineItem);

        int databaseSizeBeforeDelete = billLineItemRepository.findAll().size();

        // Delete the billLineItem
        restBillLineItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, billLineItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BillLineItem> billLineItemList = billLineItemRepository.findAll();
        assertThat(billLineItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
