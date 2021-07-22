package com.makeitgoals.rentalstore.repository;

import com.makeitgoals.rentalstore.domain.RentalOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RentalOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long>, JpaSpecificationExecutor<RentalOrder> {}
