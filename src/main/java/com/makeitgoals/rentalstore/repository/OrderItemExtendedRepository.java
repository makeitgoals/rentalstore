package com.makeitgoals.rentalstore.repository;

import com.makeitgoals.rentalstore.domain.OrderItem;
import com.makeitgoals.rentalstore.domain.RentalOrder;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemExtendedRepository extends OrderItemRepository {
    @Query(
        "select orderItem " +
        "from OrderItem orderItem " +
        "inner join orderItem.rentalOrder " +
        "where orderItem.rentalOrder = :rentalOrder"
    )
    List<OrderItem> findOrderItemsByRentalOrder(@Param("rentalOrder") RentalOrder rentalOrder);
}
