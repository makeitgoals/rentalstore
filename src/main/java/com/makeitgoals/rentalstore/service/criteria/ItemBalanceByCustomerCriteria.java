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
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.ItemBalanceByCustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-balance-by-customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemBalanceByCustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter outstandingBalance;

    private LongFilter productId;

    private LongFilter orderItemId;

    private LongFilter customerId;

    public ItemBalanceByCustomerCriteria() {}

    public ItemBalanceByCustomerCriteria(ItemBalanceByCustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.outstandingBalance = other.outstandingBalance == null ? null : other.outstandingBalance.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
    }

    @Override
    public ItemBalanceByCustomerCriteria copy() {
        return new ItemBalanceByCustomerCriteria(this);
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

    public IntegerFilter getOutstandingBalance() {
        return outstandingBalance;
    }

    public IntegerFilter outstandingBalance() {
        if (outstandingBalance == null) {
            outstandingBalance = new IntegerFilter();
        }
        return outstandingBalance;
    }

    public void setOutstandingBalance(IntegerFilter outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
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

    public LongFilter getOrderItemId() {
        return orderItemId;
    }

    public LongFilter orderItemId() {
        if (orderItemId == null) {
            orderItemId = new LongFilter();
        }
        return orderItemId;
    }

    public void setOrderItemId(LongFilter orderItemId) {
        this.orderItemId = orderItemId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemBalanceByCustomerCriteria that = (ItemBalanceByCustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(outstandingBalance, that.outstandingBalance) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(customerId, that.customerId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, outstandingBalance, productId, orderItemId, customerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemBalanceByCustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (outstandingBalance != null ? "outstandingBalance=" + outstandingBalance + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }
}
