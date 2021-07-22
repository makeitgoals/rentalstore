package com.makeitgoals.rentalstore.web.rest;

import static com.makeitgoals.rentalstore.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.makeitgoals.rentalstore.IntegrationTest;
import com.makeitgoals.rentalstore.domain.Product;
import com.makeitgoals.rentalstore.domain.ProductCategory;
import com.makeitgoals.rentalstore.repository.ProductRepository;
import com.makeitgoals.rentalstore.service.criteria.ProductCriteria;
import com.makeitgoals.rentalstore.service.dto.ProductDTO;
import com.makeitgoals.rentalstore.service.mapper.ProductMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_SIZE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE_PER_DAY = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE_PER_DAY = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRICE_PER_DAY = new BigDecimal(0 - 1);

    private static final byte[] DEFAULT_PRODUCT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRODUCT_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRODUCT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRODUCT_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .productName(DEFAULT_PRODUCT_NAME)
            .productDescription(DEFAULT_PRODUCT_DESCRIPTION)
            .productSize(DEFAULT_PRODUCT_SIZE)
            .pricePerDay(DEFAULT_PRICE_PER_DAY)
            .productImage(DEFAULT_PRODUCT_IMAGE)
            .productImageContentType(DEFAULT_PRODUCT_IMAGE_CONTENT_TYPE);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .productName(UPDATED_PRODUCT_NAME)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productSize(UPDATED_PRODUCT_SIZE)
            .pricePerDay(UPDATED_PRICE_PER_DAY)
            .productImage(UPDATED_PRODUCT_IMAGE)
            .productImageContentType(UPDATED_PRODUCT_IMAGE_CONTENT_TYPE);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProduct.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductSize()).isEqualTo(DEFAULT_PRODUCT_SIZE);
        assertThat(testProduct.getPricePerDay()).isEqualByComparingTo(DEFAULT_PRICE_PER_DAY);
        assertThat(testProduct.getProductImage()).isEqualTo(DEFAULT_PRODUCT_IMAGE);
        assertThat(testProduct.getProductImageContentType()).isEqualTo(DEFAULT_PRODUCT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductSize(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setPricePerDay(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productSize").value(hasItem(DEFAULT_PRODUCT_SIZE)))
            .andExpect(jsonPath("$.[*].pricePerDay").value(hasItem(sameNumber(DEFAULT_PRICE_PER_DAY))))
            .andExpect(jsonPath("$.[*].productImageContentType").value(hasItem(DEFAULT_PRODUCT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE))));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.productDescription").value(DEFAULT_PRODUCT_DESCRIPTION))
            .andExpect(jsonPath("$.productSize").value(DEFAULT_PRODUCT_SIZE))
            .andExpect(jsonPath("$.pricePerDay").value(sameNumber(DEFAULT_PRICE_PER_DAY)))
            .andExpect(jsonPath("$.productImageContentType").value(DEFAULT_PRODUCT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.productImage").value(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE)));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName equals to DEFAULT_PRODUCT_NAME
        defaultProductShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName not equals to DEFAULT_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.notEquals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName not equals to UPDATED_PRODUCT_NAME
        defaultProductShouldBeFound("productName.notEquals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultProductShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the productList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName is not null
        defaultProductShouldBeFound("productName.specified=true");

        // Get all the productList where productName is null
        defaultProductShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName contains DEFAULT_PRODUCT_NAME
        defaultProductShouldBeFound("productName.contains=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName contains UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.contains=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName does not contain DEFAULT_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.doesNotContain=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName does not contain UPDATED_PRODUCT_NAME
        defaultProductShouldBeFound("productName.doesNotContain=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription equals to DEFAULT_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.equals=" + DEFAULT_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription equals to UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.equals=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription not equals to DEFAULT_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.notEquals=" + DEFAULT_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription not equals to UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.notEquals=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription in DEFAULT_PRODUCT_DESCRIPTION or UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.in=" + DEFAULT_PRODUCT_DESCRIPTION + "," + UPDATED_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription equals to UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.in=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription is not null
        defaultProductShouldBeFound("productDescription.specified=true");

        // Get all the productList where productDescription is null
        defaultProductShouldNotBeFound("productDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription contains DEFAULT_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.contains=" + DEFAULT_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription contains UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.contains=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription does not contain DEFAULT_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.doesNotContain=" + DEFAULT_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription does not contain UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.doesNotContain=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productSize equals to DEFAULT_PRODUCT_SIZE
        defaultProductShouldBeFound("productSize.equals=" + DEFAULT_PRODUCT_SIZE);

        // Get all the productList where productSize equals to UPDATED_PRODUCT_SIZE
        defaultProductShouldNotBeFound("productSize.equals=" + UPDATED_PRODUCT_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByProductSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productSize not equals to DEFAULT_PRODUCT_SIZE
        defaultProductShouldNotBeFound("productSize.notEquals=" + DEFAULT_PRODUCT_SIZE);

        // Get all the productList where productSize not equals to UPDATED_PRODUCT_SIZE
        defaultProductShouldBeFound("productSize.notEquals=" + UPDATED_PRODUCT_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByProductSizeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productSize in DEFAULT_PRODUCT_SIZE or UPDATED_PRODUCT_SIZE
        defaultProductShouldBeFound("productSize.in=" + DEFAULT_PRODUCT_SIZE + "," + UPDATED_PRODUCT_SIZE);

        // Get all the productList where productSize equals to UPDATED_PRODUCT_SIZE
        defaultProductShouldNotBeFound("productSize.in=" + UPDATED_PRODUCT_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByProductSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productSize is not null
        defaultProductShouldBeFound("productSize.specified=true");

        // Get all the productList where productSize is null
        defaultProductShouldNotBeFound("productSize.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductSizeContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productSize contains DEFAULT_PRODUCT_SIZE
        defaultProductShouldBeFound("productSize.contains=" + DEFAULT_PRODUCT_SIZE);

        // Get all the productList where productSize contains UPDATED_PRODUCT_SIZE
        defaultProductShouldNotBeFound("productSize.contains=" + UPDATED_PRODUCT_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByProductSizeNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productSize does not contain DEFAULT_PRODUCT_SIZE
        defaultProductShouldNotBeFound("productSize.doesNotContain=" + DEFAULT_PRODUCT_SIZE);

        // Get all the productList where productSize does not contain UPDATED_PRODUCT_SIZE
        defaultProductShouldBeFound("productSize.doesNotContain=" + UPDATED_PRODUCT_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByPricePerDayIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where pricePerDay equals to DEFAULT_PRICE_PER_DAY
        defaultProductShouldBeFound("pricePerDay.equals=" + DEFAULT_PRICE_PER_DAY);

        // Get all the productList where pricePerDay equals to UPDATED_PRICE_PER_DAY
        defaultProductShouldNotBeFound("pricePerDay.equals=" + UPDATED_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductsByPricePerDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where pricePerDay not equals to DEFAULT_PRICE_PER_DAY
        defaultProductShouldNotBeFound("pricePerDay.notEquals=" + DEFAULT_PRICE_PER_DAY);

        // Get all the productList where pricePerDay not equals to UPDATED_PRICE_PER_DAY
        defaultProductShouldBeFound("pricePerDay.notEquals=" + UPDATED_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductsByPricePerDayIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where pricePerDay in DEFAULT_PRICE_PER_DAY or UPDATED_PRICE_PER_DAY
        defaultProductShouldBeFound("pricePerDay.in=" + DEFAULT_PRICE_PER_DAY + "," + UPDATED_PRICE_PER_DAY);

        // Get all the productList where pricePerDay equals to UPDATED_PRICE_PER_DAY
        defaultProductShouldNotBeFound("pricePerDay.in=" + UPDATED_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductsByPricePerDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where pricePerDay is not null
        defaultProductShouldBeFound("pricePerDay.specified=true");

        // Get all the productList where pricePerDay is null
        defaultProductShouldNotBeFound("pricePerDay.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPricePerDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where pricePerDay is greater than or equal to DEFAULT_PRICE_PER_DAY
        defaultProductShouldBeFound("pricePerDay.greaterThanOrEqual=" + DEFAULT_PRICE_PER_DAY);

        // Get all the productList where pricePerDay is greater than or equal to UPDATED_PRICE_PER_DAY
        defaultProductShouldNotBeFound("pricePerDay.greaterThanOrEqual=" + UPDATED_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductsByPricePerDayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where pricePerDay is less than or equal to DEFAULT_PRICE_PER_DAY
        defaultProductShouldBeFound("pricePerDay.lessThanOrEqual=" + DEFAULT_PRICE_PER_DAY);

        // Get all the productList where pricePerDay is less than or equal to SMALLER_PRICE_PER_DAY
        defaultProductShouldNotBeFound("pricePerDay.lessThanOrEqual=" + SMALLER_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductsByPricePerDayIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where pricePerDay is less than DEFAULT_PRICE_PER_DAY
        defaultProductShouldNotBeFound("pricePerDay.lessThan=" + DEFAULT_PRICE_PER_DAY);

        // Get all the productList where pricePerDay is less than UPDATED_PRICE_PER_DAY
        defaultProductShouldBeFound("pricePerDay.lessThan=" + UPDATED_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductsByPricePerDayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where pricePerDay is greater than DEFAULT_PRICE_PER_DAY
        defaultProductShouldNotBeFound("pricePerDay.greaterThan=" + DEFAULT_PRICE_PER_DAY);

        // Get all the productList where pricePerDay is greater than SMALLER_PRICE_PER_DAY
        defaultProductShouldBeFound("pricePerDay.greaterThan=" + SMALLER_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductsByProductCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        ProductCategory productCategory = ProductCategoryResourceIT.createEntity(em);
        em.persist(productCategory);
        em.flush();
        product.setProductCategory(productCategory);
        productRepository.saveAndFlush(product);
        Long productCategoryId = productCategory.getId();

        // Get all the productList where productCategory equals to productCategoryId
        defaultProductShouldBeFound("productCategoryId.equals=" + productCategoryId);

        // Get all the productList where productCategory equals to (productCategoryId + 1)
        defaultProductShouldNotBeFound("productCategoryId.equals=" + (productCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productSize").value(hasItem(DEFAULT_PRODUCT_SIZE)))
            .andExpect(jsonPath("$.[*].pricePerDay").value(hasItem(sameNumber(DEFAULT_PRICE_PER_DAY))))
            .andExpect(jsonPath("$.[*].productImageContentType").value(hasItem(DEFAULT_PRODUCT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE))));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .productName(UPDATED_PRODUCT_NAME)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productSize(UPDATED_PRODUCT_SIZE)
            .pricePerDay(UPDATED_PRICE_PER_DAY)
            .productImage(UPDATED_PRODUCT_IMAGE)
            .productImageContentType(UPDATED_PRODUCT_IMAGE_CONTENT_TYPE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProduct.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductSize()).isEqualTo(UPDATED_PRODUCT_SIZE);
        assertThat(testProduct.getPricePerDay()).isEqualTo(UPDATED_PRICE_PER_DAY);
        assertThat(testProduct.getProductImage()).isEqualTo(UPDATED_PRODUCT_IMAGE);
        assertThat(testProduct.getProductImageContentType()).isEqualTo(UPDATED_PRODUCT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.productImage(UPDATED_PRODUCT_IMAGE).productImageContentType(UPDATED_PRODUCT_IMAGE_CONTENT_TYPE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProduct.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductSize()).isEqualTo(DEFAULT_PRODUCT_SIZE);
        assertThat(testProduct.getPricePerDay()).isEqualByComparingTo(DEFAULT_PRICE_PER_DAY);
        assertThat(testProduct.getProductImage()).isEqualTo(UPDATED_PRODUCT_IMAGE);
        assertThat(testProduct.getProductImageContentType()).isEqualTo(UPDATED_PRODUCT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .productName(UPDATED_PRODUCT_NAME)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productSize(UPDATED_PRODUCT_SIZE)
            .pricePerDay(UPDATED_PRICE_PER_DAY)
            .productImage(UPDATED_PRODUCT_IMAGE)
            .productImageContentType(UPDATED_PRODUCT_IMAGE_CONTENT_TYPE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProduct.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductSize()).isEqualTo(UPDATED_PRODUCT_SIZE);
        assertThat(testProduct.getPricePerDay()).isEqualByComparingTo(UPDATED_PRICE_PER_DAY);
        assertThat(testProduct.getProductImage()).isEqualTo(UPDATED_PRODUCT_IMAGE);
        assertThat(testProduct.getProductImageContentType()).isEqualTo(UPDATED_PRODUCT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
