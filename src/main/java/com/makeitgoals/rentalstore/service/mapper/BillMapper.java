package com.makeitgoals.rentalstore.service.mapper;

import com.makeitgoals.rentalstore.domain.*;
import com.makeitgoals.rentalstore.service.dto.BillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bill} and its DTO {@link BillDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public interface BillMapper extends EntityMapper<BillDTO, Bill> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerName")
    BillDTO toDto(Bill s);

    @Named("code")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    BillDTO toDtoCode(Bill bill);
}
