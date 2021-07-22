package com.makeitgoals.rentalstore.service.mapper;

import com.makeitgoals.rentalstore.domain.*;
import com.makeitgoals.rentalstore.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerName")
    PaymentDTO toDto(Payment s);
}
