package com.makeitgoals.rentalstore.web.rest;

import com.makeitgoals.rentalstore.repository.BillLineItemRepository;
import com.makeitgoals.rentalstore.service.BillLineItemService;
import com.makeitgoals.rentalstore.service.dto.BillLineItemDTO;
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
 * REST controller for managing {@link com.makeitgoals.rentalstore.domain.BillLineItem}.
 */
@RestController
@RequestMapping("/api")
public class BillLineItemResource {

    private final Logger log = LoggerFactory.getLogger(BillLineItemResource.class);

    private static final String ENTITY_NAME = "billLineItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillLineItemService billLineItemService;

    private final BillLineItemRepository billLineItemRepository;

    public BillLineItemResource(BillLineItemService billLineItemService, BillLineItemRepository billLineItemRepository) {
        this.billLineItemService = billLineItemService;
        this.billLineItemRepository = billLineItemRepository;
    }

    /**
     * {@code POST  /bill-line-items} : Create a new billLineItem.
     *
     * @param billLineItemDTO the billLineItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billLineItemDTO, or with status {@code 400 (Bad Request)} if the billLineItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bill-line-items")
    public ResponseEntity<BillLineItemDTO> createBillLineItem(@Valid @RequestBody BillLineItemDTO billLineItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save BillLineItem : {}", billLineItemDTO);
        if (billLineItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new billLineItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillLineItemDTO result = billLineItemService.save(billLineItemDTO);
        return ResponseEntity
            .created(new URI("/api/bill-line-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bill-line-items/:id} : Updates an existing billLineItem.
     *
     * @param id the id of the billLineItemDTO to save.
     * @param billLineItemDTO the billLineItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billLineItemDTO,
     * or with status {@code 400 (Bad Request)} if the billLineItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billLineItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bill-line-items/{id}")
    public ResponseEntity<BillLineItemDTO> updateBillLineItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillLineItemDTO billLineItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BillLineItem : {}, {}", id, billLineItemDTO);
        if (billLineItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billLineItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billLineItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillLineItemDTO result = billLineItemService.save(billLineItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billLineItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bill-line-items/:id} : Partial updates given fields of an existing billLineItem, field will ignore if it is null
     *
     * @param id the id of the billLineItemDTO to save.
     * @param billLineItemDTO the billLineItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billLineItemDTO,
     * or with status {@code 400 (Bad Request)} if the billLineItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billLineItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billLineItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bill-line-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BillLineItemDTO> partialUpdateBillLineItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillLineItemDTO billLineItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillLineItem partially : {}, {}", id, billLineItemDTO);
        if (billLineItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billLineItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billLineItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillLineItemDTO> result = billLineItemService.partialUpdate(billLineItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billLineItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bill-line-items} : get all the billLineItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billLineItems in body.
     */
    @GetMapping("/bill-line-items")
    public List<BillLineItemDTO> getAllBillLineItems() {
        log.debug("REST request to get all BillLineItems");
        return billLineItemService.findAll();
    }

    /**
     * {@code GET  /bill-line-items/:id} : get the "id" billLineItem.
     *
     * @param id the id of the billLineItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billLineItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bill-line-items/{id}")
    public ResponseEntity<BillLineItemDTO> getBillLineItem(@PathVariable Long id) {
        log.debug("REST request to get BillLineItem : {}", id);
        Optional<BillLineItemDTO> billLineItemDTO = billLineItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billLineItemDTO);
    }

    /**
     * {@code DELETE  /bill-line-items/:id} : delete the "id" billLineItem.
     *
     * @param id the id of the billLineItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bill-line-items/{id}")
    public ResponseEntity<Void> deleteBillLineItem(@PathVariable Long id) {
        log.debug("REST request to delete BillLineItem : {}", id);
        billLineItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
