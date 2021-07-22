package com.makeitgoals.rentalstore.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer} entity.
 */
public class ItemBalanceByCustomerDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer outstandingBalance;

    private ProductDTO product;

    private OrderItemDTO orderItem;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(Integer outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public OrderItemDTO getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemDTO orderItem) {
        this.orderItem = orderItem;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemBalanceByCustomerDTO)) {
            return false;
        }

        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO = (ItemBalanceByCustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemBalanceByCustomerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemBalanceByCustomerDTO{" +
            "id=" + getId() +
            ", outstandingBalance=" + getOutstandingBalance() +
            ", product=" + getProduct() +
            ", orderItem=" + getOrderItem() +
            ", customer=" + getCustomer() +
            "}";
    }
}
