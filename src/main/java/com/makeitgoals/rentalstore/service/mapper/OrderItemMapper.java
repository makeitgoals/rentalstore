package com.makeitgoals.rentalstore.service.mapper;

import com.makeitgoals.rentalstore.domain.*;
import com.makeitgoals.rentalstore.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { RentalOrderMapper.class, ProductMapper.class })
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "rentalOrder", source = "rentalOrder", qualifiedByName = "code")
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    OrderItemDTO toDto(OrderItem s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderItemDTO toDtoId(OrderItem orderItem);
}
