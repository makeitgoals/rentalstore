package com.makeitgoals.rentalstore.service.mapper;

import com.makeitgoals.rentalstore.domain.*;
import com.makeitgoals.rentalstore.service.dto.BillLineItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillLineItem} and its DTO {@link BillLineItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, BillMapper.class, RentalOrderMapper.class })
public interface BillLineItemMapper extends EntityMapper<BillLineItemDTO, BillLineItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "bill", source = "bill", qualifiedByName = "code")
    @Mapping(target = "rentalOrder", source = "rentalOrder", qualifiedByName = "code")
    BillLineItemDTO toDto(BillLineItem s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillLineItemDTO toDtoId(BillLineItem billLineItem);
}
