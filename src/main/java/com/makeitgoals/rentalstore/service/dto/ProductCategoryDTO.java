package com.makeitgoals.rentalstore.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.makeitgoals.rentalstore.domain.ProductCategory} entity.
 */
public class ProductCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String productCategoryName;

    private String productCategoryDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getProductCategoryDescription() {
        return productCategoryDescription;
    }

    public void setProductCategoryDescription(String productCategoryDescription) {
        this.productCategoryDescription = productCategoryDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCategoryDTO)) {
            return false;
        }

        ProductCategoryDTO productCategoryDTO = (ProductCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategoryDTO{" +
            "id=" + getId() +
            ", productCategoryName='" + getProductCategoryName() + "'" +
            ", productCategoryDescription='" + getProductCategoryDescription() + "'" +
            "}";
    }
}
