package com.makeitgoals.rentalstore.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.OrderItem} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.OrderItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quantity;

    private LongFilter rentalOrderId;

    private LongFilter productId;

    private LongFilter itemBalanceByCustomerId;

    public OrderItemCriteria() {}

    public OrderItemCriteria(OrderItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.rentalOrderId = other.rentalOrderId == null ? null : other.rentalOrderId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.itemBalanceByCustomerId = other.itemBalanceByCustomerId == null ? null : other.itemBalanceByCustomerId.copy();
    }

    @Override
    public OrderItemCriteria copy() {
        return new OrderItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getRentalOrderId() {
        return rentalOrderId;
    }

    public LongFilter rentalOrderId() {
        if (rentalOrderId == null) {
            rentalOrderId = new LongFilter();
        }
        return rentalOrderId;
    }

    public void setRentalOrderId(LongFilter rentalOrderId) {
        this.rentalOrderId = rentalOrderId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getItemBalanceByCustomerId() {
        return itemBalanceByCustomerId;
    }

    public LongFilter itemBalanceByCustomerId() {
        if (itemBalanceByCustomerId == null) {
            itemBalanceByCustomerId = new LongFilter();
        }
        return itemBalanceByCustomerId;
    }

    public void setItemBalanceByCustomerId(LongFilter itemBalanceByCustomerId) {
        this.itemBalanceByCustomerId = itemBalanceByCustomerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderItemCriteria that = (OrderItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(rentalOrderId, that.rentalOrderId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(itemBalanceByCustomerId, that.itemBalanceByCustomerId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, rentalOrderId, productId, itemBalanceByCustomerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (rentalOrderId != null ? "rentalOrderId=" + rentalOrderId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (itemBalanceByCustomerId != null ? "itemBalanceByCustomerId=" + itemBalanceByCustomerId + ", " : "") +
            "}";
    }
}
