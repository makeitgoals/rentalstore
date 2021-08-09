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
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.BillLineItemToOrderItem} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.BillLineItemToOrderItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bill-line-item-to-order-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BillLineItemToOrderItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter details;

    private LongFilter orderItemId;

    private LongFilter rentalOrderId;

    private LongFilter billLineItemId;

    public BillLineItemToOrderItemCriteria() {}

    public BillLineItemToOrderItemCriteria(BillLineItemToOrderItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.details = other.details == null ? null : other.details.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
        this.rentalOrderId = other.rentalOrderId == null ? null : other.rentalOrderId.copy();
        this.billLineItemId = other.billLineItemId == null ? null : other.billLineItemId.copy();
    }

    @Override
    public BillLineItemToOrderItemCriteria copy() {
        return new BillLineItemToOrderItemCriteria(this);
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

    public StringFilter getDetails() {
        return details;
    }

    public StringFilter details() {
        if (details == null) {
            details = new StringFilter();
        }
        return details;
    }

    public void setDetails(StringFilter details) {
        this.details = details;
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

    public LongFilter getBillLineItemId() {
        return billLineItemId;
    }

    public LongFilter billLineItemId() {
        if (billLineItemId == null) {
            billLineItemId = new LongFilter();
        }
        return billLineItemId;
    }

    public void setBillLineItemId(LongFilter billLineItemId) {
        this.billLineItemId = billLineItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BillLineItemToOrderItemCriteria that = (BillLineItemToOrderItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(details, that.details) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(rentalOrderId, that.rentalOrderId) &&
            Objects.equals(billLineItemId, that.billLineItemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, details, orderItemId, rentalOrderId, billLineItemId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillLineItemToOrderItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (details != null ? "details=" + details + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            (rentalOrderId != null ? "rentalOrderId=" + rentalOrderId + ", " : "") +
            (billLineItemId != null ? "billLineItemId=" + billLineItemId + ", " : "") +
            "}";
    }
}
