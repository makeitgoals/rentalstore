package com.makeitgoals.rentalstore.web.rest;

import com.makeitgoals.rentalstore.repository.ItemBalanceByCustomerRepository;
import com.makeitgoals.rentalstore.service.ItemBalanceByCustomerService;
import com.makeitgoals.rentalstore.service.dto.ItemBalanceByCustomerDTO;
import com.makeitgoals.rentalstore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer}.
 */
@RestController
@RequestMapping("/api")
public class ItemBalanceByCustomerResource {

    private final Logger log = LoggerFactory.getLogger(ItemBalanceByCustomerResource.class);

    private static final String ENTITY_NAME = "itemBalanceByCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemBalanceByCustomerService itemBalanceByCustomerService;

    private final ItemBalanceByCustomerRepository itemBalanceByCustomerRepository;

    public ItemBalanceByCustomerResource(
        ItemBalanceByCustomerService itemBalanceByCustomerService,
        ItemBalanceByCustomerRepository itemBalanceByCustomerRepository
    ) {
        this.itemBalanceByCustomerService = itemBalanceByCustomerService;
        this.itemBalanceByCustomerRepository = itemBalanceByCustomerRepository;
    }

    /**
     * {@code POST  /item-balance-by-customers} : Create a new itemBalanceByCustomer.
     *
     * @param itemBalanceByCustomerDTO the itemBalanceByCustomerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemBalanceByCustomerDTO, or with status {@code 400 (Bad Request)} if the itemBalanceByCustomer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-balance-by-customers")
    public ResponseEntity<ItemBalanceByCustomerDTO> createItemBalanceByCustomer(
        @Valid @RequestBody ItemBalanceByCustomerDTO itemBalanceByCustomerDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ItemBalanceByCustomer : {}", itemBalanceByCustomerDTO);
        if (itemBalanceByCustomerDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemBalanceByCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemBalanceByCustomerDTO result = itemBalanceByCustomerService.save(itemBalanceByCustomerDTO);
        return ResponseEntity
            .created(new URI("/api/item-balance-by-customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-balance-by-customers/:id} : Updates an existing itemBalanceByCustomer.
     *
     * @param id the id of the itemBalanceByCustomerDTO to save.
     * @param itemBalanceByCustomerDTO the itemBalanceByCustomerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemBalanceByCustomerDTO,
     * or with status {@code 400 (Bad Request)} if the itemBalanceByCustomerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemBalanceByCustomerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-balance-by-customers/{id}")
    public ResponseEntity<ItemBalanceByCustomerDTO> updateItemBalanceByCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemBalanceByCustomerDTO itemBalanceByCustomerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemBalanceByCustomer : {}, {}", id, itemBalanceByCustomerDTO);
        if (itemBalanceByCustomerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemBalanceByCustomerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemBalanceByCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemBalanceByCustomerDTO result = itemBalanceByCustomerService.save(itemBalanceByCustomerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemBalanceByCustomerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-balance-by-customers/:id} : Partial updates given fields of an existing itemBalanceByCustomer, field will ignore if it is null
     *
     * @param id the id of the itemBalanceByCustomerDTO to save.
     * @param itemBalanceByCustomerDTO the itemBalanceByCustomerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemBalanceByCustomerDTO,
     * or with status {@code 400 (Bad Request)} if the itemBalanceByCustomerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemBalanceByCustomerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemBalanceByCustomerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-balance-by-customers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ItemBalanceByCustomerDTO> partialUpdateItemBalanceByCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemBalanceByCustomerDTO itemBalanceByCustomerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemBalanceByCustomer partially : {}, {}", id, itemBalanceByCustomerDTO);
        if (itemBalanceByCustomerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemBalanceByCustomerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemBalanceByCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemBalanceByCustomerDTO> result = itemBalanceByCustomerService.partialUpdate(itemBalanceByCustomerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemBalanceByCustomerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-balance-by-customers} : get all the itemBalanceByCustomers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemBalanceByCustomers in body.
     */
    @GetMapping("/item-balance-by-customers")
    public ResponseEntity<List<ItemBalanceByCustomerDTO>> getAllItemBalanceByCustomers(Pageable pageable) {
        log.debug("REST request to get a page of ItemBalanceByCustomers");
        Page<ItemBalanceByCustomerDTO> page = itemBalanceByCustomerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-balance-by-customers/:id} : get the "id" itemBalanceByCustomer.
     *
     * @param id the id of the itemBalanceByCustomerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemBalanceByCustomerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-balance-by-customers/{id}")
    public ResponseEntity<ItemBalanceByCustomerDTO> getItemBalanceByCustomer(@PathVariable Long id) {
        log.debug("REST request to get ItemBalanceByCustomer : {}", id);
        Optional<ItemBalanceByCustomerDTO> itemBalanceByCustomerDTO = itemBalanceByCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemBalanceByCustomerDTO);
    }

    /**
     * {@code DELETE  /item-balance-by-customers/:id} : delete the "id" itemBalanceByCustomer.
     *
     * @param id the id of the itemBalanceByCustomerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-balance-by-customers/{id}")
    public ResponseEntity<Void> deleteItemBalanceByCustomer(@PathVariable Long id) {
        log.debug("REST request to delete ItemBalanceByCustomer : {}", id);
        itemBalanceByCustomerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
