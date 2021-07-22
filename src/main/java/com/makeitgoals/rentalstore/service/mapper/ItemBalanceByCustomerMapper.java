package com.makeitgoals.rentalstore.service.mapper;

import com.makeitgoals.rentalstore.domain.*;
import com.makeitgoals.rentalstore.service.dto.ItemBalanceByCustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemBalanceByCustomer} and its DTO {@link ItemBalanceByCustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, OrderItemMapper.class, CustomerMapper.class })
public interface ItemBalanceByCustomerMapper extends EntityMapper<ItemBalanceByCustomerDTO, ItemBalanceByCustomer> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "id")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerName")
    ItemBalanceByCustomerDTO toDto(ItemBalanceByCustomer s);
}
