package com.makeitgoals.rentalstore.service.impl;

import com.makeitgoals.rentalstore.repository.OrderItemExtendedRepository;
import com.makeitgoals.rentalstore.repository.RentalOrderRepository;
import com.makeitgoals.rentalstore.service.dto.OrderItemDTO;
import com.makeitgoals.rentalstore.service.dto.RentalOrderDTO;
import com.makeitgoals.rentalstore.service.mapper.OrderItemMapper;
import com.makeitgoals.rentalstore.service.mapper.RentalOrderMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
public class OrderItemExtendedServiceImpl extends OrderItemServiceImpl {

    private final Logger log = LoggerFactory.getLogger(OrderItemExtendedServiceImpl.class);

    private final OrderItemExtendedRepository orderItemExtendedRepository;
    private final RentalOrderRepository rentalOrderRepository;
    private final OrderItemMapper orderItemMapper;
    private final RentalOrderMapper rentalOrderMapper;

    public OrderItemExtendedServiceImpl(
        OrderItemExtendedRepository orderItemExtendedRepository,
        OrderItemMapper orderItemMapper,
        RentalOrderRepository rentalOrderRepository,
        RentalOrderMapper rentalOrderMapper
    ) {
        super(orderItemExtendedRepository, orderItemMapper);
        this.orderItemExtendedRepository = orderItemExtendedRepository;
        this.rentalOrderRepository = rentalOrderRepository;
        this.orderItemMapper = orderItemMapper;
        this.rentalOrderMapper = rentalOrderMapper;
    }

    //    public List<OrderItem> findOrderItemsByRentalOrder(RentalOrder rentalOrder) {
    //        return orderItemExtendedRepository.findOrderItemsByRentalOrder(rentalOrder);
    //    }

    //    @Override
    //    @Transactional(readOnly = true)
    //    public Optional<OrderItemDTO> findOne(Long id) {
    //        log.debug("Request to get OrderItem : {}", id);
    //        return orderItemRepository.
    //        return orderItemRepository.findById(id).map(orderItemMapper::toDto);
    //    }

    @Transactional(readOnly = true)
    public List<OrderItemDTO> findOrderItemsByRentalOrder(RentalOrderDTO rentalOrderDTO) {
        log.debug("Request to get all OrderItems by RentalOrder : {}", rentalOrderDTO.toString());
        //        return orderItemExtendedRepository.findOrderItemsByRentalOrder(rentalOrder).stream()
        //            .map(user -> modelMapper.map(user, UserDTO.class))
        //            .collect(Collectors.toList());
        return orderItemExtendedRepository
            .findOrderItemsByRentalOrder(rentalOrderMapper.toEntity(rentalOrderDTO))
            .stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
