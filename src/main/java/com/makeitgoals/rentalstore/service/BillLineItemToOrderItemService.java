package com.makeitgoals.rentalstore.service;

import com.makeitgoals.rentalstore.service.dto.BillLineItemToOrderItemDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.makeitgoals.rentalstore.domain.BillLineItemToOrderItem}.
 */
public interface BillLineItemToOrderItemService {
    /**
     * Save a billLineItemToOrderItem.
     *
     * @param billLineItemToOrderItemDTO the entity to save.
     * @return the persisted entity.
     */
    BillLineItemToOrderItemDTO save(BillLineItemToOrderItemDTO billLineItemToOrderItemDTO);

    /**
     * Partially updates a billLineItemToOrderItem.
     *
     * @param billLineItemToOrderItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillLineItemToOrderItemDTO> partialUpdate(BillLineItemToOrderItemDTO billLineItemToOrderItemDTO);

    /**
     * Get all the billLineItemToOrderItems.
     *
     * @return the list of entities.
     */
    List<BillLineItemToOrderItemDTO> findAll();

    /**
     * Get the "id" billLineItemToOrderItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillLineItemToOrderItemDTO> findOne(Long id);

    /**
     * Delete the "id" billLineItemToOrderItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
