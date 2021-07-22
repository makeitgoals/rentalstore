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
