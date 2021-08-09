package com.makeitgoals.rentalstore.web.rest;

import com.makeitgoals.rentalstore.repository.RentalOrderRepository;
import com.makeitgoals.rentalstore.service.RentalOrderQueryService;
import com.makeitgoals.rentalstore.service.RentalOrderService;
import com.makeitgoals.rentalstore.service.dto.OrderItemDTO;
import com.makeitgoals.rentalstore.service.impl.OrderItemExtendedServiceImpl;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RentalOrderExtendedResource extends RentalOrderResource {

    private final Logger log = LoggerFactory.getLogger(RentalOrderExtendedResource.class);

    private final RentalOrderService rentalOrderService;
    private final RentalOrderRepository rentalOrderRepository;
    private final RentalOrderQueryService rentalOrderQueryService;
    private final OrderItemExtendedServiceImpl orderItemExtendedServiceImpl;

    public RentalOrderExtendedResource(
        RentalOrderService rentalOrderService,
        RentalOrderRepository rentalOrderRepository,
        RentalOrderQueryService rentalOrderQueryService,
        OrderItemExtendedServiceImpl orderItemExtendedServiceImpl
    ) {
        super(rentalOrderService, rentalOrderRepository, rentalOrderQueryService);
        this.rentalOrderService = rentalOrderService;
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentalOrderQueryService = rentalOrderQueryService;
        this.orderItemExtendedServiceImpl = orderItemExtendedServiceImpl;
    }

    @GetMapping("/rental-orders/{id}/order-items")
    public List<OrderItemDTO> listOrderItemsWithRentalOrder(@PathVariable Long id) {
        return rentalOrderService
            .findOne(id)
            .map(orderItemExtendedServiceImpl::findOrderItemsByRentalOrder)
            .orElseThrow(NoSuchElementException::new);
    }
}
