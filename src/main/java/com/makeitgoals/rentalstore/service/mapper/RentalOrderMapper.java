package com.makeitgoals.rentalstore.service.mapper;

import com.makeitgoals.rentalstore.domain.*;
import com.makeitgoals.rentalstore.service.dto.RentalOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RentalOrder} and its DTO {@link RentalOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public interface RentalOrderMapper extends EntityMapper<RentalOrderDTO, RentalOrder> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerName")
    RentalOrderDTO toDto(RentalOrder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RentalOrderDTO toDtoId(RentalOrder rentalOrder);

    @Named("code")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    RentalOrderDTO toDtoCode(RentalOrder rentalOrder);
}
