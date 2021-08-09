package com.makeitgoals.rentalstore.repository;

import com.makeitgoals.rentalstore.domain.BillLineItemToOrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BillLineItemToOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillLineItemToOrderItemRepository
    extends JpaRepository<BillLineItemToOrderItem, Long>, JpaSpecificationExecutor<BillLineItemToOrderItem> {}
