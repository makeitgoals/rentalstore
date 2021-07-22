package com.makeitgoals.rentalstore.web.rest;

import com.makeitgoals.rentalstore.repository.RentalOrderRepository;
import com.makeitgoals.rentalstore.service.RentalOrderQueryService;
import com.makeitgoals.rentalstore.service.RentalOrderService;
import com.makeitgoals.rentalstore.service.criteria.RentalOrderCriteria;
import com.makeitgoals.rentalstore.service.dto.RentalOrderDTO;
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
 * REST controller for managing {@link com.makeitgoals.rentalstore.domain.RentalOrder}.
 */
@RestController
@RequestMapping("/api")
public class RentalOrderResource {

    private final Logger log = LoggerFactory.getLogger(RentalOrderResource.class);

    private static final String ENTITY_NAME = "rentalOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentalOrderService rentalOrderService;

    private final RentalOrderRepository rentalOrderRepository;

    private final RentalOrderQueryService rentalOrderQueryService;

    public RentalOrderResource(
        RentalOrderService rentalOrderService,
        RentalOrderRepository rentalOrderRepository,
        RentalOrderQueryService rentalOrderQueryService
    ) {
        this.rentalOrderService = rentalOrderService;
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentalOrderQueryService = rentalOrderQueryService;
    }

    /**
     * {@code POST  /rental-orders} : Create a new rentalOrder.
     *
     * @param rentalOrderDTO the rentalOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentalOrderDTO, or with status {@code 400 (Bad Request)} if the rentalOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rental-orders")
    public ResponseEntity<RentalOrderDTO> createRentalOrder(@Valid @RequestBody RentalOrderDTO rentalOrderDTO) throws URISyntaxException {
        log.debug("REST request to save RentalOrder : {}", rentalOrderDTO);
        if (rentalOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new rentalOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RentalOrderDTO result = rentalOrderService.save(rentalOrderDTO);
        return ResponseEntity
            .created(new URI("/api/rental-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rental-orders/:id} : Updates an existing rentalOrder.
     *
     * @param id the id of the rentalOrderDTO to save.
     * @param rentalOrderDTO the rentalOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalOrderDTO,
     * or with status {@code 400 (Bad Request)} if the rentalOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentalOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rental-orders/{id}")
    public ResponseEntity<RentalOrderDTO> updateRentalOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RentalOrderDTO rentalOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RentalOrder : {}, {}", id, rentalOrderDTO);
        if (rentalOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rentalOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RentalOrderDTO result = rentalOrderService.save(rentalOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rental-orders/:id} : Partial updates given fields of an existing rentalOrder, field will ignore if it is null
     *
     * @param id the id of the rentalOrderDTO to save.
     * @param rentalOrderDTO the rentalOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalOrderDTO,
     * or with status {@code 400 (Bad Request)} if the rentalOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rentalOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rentalOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rental-orders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RentalOrderDTO> partialUpdateRentalOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RentalOrderDTO rentalOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RentalOrder partially : {}, {}", id, rentalOrderDTO);
        if (rentalOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rentalOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RentalOrderDTO> result = rentalOrderService.partialUpdate(rentalOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rental-orders} : get all the rentalOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentalOrders in body.
     */
    @GetMapping("/rental-orders")
    public ResponseEntity<List<RentalOrderDTO>> getAllRentalOrders(RentalOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RentalOrders by criteria: {}", criteria);
        Page<RentalOrderDTO> page = rentalOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rental-orders/count} : count all the rentalOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rental-orders/count")
    public ResponseEntity<Long> countRentalOrders(RentalOrderCriteria criteria) {
        log.debug("REST request to count RentalOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(rentalOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rental-orders/:id} : get the "id" rentalOrder.
     *
     * @param id the id of the rentalOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentalOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rental-orders/{id}")
    public ResponseEntity<RentalOrderDTO> getRentalOrder(@PathVariable Long id) {
        log.debug("REST request to get RentalOrder : {}", id);
        Optional<RentalOrderDTO> rentalOrderDTO = rentalOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rentalOrderDTO);
    }

    /**
     * {@code DELETE  /rental-orders/:id} : delete the "id" rentalOrder.
     *
     * @param id the id of the rentalOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rental-orders/{id}")
    public ResponseEntity<Void> deleteRentalOrder(@PathVariable Long id) {
        log.debug("REST request to delete RentalOrder : {}", id);
        rentalOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
