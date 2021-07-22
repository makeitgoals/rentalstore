package com.makeitgoals.rentalstore.repository;

import com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemBalanceByCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemBalanceByCustomerRepository extends JpaRepository<ItemBalanceByCustomer, Long> {}
