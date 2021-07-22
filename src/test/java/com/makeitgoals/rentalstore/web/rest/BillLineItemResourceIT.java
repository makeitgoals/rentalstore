package com.makeitgoals.rentalstore.web.rest;

import static com.makeitgoals.rentalstore.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.BillLineItem;
import com.makeitgoals.rentalstore.repository.BillLineItemRepository;
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

    private static final BigDecimal DEFAULT_LINE_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_LINE_AMOUNT = new BigDecimal(1);

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
