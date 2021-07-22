package com.makeitgoals.rentalstore.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.makeitgoals.rentalstore.domain.OrderItem} entity.
 */
public class OrderItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer quantity;

    private RentalOrderDTO rentalOrder;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public RentalOrderDTO getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrderDTO rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }

        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", rentalOrder=" + getRentalOrder() +
            ", product=" + getProduct() +
            "}";
    }
}
