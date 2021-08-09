package com.makeitgoals.rentalstore.web.rest;

import com.makeitgoals.rentalstore.repository.BillLineItemToOrderItemRepository;
import com.makeitgoals.rentalstore.service.BillLineItemToOrderItemQueryService;
import com.makeitgoals.rentalstore.service.BillLineItemToOrderItemService;
import com.makeitgoals.rentalstore.service.criteria.BillLineItemToOrderItemCriteria;
import com.makeitgoals.rentalstore.service.dto.BillLineItemToOrderItemDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.makeitgoals.rentalstore.domain.BillLineItemToOrderItem}.
 */
@RestController
@RequestMapping("/api")
public class BillLineItemToOrderItemResource {

    private final Logger log = LoggerFactory.getLogger(BillLineItemToOrderItemResource.class);

    private static final String ENTITY_NAME = "billLineItemToOrderItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillLineItemToOrderItemService billLineItemToOrderItemService;

    private final BillLineItemToOrderItemRepository billLineItemToOrderItemRepository;

    private final BillLineItemToOrderItemQueryService billLineItemToOrderItemQueryService;

    public BillLineItemToOrderItemResource(
        BillLineItemToOrderItemService billLineItemToOrderItemService,
        BillLineItemToOrderItemRepository billLineItemToOrderItemRepository,
        BillLineItemToOrderItemQueryService billLineItemToOrderItemQueryService
    ) {
        this.billLineItemToOrderItemService = billLineItemToOrderItemService;
        this.billLineItemToOrderItemRepository = billLineItemToOrderItemRepository;
        this.billLineItemToOrderItemQueryService = billLineItemToOrderItemQueryService;
    }

    /**
     * {@code POST  /bill-line-item-to-order-items} : Create a new billLineItemToOrderItem.
     *
     * @param billLineItemToOrderItemDTO the billLineItemToOrderItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billLineItemToOrderItemDTO, or with status {@code 400 (Bad Request)} if the billLineItemToOrderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bill-line-item-to-order-items")
    public ResponseEntity<BillLineItemToOrderItemDTO> createBillLineItemToOrderItem(
        @Valid @RequestBody BillLineItemToOrderItemDTO billLineItemToOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to save BillLineItemToOrderItem : {}", billLineItemToOrderItemDTO);
        if (billLineItemToOrderItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new billLineItemToOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillLineItemToOrderItemDTO result = billLineItemToOrderItemService.save(billLineItemToOrderItemDTO);
        return ResponseEntity
            .created(new URI("/api/bill-line-item-to-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bill-line-item-to-order-items/:id} : Updates an existing billLineItemToOrderItem.
     *
     * @param id the id of the billLineItemToOrderItemDTO to save.
     * @param billLineItemToOrderItemDTO the billLineItemToOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billLineItemToOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the billLineItemToOrderItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billLineItemToOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bill-line-item-to-order-items/{id}")
    public ResponseEntity<BillLineItemToOrderItemDTO> updateBillLineItemToOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillLineItemToOrderItemDTO billLineItemToOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BillLineItemToOrderItem : {}, {}", id, billLineItemToOrderItemDTO);
        if (billLineItemToOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billLineItemToOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billLineItemToOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillLineItemToOrderItemDTO result = billLineItemToOrderItemService.save(billLineItemToOrderItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billLineItemToOrderItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bill-line-item-to-order-items/:id} : Partial updates given fields of an existing billLineItemToOrderItem, field will ignore if it is null
     *
     * @param id the id of the billLineItemToOrderItemDTO to save.
     * @param billLineItemToOrderItemDTO the billLineItemToOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billLineItemToOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the billLineItemToOrderItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billLineItemToOrderItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billLineItemToOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bill-line-item-to-order-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BillLineItemToOrderItemDTO> partialUpdateBillLineItemToOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillLineItemToOrderItemDTO billLineItemToOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillLineItemToOrderItem partially : {}, {}", id, billLineItemToOrderItemDTO);
        if (billLineItemToOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billLineItemToOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billLineItemToOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillLineItemToOrderItemDTO> result = billLineItemToOrderItemService.partialUpdate(billLineItemToOrderItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billLineItemToOrderItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bill-line-item-to-order-items} : get all the billLineItemToOrderItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billLineItemToOrderItems in body.
     */
    @GetMapping("/bill-line-item-to-order-items")
    public ResponseEntity<List<BillLineItemToOrderItemDTO>> getAllBillLineItemToOrderItems(BillLineItemToOrderItemCriteria criteria) {
        log.debug("REST request to get BillLineItemToOrderItems by criteria: {}", criteria);
        List<BillLineItemToOrderItemDTO> entityList = billLineItemToOrderItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /bill-line-item-to-order-items/count} : count all the billLineItemToOrderItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bill-line-item-to-order-items/count")
    public ResponseEntity<Long> countBillLineItemToOrderItems(BillLineItemToOrderItemCriteria criteria) {
        log.debug("REST request to count BillLineItemToOrderItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(billLineItemToOrderItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bill-line-item-to-order-items/:id} : get the "id" billLineItemToOrderItem.
     *
     * @param id the id of the billLineItemToOrderItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billLineItemToOrderItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bill-line-item-to-order-items/{id}")
    public ResponseEntity<BillLineItemToOrderItemDTO> getBillLineItemToOrderItem(@PathVariable Long id) {
        log.debug("REST request to get BillLineItemToOrderItem : {}", id);
        Optional<BillLineItemToOrderItemDTO> billLineItemToOrderItemDTO = billLineItemToOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billLineItemToOrderItemDTO);
    }

    /**
     * {@code DELETE  /bill-line-item-to-order-items/:id} : delete the "id" billLineItemToOrderItem.
     *
     * @param id the id of the billLineItemToOrderItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bill-line-item-to-order-items/{id}")
    public ResponseEntity<Void> deleteBillLineItemToOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete BillLineItemToOrderItem : {}", id);
        billLineItemToOrderItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
