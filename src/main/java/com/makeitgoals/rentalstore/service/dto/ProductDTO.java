package com.makeitgoals.rentalstore.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.makeitgoals.rentalstore.domain.Product} entity.
 */
@ApiModel(description = "not an ignored comment")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String productName;

    private String productDescription;

    @NotNull
    private String productSize;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal pricePerDay;

    @Lob
    private byte[] productImage;

    private String productImageContentType;
    private ProductCategoryDTO productCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getProductImageContentType() {
        return productImageContentType;
    }

    public void setProductImageContentType(String productImageContentType) {
        this.productImageContentType = productImageContentType;
    }

    public ProductCategoryDTO getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryDTO productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", productSize='" + getProductSize() + "'" +
            ", pricePerDay=" + getPricePerDay() +
            ", productImage='" + getProductImage() + "'" +
            ", productCategory=" + getProductCategory() +
            "}";
    }
}
