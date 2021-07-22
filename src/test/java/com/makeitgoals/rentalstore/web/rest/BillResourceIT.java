package com.makeitgoals.rentalstore.web.rest;

import static com.makeitgoals.rentalstore.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.Bill;
import com.makeitgoals.rentalstore.domain.Customer;
import com.makeitgoals.rentalstore.domain.enumeration.BillStatus;
import com.makeitgoals.rentalstore.repository.BillRepository;
import com.makeitgoals.rentalstore.service.criteria.BillCriteria;
import com.makeitgoals.rentalstore.service.dto.BillDTO;
import com.makeitgoals.rentalstore.service.mapper.BillMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link BillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_COST_BILL_NUMBER = 1;
    private static final Integer UPDATED_COST_BILL_NUMBER = 2;
    private static final Integer SMALLER_COST_BILL_NUMBER = 1 - 1;

    private static final BillStatus DEFAULT_BILL_STATUS = BillStatus.PENDING;
    private static final BillStatus UPDATED_BILL_STATUS = BillStatus.PAID;

    private static final BigDecimal DEFAULT_BILL_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_BILL_TOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_BILL_TOTAL = new BigDecimal(0 - 1);

    private static final Long DEFAULT_TAX_PERCENT = 0L;
    private static final Long UPDATED_TAX_PERCENT = 1L;
    private static final Long SMALLER_TAX_PERCENT = 0L - 1L;

    private static final BigDecimal DEFAULT_BILL_TOTAL_WITH_TAX = new BigDecimal(0);
    private static final BigDecimal UPDATED_BILL_TOTAL_WITH_TAX = new BigDecimal(1);
    private static final BigDecimal SMALLER_BILL_TOTAL_WITH_TAX = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/bills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillMockMvc;

    private Bill bill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createEntity(EntityManager em) {
        Bill bill = new Bill()
            .code(DEFAULT_CODE)
            .costBillNumber(DEFAULT_COST_BILL_NUMBER)
            .billStatus(DEFAULT_BILL_STATUS)
            .billTotal(DEFAULT_BILL_TOTAL)
            .taxPercent(DEFAULT_TAX_PERCENT)
            .billTotalWithTax(DEFAULT_BILL_TOTAL_WITH_TAX);
        return bill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createUpdatedEntity(EntityManager em) {
        Bill bill = new Bill()
            .code(UPDATED_CODE)
            .costBillNumber(UPDATED_COST_BILL_NUMBER)
            .billStatus(UPDATED_BILL_STATUS)
            .billTotal(UPDATED_BILL_TOTAL)
            .taxPercent(UPDATED_TAX_PERCENT)
            .billTotalWithTax(UPDATED_BILL_TOTAL_WITH_TAX);
        return bill;
    }

    @BeforeEach
    public void initTest() {
        bill = createEntity(em);
    }

    @Test
    @Transactional
    void createBill() throws Exception {
        int databaseSizeBeforeCreate = billRepository.findAll().size();
        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);
        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isCreated());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeCreate + 1);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBill.getCostBillNumber()).isEqualTo(DEFAULT_COST_BILL_NUMBER);
        assertThat(testBill.getBillStatus()).isEqualTo(DEFAULT_BILL_STATUS);
        assertThat(testBill.getBillTotal()).isEqualByComparingTo(DEFAULT_BILL_TOTAL);
        assertThat(testBill.getTaxPercent()).isEqualTo(DEFAULT_TAX_PERCENT);
        assertThat(testBill.getBillTotalWithTax()).isEqualByComparingTo(DEFAULT_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void createBillWithExistingId() throws Exception {
        // Create the Bill with an existing ID
        bill.setId(1L);
        BillDTO billDTO = billMapper.toDto(bill);

        int databaseSizeBeforeCreate = billRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = billRepository.findAll().size();
        // set the field null
        bill.setCode(null);

        // Create the Bill, which fails.
        BillDTO billDTO = billMapper.toDto(bill);

        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBillStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = billRepository.findAll().size();
        // set the field null
        bill.setBillStatus(null);

        // Create the Bill, which fails.
        BillDTO billDTO = billMapper.toDto(bill);

        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBillTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = billRepository.findAll().size();
        // set the field null
        bill.setBillTotal(null);

        // Create the Bill, which fails.
        BillDTO billDTO = billMapper.toDto(bill);

        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBills() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList
        restBillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].costBillNumber").value(hasItem(DEFAULT_COST_BILL_NUMBER)))
            .andExpect(jsonPath("$.[*].billStatus").value(hasItem(DEFAULT_BILL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].billTotal").value(hasItem(sameNumber(DEFAULT_BILL_TOTAL))))
            .andExpect(jsonPath("$.[*].taxPercent").value(hasItem(DEFAULT_TAX_PERCENT.intValue())))
            .andExpect(jsonPath("$.[*].billTotalWithTax").value(hasItem(sameNumber(DEFAULT_BILL_TOTAL_WITH_TAX))));
    }

    @Test
    @Transactional
    void getBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get the bill
        restBillMockMvc
            .perform(get(ENTITY_API_URL_ID, bill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bill.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.costBillNumber").value(DEFAULT_COST_BILL_NUMBER))
            .andExpect(jsonPath("$.billStatus").value(DEFAULT_BILL_STATUS.toString()))
            .andExpect(jsonPath("$.billTotal").value(sameNumber(DEFAULT_BILL_TOTAL)))
            .andExpect(jsonPath("$.taxPercent").value(DEFAULT_TAX_PERCENT.intValue()))
            .andExpect(jsonPath("$.billTotalWithTax").value(sameNumber(DEFAULT_BILL_TOTAL_WITH_TAX)));
    }

    @Test
    @Transactional
    void getBillsByIdFiltering() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        Long id = bill.getId();

        defaultBillShouldBeFound("id.equals=" + id);
        defaultBillShouldNotBeFound("id.notEquals=" + id);

        defaultBillShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBillShouldNotBeFound("id.greaterThan=" + id);

        defaultBillShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBillShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBillsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where code equals to DEFAULT_CODE
        defaultBillShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the billList where code equals to UPDATED_CODE
        defaultBillShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBillsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where code not equals to DEFAULT_CODE
        defaultBillShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the billList where code not equals to UPDATED_CODE
        defaultBillShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBillsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBillShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the billList where code equals to UPDATED_CODE
        defaultBillShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBillsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where code is not null
        defaultBillShouldBeFound("code.specified=true");

        // Get all the billList where code is null
        defaultBillShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByCodeContainsSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where code contains DEFAULT_CODE
        defaultBillShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the billList where code contains UPDATED_CODE
        defaultBillShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBillsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where code does not contain DEFAULT_CODE
        defaultBillShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the billList where code does not contain UPDATED_CODE
        defaultBillShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBillsByCostBillNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where costBillNumber equals to DEFAULT_COST_BILL_NUMBER
        defaultBillShouldBeFound("costBillNumber.equals=" + DEFAULT_COST_BILL_NUMBER);

        // Get all the billList where costBillNumber equals to UPDATED_COST_BILL_NUMBER
        defaultBillShouldNotBeFound("costBillNumber.equals=" + UPDATED_COST_BILL_NUMBER);
    }

    @Test
    @Transactional
    void getAllBillsByCostBillNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where costBillNumber not equals to DEFAULT_COST_BILL_NUMBER
        defaultBillShouldNotBeFound("costBillNumber.notEquals=" + DEFAULT_COST_BILL_NUMBER);

        // Get all the billList where costBillNumber not equals to UPDATED_COST_BILL_NUMBER
        defaultBillShouldBeFound("costBillNumber.notEquals=" + UPDATED_COST_BILL_NUMBER);
    }

    @Test
    @Transactional
    void getAllBillsByCostBillNumberIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where costBillNumber in DEFAULT_COST_BILL_NUMBER or UPDATED_COST_BILL_NUMBER
        defaultBillShouldBeFound("costBillNumber.in=" + DEFAULT_COST_BILL_NUMBER + "," + UPDATED_COST_BILL_NUMBER);

        // Get all the billList where costBillNumber equals to UPDATED_COST_BILL_NUMBER
        defaultBillShouldNotBeFound("costBillNumber.in=" + UPDATED_COST_BILL_NUMBER);
    }

    @Test
    @Transactional
    void getAllBillsByCostBillNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where costBillNumber is not null
        defaultBillShouldBeFound("costBillNumber.specified=true");

        // Get all the billList where costBillNumber is null
        defaultBillShouldNotBeFound("costBillNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByCostBillNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where costBillNumber is greater than or equal to DEFAULT_COST_BILL_NUMBER
        defaultBillShouldBeFound("costBillNumber.greaterThanOrEqual=" + DEFAULT_COST_BILL_NUMBER);

        // Get all the billList where costBillNumber is greater than or equal to UPDATED_COST_BILL_NUMBER
        defaultBillShouldNotBeFound("costBillNumber.greaterThanOrEqual=" + UPDATED_COST_BILL_NUMBER);
    }

    @Test
    @Transactional
    void getAllBillsByCostBillNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where costBillNumber is less than or equal to DEFAULT_COST_BILL_NUMBER
        defaultBillShouldBeFound("costBillNumber.lessThanOrEqual=" + DEFAULT_COST_BILL_NUMBER);

        // Get all the billList where costBillNumber is less than or equal to SMALLER_COST_BILL_NUMBER
        defaultBillShouldNotBeFound("costBillNumber.lessThanOrEqual=" + SMALLER_COST_BILL_NUMBER);
    }

    @Test
    @Transactional
    void getAllBillsByCostBillNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where costBillNumber is less than DEFAULT_COST_BILL_NUMBER
        defaultBillShouldNotBeFound("costBillNumber.lessThan=" + DEFAULT_COST_BILL_NUMBER);

        // Get all the billList where costBillNumber is less than UPDATED_COST_BILL_NUMBER
        defaultBillShouldBeFound("costBillNumber.lessThan=" + UPDATED_COST_BILL_NUMBER);
    }

    @Test
    @Transactional
    void getAllBillsByCostBillNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where costBillNumber is greater than DEFAULT_COST_BILL_NUMBER
        defaultBillShouldNotBeFound("costBillNumber.greaterThan=" + DEFAULT_COST_BILL_NUMBER);

        // Get all the billList where costBillNumber is greater than SMALLER_COST_BILL_NUMBER
        defaultBillShouldBeFound("costBillNumber.greaterThan=" + SMALLER_COST_BILL_NUMBER);
    }

    @Test
    @Transactional
    void getAllBillsByBillStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billStatus equals to DEFAULT_BILL_STATUS
        defaultBillShouldBeFound("billStatus.equals=" + DEFAULT_BILL_STATUS);

        // Get all the billList where billStatus equals to UPDATED_BILL_STATUS
        defaultBillShouldNotBeFound("billStatus.equals=" + UPDATED_BILL_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByBillStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billStatus not equals to DEFAULT_BILL_STATUS
        defaultBillShouldNotBeFound("billStatus.notEquals=" + DEFAULT_BILL_STATUS);

        // Get all the billList where billStatus not equals to UPDATED_BILL_STATUS
        defaultBillShouldBeFound("billStatus.notEquals=" + UPDATED_BILL_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByBillStatusIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billStatus in DEFAULT_BILL_STATUS or UPDATED_BILL_STATUS
        defaultBillShouldBeFound("billStatus.in=" + DEFAULT_BILL_STATUS + "," + UPDATED_BILL_STATUS);

        // Get all the billList where billStatus equals to UPDATED_BILL_STATUS
        defaultBillShouldNotBeFound("billStatus.in=" + UPDATED_BILL_STATUS);
    }

    @Test
    @Transactional
    void getAllBillsByBillStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billStatus is not null
        defaultBillShouldBeFound("billStatus.specified=true");

        // Get all the billList where billStatus is null
        defaultBillShouldNotBeFound("billStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotal equals to DEFAULT_BILL_TOTAL
        defaultBillShouldBeFound("billTotal.equals=" + DEFAULT_BILL_TOTAL);

        // Get all the billList where billTotal equals to UPDATED_BILL_TOTAL
        defaultBillShouldNotBeFound("billTotal.equals=" + UPDATED_BILL_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotal not equals to DEFAULT_BILL_TOTAL
        defaultBillShouldNotBeFound("billTotal.notEquals=" + DEFAULT_BILL_TOTAL);

        // Get all the billList where billTotal not equals to UPDATED_BILL_TOTAL
        defaultBillShouldBeFound("billTotal.notEquals=" + UPDATED_BILL_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotal in DEFAULT_BILL_TOTAL or UPDATED_BILL_TOTAL
        defaultBillShouldBeFound("billTotal.in=" + DEFAULT_BILL_TOTAL + "," + UPDATED_BILL_TOTAL);

        // Get all the billList where billTotal equals to UPDATED_BILL_TOTAL
        defaultBillShouldNotBeFound("billTotal.in=" + UPDATED_BILL_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotal is not null
        defaultBillShouldBeFound("billTotal.specified=true");

        // Get all the billList where billTotal is null
        defaultBillShouldNotBeFound("billTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotal is greater than or equal to DEFAULT_BILL_TOTAL
        defaultBillShouldBeFound("billTotal.greaterThanOrEqual=" + DEFAULT_BILL_TOTAL);

        // Get all the billList where billTotal is greater than or equal to UPDATED_BILL_TOTAL
        defaultBillShouldNotBeFound("billTotal.greaterThanOrEqual=" + UPDATED_BILL_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotal is less than or equal to DEFAULT_BILL_TOTAL
        defaultBillShouldBeFound("billTotal.lessThanOrEqual=" + DEFAULT_BILL_TOTAL);

        // Get all the billList where billTotal is less than or equal to SMALLER_BILL_TOTAL
        defaultBillShouldNotBeFound("billTotal.lessThanOrEqual=" + SMALLER_BILL_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotal is less than DEFAULT_BILL_TOTAL
        defaultBillShouldNotBeFound("billTotal.lessThan=" + DEFAULT_BILL_TOTAL);

        // Get all the billList where billTotal is less than UPDATED_BILL_TOTAL
        defaultBillShouldBeFound("billTotal.lessThan=" + UPDATED_BILL_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotal is greater than DEFAULT_BILL_TOTAL
        defaultBillShouldNotBeFound("billTotal.greaterThan=" + DEFAULT_BILL_TOTAL);

        // Get all the billList where billTotal is greater than SMALLER_BILL_TOTAL
        defaultBillShouldBeFound("billTotal.greaterThan=" + SMALLER_BILL_TOTAL);
    }

    @Test
    @Transactional
    void getAllBillsByTaxPercentIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where taxPercent equals to DEFAULT_TAX_PERCENT
        defaultBillShouldBeFound("taxPercent.equals=" + DEFAULT_TAX_PERCENT);

        // Get all the billList where taxPercent equals to UPDATED_TAX_PERCENT
        defaultBillShouldNotBeFound("taxPercent.equals=" + UPDATED_TAX_PERCENT);
    }

    @Test
    @Transactional
    void getAllBillsByTaxPercentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where taxPercent not equals to DEFAULT_TAX_PERCENT
        defaultBillShouldNotBeFound("taxPercent.notEquals=" + DEFAULT_TAX_PERCENT);

        // Get all the billList where taxPercent not equals to UPDATED_TAX_PERCENT
        defaultBillShouldBeFound("taxPercent.notEquals=" + UPDATED_TAX_PERCENT);
    }

    @Test
    @Transactional
    void getAllBillsByTaxPercentIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where taxPercent in DEFAULT_TAX_PERCENT or UPDATED_TAX_PERCENT
        defaultBillShouldBeFound("taxPercent.in=" + DEFAULT_TAX_PERCENT + "," + UPDATED_TAX_PERCENT);

        // Get all the billList where taxPercent equals to UPDATED_TAX_PERCENT
        defaultBillShouldNotBeFound("taxPercent.in=" + UPDATED_TAX_PERCENT);
    }

    @Test
    @Transactional
    void getAllBillsByTaxPercentIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where taxPercent is not null
        defaultBillShouldBeFound("taxPercent.specified=true");

        // Get all the billList where taxPercent is null
        defaultBillShouldNotBeFound("taxPercent.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByTaxPercentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where taxPercent is greater than or equal to DEFAULT_TAX_PERCENT
        defaultBillShouldBeFound("taxPercent.greaterThanOrEqual=" + DEFAULT_TAX_PERCENT);

        // Get all the billList where taxPercent is greater than or equal to UPDATED_TAX_PERCENT
        defaultBillShouldNotBeFound("taxPercent.greaterThanOrEqual=" + UPDATED_TAX_PERCENT);
    }

    @Test
    @Transactional
    void getAllBillsByTaxPercentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where taxPercent is less than or equal to DEFAULT_TAX_PERCENT
        defaultBillShouldBeFound("taxPercent.lessThanOrEqual=" + DEFAULT_TAX_PERCENT);

        // Get all the billList where taxPercent is less than or equal to SMALLER_TAX_PERCENT
        defaultBillShouldNotBeFound("taxPercent.lessThanOrEqual=" + SMALLER_TAX_PERCENT);
    }

    @Test
    @Transactional
    void getAllBillsByTaxPercentIsLessThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where taxPercent is less than DEFAULT_TAX_PERCENT
        defaultBillShouldNotBeFound("taxPercent.lessThan=" + DEFAULT_TAX_PERCENT);

        // Get all the billList where taxPercent is less than UPDATED_TAX_PERCENT
        defaultBillShouldBeFound("taxPercent.lessThan=" + UPDATED_TAX_PERCENT);
    }

    @Test
    @Transactional
    void getAllBillsByTaxPercentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where taxPercent is greater than DEFAULT_TAX_PERCENT
        defaultBillShouldNotBeFound("taxPercent.greaterThan=" + DEFAULT_TAX_PERCENT);

        // Get all the billList where taxPercent is greater than SMALLER_TAX_PERCENT
        defaultBillShouldBeFound("taxPercent.greaterThan=" + SMALLER_TAX_PERCENT);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalWithTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotalWithTax equals to DEFAULT_BILL_TOTAL_WITH_TAX
        defaultBillShouldBeFound("billTotalWithTax.equals=" + DEFAULT_BILL_TOTAL_WITH_TAX);

        // Get all the billList where billTotalWithTax equals to UPDATED_BILL_TOTAL_WITH_TAX
        defaultBillShouldNotBeFound("billTotalWithTax.equals=" + UPDATED_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalWithTaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotalWithTax not equals to DEFAULT_BILL_TOTAL_WITH_TAX
        defaultBillShouldNotBeFound("billTotalWithTax.notEquals=" + DEFAULT_BILL_TOTAL_WITH_TAX);

        // Get all the billList where billTotalWithTax not equals to UPDATED_BILL_TOTAL_WITH_TAX
        defaultBillShouldBeFound("billTotalWithTax.notEquals=" + UPDATED_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalWithTaxIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotalWithTax in DEFAULT_BILL_TOTAL_WITH_TAX or UPDATED_BILL_TOTAL_WITH_TAX
        defaultBillShouldBeFound("billTotalWithTax.in=" + DEFAULT_BILL_TOTAL_WITH_TAX + "," + UPDATED_BILL_TOTAL_WITH_TAX);

        // Get all the billList where billTotalWithTax equals to UPDATED_BILL_TOTAL_WITH_TAX
        defaultBillShouldNotBeFound("billTotalWithTax.in=" + UPDATED_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalWithTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotalWithTax is not null
        defaultBillShouldBeFound("billTotalWithTax.specified=true");

        // Get all the billList where billTotalWithTax is null
        defaultBillShouldNotBeFound("billTotalWithTax.specified=false");
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalWithTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotalWithTax is greater than or equal to DEFAULT_BILL_TOTAL_WITH_TAX
        defaultBillShouldBeFound("billTotalWithTax.greaterThanOrEqual=" + DEFAULT_BILL_TOTAL_WITH_TAX);

        // Get all the billList where billTotalWithTax is greater than or equal to UPDATED_BILL_TOTAL_WITH_TAX
        defaultBillShouldNotBeFound("billTotalWithTax.greaterThanOrEqual=" + UPDATED_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalWithTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotalWithTax is less than or equal to DEFAULT_BILL_TOTAL_WITH_TAX
        defaultBillShouldBeFound("billTotalWithTax.lessThanOrEqual=" + DEFAULT_BILL_TOTAL_WITH_TAX);

        // Get all the billList where billTotalWithTax is less than or equal to SMALLER_BILL_TOTAL_WITH_TAX
        defaultBillShouldNotBeFound("billTotalWithTax.lessThanOrEqual=" + SMALLER_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalWithTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotalWithTax is less than DEFAULT_BILL_TOTAL_WITH_TAX
        defaultBillShouldNotBeFound("billTotalWithTax.lessThan=" + DEFAULT_BILL_TOTAL_WITH_TAX);

        // Get all the billList where billTotalWithTax is less than UPDATED_BILL_TOTAL_WITH_TAX
        defaultBillShouldBeFound("billTotalWithTax.lessThan=" + UPDATED_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void getAllBillsByBillTotalWithTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where billTotalWithTax is greater than DEFAULT_BILL_TOTAL_WITH_TAX
        defaultBillShouldNotBeFound("billTotalWithTax.greaterThan=" + DEFAULT_BILL_TOTAL_WITH_TAX);

        // Get all the billList where billTotalWithTax is greater than SMALLER_BILL_TOTAL_WITH_TAX
        defaultBillShouldBeFound("billTotalWithTax.greaterThan=" + SMALLER_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void getAllBillsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        bill.setCustomer(customer);
        billRepository.saveAndFlush(bill);
        Long customerId = customer.getId();

        // Get all the billList where customer equals to customerId
        defaultBillShouldBeFound("customerId.equals=" + customerId);

        // Get all the billList where customer equals to (customerId + 1)
        defaultBillShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBillShouldBeFound(String filter) throws Exception {
        restBillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].costBillNumber").value(hasItem(DEFAULT_COST_BILL_NUMBER)))
            .andExpect(jsonPath("$.[*].billStatus").value(hasItem(DEFAULT_BILL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].billTotal").value(hasItem(sameNumber(DEFAULT_BILL_TOTAL))))
            .andExpect(jsonPath("$.[*].taxPercent").value(hasItem(DEFAULT_TAX_PERCENT.intValue())))
            .andExpect(jsonPath("$.[*].billTotalWithTax").value(hasItem(sameNumber(DEFAULT_BILL_TOTAL_WITH_TAX))));

        // Check, that the count call also returns 1
        restBillMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBillShouldNotBeFound(String filter) throws Exception {
        restBillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBillMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBill() throws Exception {
        // Get the bill
        restBillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill
        Bill updatedBill = billRepository.findById(bill.getId()).get();
        // Disconnect from session so that the updates on updatedBill are not directly saved in db
        em.detach(updatedBill);
        updatedBill
            .code(UPDATED_CODE)
            .costBillNumber(UPDATED_COST_BILL_NUMBER)
            .billStatus(UPDATED_BILL_STATUS)
            .billTotal(UPDATED_BILL_TOTAL)
            .taxPercent(UPDATED_TAX_PERCENT)
            .billTotalWithTax(UPDATED_BILL_TOTAL_WITH_TAX);
        BillDTO billDTO = billMapper.toDto(updatedBill);

        restBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBill.getCostBillNumber()).isEqualTo(UPDATED_COST_BILL_NUMBER);
        assertThat(testBill.getBillStatus()).isEqualTo(UPDATED_BILL_STATUS);
        assertThat(testBill.getBillTotal()).isEqualTo(UPDATED_BILL_TOTAL);
        assertThat(testBill.getTaxPercent()).isEqualTo(UPDATED_TAX_PERCENT);
        assertThat(testBill.getBillTotalWithTax()).isEqualTo(UPDATED_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void putNonExistingBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillWithPatch() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill using partial update
        Bill partialUpdatedBill = new Bill();
        partialUpdatedBill.setId(bill.getId());

        partialUpdatedBill.billTotal(UPDATED_BILL_TOTAL).taxPercent(UPDATED_TAX_PERCENT).billTotalWithTax(UPDATED_BILL_TOTAL_WITH_TAX);

        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBill))
            )
            .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBill.getCostBillNumber()).isEqualTo(DEFAULT_COST_BILL_NUMBER);
        assertThat(testBill.getBillStatus()).isEqualTo(DEFAULT_BILL_STATUS);
        assertThat(testBill.getBillTotal()).isEqualByComparingTo(UPDATED_BILL_TOTAL);
        assertThat(testBill.getTaxPercent()).isEqualTo(UPDATED_TAX_PERCENT);
        assertThat(testBill.getBillTotalWithTax()).isEqualByComparingTo(UPDATED_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void fullUpdateBillWithPatch() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill using partial update
        Bill partialUpdatedBill = new Bill();
        partialUpdatedBill.setId(bill.getId());

        partialUpdatedBill
            .code(UPDATED_CODE)
            .costBillNumber(UPDATED_COST_BILL_NUMBER)
            .billStatus(UPDATED_BILL_STATUS)
            .billTotal(UPDATED_BILL_TOTAL)
            .taxPercent(UPDATED_TAX_PERCENT)
            .billTotalWithTax(UPDATED_BILL_TOTAL_WITH_TAX);

        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBill))
            )
            .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBill.getCostBillNumber()).isEqualTo(UPDATED_COST_BILL_NUMBER);
        assertThat(testBill.getBillStatus()).isEqualTo(UPDATED_BILL_STATUS);
        assertThat(testBill.getBillTotal()).isEqualByComparingTo(UPDATED_BILL_TOTAL);
        assertThat(testBill.getTaxPercent()).isEqualTo(UPDATED_TAX_PERCENT);
        assertThat(testBill.getBillTotalWithTax()).isEqualByComparingTo(UPDATED_BILL_TOTAL_WITH_TAX);
    }

    @Test
    @Transactional
    void patchNonExistingBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeDelete = billRepository.findAll().size();

        // Delete the bill
        restBillMockMvc
            .perform(delete(ENTITY_API_URL_ID, bill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
