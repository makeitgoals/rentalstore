package com.makeitgoals.rentalstore.repository;

import com.makeitgoals.rentalstore.domain.BillLineItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BillLineItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillLineItemRepository extends JpaRepository<BillLineItem, Long>, JpaSpecificationExecutor<BillLineItem> {}
