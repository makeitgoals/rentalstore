package com.makeitgoals.rentalstore.service;

import com.makeitgoals.rentalstore.service.dto.RentalOrderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.makeitgoals.rentalstore.domain.RentalOrder}.
 */
public interface RentalOrderService {
    /**
     * Save a rentalOrder.
     *
     * @param rentalOrderDTO the entity to save.
     * @return the persisted entity.
     */
    RentalOrderDTO save(RentalOrderDTO rentalOrderDTO);

    /**
     * Partially updates a rentalOrder.
     *
     * @param rentalOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RentalOrderDTO> partialUpdate(RentalOrderDTO rentalOrderDTO);

    /**
     * Get all the rentalOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentalOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rentalOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentalOrderDTO> findOne(Long id);

    /**
     * Delete the "id" rentalOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
