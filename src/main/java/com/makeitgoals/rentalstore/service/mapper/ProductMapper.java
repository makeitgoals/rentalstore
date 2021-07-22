package com.makeitgoals.rentalstore.service.mapper;

import com.makeitgoals.rentalstore.domain.*;
import com.makeitgoals.rentalstore.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductCategoryMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "productCategory", source = "productCategory", qualifiedByName = "productCategoryName")
    ProductDTO toDto(Product s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productName", source = "productName")
    ProductDTO toDtoProductName(Product product);
}
