package com.makeitgoals.rentalstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.BillLineItem;
import com.makeitgoals.rentalstore.domain.BillLineItemToOrderItem;
import com.makeitgoals.rentalstore.domain.OrderItem;
import com.makeitgoals.rentalstore.domain.RentalOrder;
import com.makeitgoals.rentalstore.repository.BillLineItemToOrderItemRepository;
import com.makeitgoals.rentalstore.service.dto.BillLineItemToOrderItemDTO;
import com.makeitgoals.rentalstore.service.mapper.BillLineItemToOrderItemMapper;
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
 * Integration tests for the {@link BillLineItemToOrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillLineItemToOrderItemResourceIT {

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bill-line-item-to-order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillLineItemToOrderItemRepository billLineItemToOrderItemRepository;

    @Autowired
    private BillLineItemToOrderItemMapper billLineItemToOrderItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillLineItemToOrderItemMockMvc;

    private BillLineItemToOrderItem billLineItemToOrderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillLineItemToOrderItem createEntity(EntityManager em) {
        BillLineItemToOrderItem billLineItemToOrderItem = new BillLineItemToOrderItem().details(DEFAULT_DETAILS);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).get(0);
        }
        billLineItemToOrderItem.setOrderItem(orderItem);
        // Add required entity
        RentalOrder rentalOrder;
        if (TestUtil.findAll(em, RentalOrder.class).isEmpty()) {
            rentalOrder = RentalOrderResourceIT.createEntity(em);
            em.persist(rentalOrder);
            em.flush();
        } else {
            rentalOrder = TestUtil.findAll(em, RentalOrder.class).get(0);
        }
        billLineItemToOrderItem.setRentalOrder(rentalOrder);
        // Add required entity
        BillLineItem billLineItem;
        if (TestUtil.findAll(em, BillLineItem.class).isEmpty()) {
            billLineItem = BillLineItemResourceIT.createEntity(em);
            em.persist(billLineItem);
            em.flush();
        } else {
            billLineItem = TestUtil.findAll(em, BillLineItem.class).get(0);
        }
        billLineItemToOrderItem.setBillLineItem(billLineItem);
        return billLineItemToOrderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillLineItemToOrderItem createUpdatedEntity(EntityManager em) {
        BillLineItemToOrderItem billLineItemToOrderItem = new BillLineItemToOrderItem().details(UPDATED_DETAILS);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createUpdatedEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).get(0);
        }
        billLineItemToOrderItem.setOrderItem(orderItem);
        // Add required entity
        RentalOrder rentalOrder;
        if (TestUtil.findAll(em, RentalOrder.class).isEmpty()) {
            rentalOrder = RentalOrderResourceIT.createUpdatedEntity(em);
            em.persist(rentalOrder);
            em.flush();
        } else {
            rentalOrder = TestUtil.findAll(em, RentalOrder.class).get(0);
        }
        billLineItemToOrderItem.setRentalOrder(rentalOrder);
        // Add required entity
        BillLineItem billLineItem;
        if (TestUtil.findAll(em, BillLineItem.class).isEmpty()) {
            billLineItem = BillLineItemResourceIT.createUpdatedEntity(em);
            em.persist(billLineItem);
            em.flush();
        } else {
            billLineItem = TestUtil.findAll(em, BillLineItem.class).get(0);
        }
        billLineItemToOrderItem.setBillLineItem(billLineItem);
        return billLineItemToOrderItem;
    }

    @BeforeEach
    public void initTest() {
        billLineItemToOrderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createBillLineItemToOrderItem() throws Exception {
        int databaseSizeBeforeCreate = billLineItemToOrderItemRepository.findAll().size();
        // Create the BillLineItemToOrderItem
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);
        restBillLineItemToOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeCreate + 1);
        BillLineItemToOrderItem testBillLineItemToOrderItem = billLineItemToOrderItemList.get(billLineItemToOrderItemList.size() - 1);
        assertThat(testBillLineItemToOrderItem.getDetails()).isEqualTo(DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    void createBillLineItemToOrderItemWithExistingId() throws Exception {
        // Create the BillLineItemToOrderItem with an existing ID
        billLineItemToOrderItem.setId(1L);
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);

        int databaseSizeBeforeCreate = billLineItemToOrderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillLineItemToOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBillLineItemToOrderItems() throws Exception {
        // Initialize the database
        billLineItemToOrderItemRepository.saveAndFlush(billLineItemToOrderItem);

        // Get all the billLineItemToOrderItemList
        restBillLineItemToOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billLineItemToOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));
    }

    @Test
    @Transactional
    void getBillLineItemToOrderItem() throws Exception {
        // Initialize the database
        billLineItemToOrderItemRepository.saveAndFlush(billLineItemToOrderItem);

        // Get the billLineItemToOrderItem
        restBillLineItemToOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, billLineItemToOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billLineItemToOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS));
    }

    @Test
    @Transactional
    void getNonExistingBillLineItemToOrderItem() throws Exception {
        // Get the billLineItemToOrderItem
        restBillLineItemToOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBillLineItemToOrderItem() throws Exception {
        // Initialize the database
        billLineItemToOrderItemRepository.saveAndFlush(billLineItemToOrderItem);

        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();

        // Update the billLineItemToOrderItem
        BillLineItemToOrderItem updatedBillLineItemToOrderItem = billLineItemToOrderItemRepository
            .findById(billLineItemToOrderItem.getId())
            .get();
        // Disconnect from session so that the updates on updatedBillLineItemToOrderItem are not directly saved in db
        em.detach(updatedBillLineItemToOrderItem);
        updatedBillLineItemToOrderItem.details(UPDATED_DETAILS);
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(updatedBillLineItemToOrderItem);

        restBillLineItemToOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billLineItemToOrderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
        BillLineItemToOrderItem testBillLineItemToOrderItem = billLineItemToOrderItemList.get(billLineItemToOrderItemList.size() - 1);
        assertThat(testBillLineItemToOrderItem.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void putNonExistingBillLineItemToOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();
        billLineItemToOrderItem.setId(count.incrementAndGet());

        // Create the BillLineItemToOrderItem
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillLineItemToOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billLineItemToOrderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillLineItemToOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();
        billLineItemToOrderItem.setId(count.incrementAndGet());

        // Create the BillLineItemToOrderItem
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillLineItemToOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillLineItemToOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();
        billLineItemToOrderItem.setId(count.incrementAndGet());

        // Create the BillLineItemToOrderItem
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillLineItemToOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillLineItemToOrderItemWithPatch() throws Exception {
        // Initialize the database
        billLineItemToOrderItemRepository.saveAndFlush(billLineItemToOrderItem);

        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();

        // Update the billLineItemToOrderItem using partial update
        BillLineItemToOrderItem partialUpdatedBillLineItemToOrderItem = new BillLineItemToOrderItem();
        partialUpdatedBillLineItemToOrderItem.setId(billLineItemToOrderItem.getId());

        restBillLineItemToOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillLineItemToOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillLineItemToOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
        BillLineItemToOrderItem testBillLineItemToOrderItem = billLineItemToOrderItemList.get(billLineItemToOrderItemList.size() - 1);
        assertThat(testBillLineItemToOrderItem.getDetails()).isEqualTo(DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateBillLineItemToOrderItemWithPatch() throws Exception {
        // Initialize the database
        billLineItemToOrderItemRepository.saveAndFlush(billLineItemToOrderItem);

        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();

        // Update the billLineItemToOrderItem using partial update
        BillLineItemToOrderItem partialUpdatedBillLineItemToOrderItem = new BillLineItemToOrderItem();
        partialUpdatedBillLineItemToOrderItem.setId(billLineItemToOrderItem.getId());

        partialUpdatedBillLineItemToOrderItem.details(UPDATED_DETAILS);

        restBillLineItemToOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillLineItemToOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillLineItemToOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
        BillLineItemToOrderItem testBillLineItemToOrderItem = billLineItemToOrderItemList.get(billLineItemToOrderItemList.size() - 1);
        assertThat(testBillLineItemToOrderItem.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingBillLineItemToOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();
        billLineItemToOrderItem.setId(count.incrementAndGet());

        // Create the BillLineItemToOrderItem
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillLineItemToOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billLineItemToOrderItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillLineItemToOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();
        billLineItemToOrderItem.setId(count.incrementAndGet());

        // Create the BillLineItemToOrderItem
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillLineItemToOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillLineItemToOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = billLineItemToOrderItemRepository.findAll().size();
        billLineItemToOrderItem.setId(count.incrementAndGet());

        // Create the BillLineItemToOrderItem
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillLineItemToOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billLineItemToOrderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillLineItemToOrderItem in the database
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillLineItemToOrderItem() throws Exception {
        // Initialize the database
        billLineItemToOrderItemRepository.saveAndFlush(billLineItemToOrderItem);

        int databaseSizeBeforeDelete = billLineItemToOrderItemRepository.findAll().size();

        // Delete the billLineItemToOrderItem
        restBillLineItemToOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, billLineItemToOrderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BillLineItemToOrderItem> billLineItemToOrderItemList = billLineItemToOrderItemRepository.findAll();
        assertThat(billLineItemToOrderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
