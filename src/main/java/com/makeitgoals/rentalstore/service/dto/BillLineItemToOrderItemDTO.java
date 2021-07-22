package com.makeitgoals.rentalstore.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.makeitgoals.rentalstore.domain.BillLineItemToOrderItem} entity.
 */
public class BillLineItemToOrderItemDTO implements Serializable {

    private Long id;

    private String details;

    private OrderItemDTO orderItem;

    private RentalOrderDTO rentalOrder;

    private BillLineItemDTO billLineItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public OrderItemDTO getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemDTO orderItem) {
        this.orderItem = orderItem;
    }

    public RentalOrderDTO getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrderDTO rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public BillLineItemDTO getBillLineItem() {
        return billLineItem;
    }

    public void setBillLineItem(BillLineItemDTO billLineItem) {
        this.billLineItem = billLineItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillLineItemToOrderItemDTO)) {
            return false;
        }

        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO = (BillLineItemToOrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billLineItemToOrderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillLineItemToOrderItemDTO{" +
            "id=" + getId() +
            ", details='" + getDetails() + "'" +
            ", orderItem=" + getOrderItem() +
            ", rentalOrder=" + getRentalOrder() +
            ", billLineItem=" + getBillLineItem() +
            "}";
    }
}
