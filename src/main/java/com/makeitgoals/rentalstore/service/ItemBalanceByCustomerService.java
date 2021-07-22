package com.makeitgoals.rentalstore.service;

import com.makeitgoals.rentalstore.service.dto.ItemBalanceByCustomerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer}.
 */
public interface ItemBalanceByCustomerService {
    /**
     * Save a itemBalanceByCustomer.
     *
     * @param itemBalanceByCustomerDTO the entity to save.
     * @return the persisted entity.
     */
    ItemBalanceByCustomerDTO save(ItemBalanceByCustomerDTO itemBalanceByCustomerDTO);

    /**
     * Partially updates a itemBalanceByCustomer.
     *
     * @param itemBalanceByCustomerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemBalanceByCustomerDTO> partialUpdate(ItemBalanceByCustomerDTO itemBalanceByCustomerDTO);

    /**
     * Get all the itemBalanceByCustomers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemBalanceByCustomerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemBalanceByCustomer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemBalanceByCustomerDTO> findOne(Long id);

    /**
     * Delete the "id" itemBalanceByCustomer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
