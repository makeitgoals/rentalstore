package com.makeitgoals.rentalstore.service.mapper;

import com.makeitgoals.rentalstore.domain.*;
import com.makeitgoals.rentalstore.service.dto.BillLineItemToOrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillLineItemToOrderItem} and its DTO {@link BillLineItemToOrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderItemMapper.class, RentalOrderMapper.class, BillLineItemMapper.class })
public interface BillLineItemToOrderItemMapper extends EntityMapper<BillLineItemToOrderItemDTO, BillLineItemToOrderItem> {
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "id")
    @Mapping(target = "rentalOrder", source = "rentalOrder", qualifiedByName = "id")
    @Mapping(target = "billLineItem", source = "billLineItem", qualifiedByName = "id")
    BillLineItemToOrderItemDTO toDto(BillLineItemToOrderItem s);
}
