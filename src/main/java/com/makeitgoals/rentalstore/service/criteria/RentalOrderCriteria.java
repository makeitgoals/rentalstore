package com.makeitgoals.rentalstore.service.criteria;

import com.makeitgoals.rentalstore.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.RentalOrder} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.RentalOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rental-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RentalOrderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {}

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter rentalIssueDate;

    private InstantFilter rentalReturnDate;

    private OrderStatusFilter rentalOrderStatus;

    private StringFilter code;

    private LongFilter customerId;

    private LongFilter billLineItemId;

    private LongFilter orderItemId;

    public RentalOrderCriteria() {}

    public RentalOrderCriteria(RentalOrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rentalIssueDate = other.rentalIssueDate == null ? null : other.rentalIssueDate.copy();
        this.rentalReturnDate = other.rentalReturnDate == null ? null : other.rentalReturnDate.copy();
        this.rentalOrderStatus = other.rentalOrderStatus == null ? null : other.rentalOrderStatus.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.billLineItemId = other.billLineItemId == null ? null : other.billLineItemId.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
    }

    @Override
    public RentalOrderCriteria copy() {
        return new RentalOrderCriteria(this);
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

    public InstantFilter getRentalIssueDate() {
        return rentalIssueDate;
    }

    public InstantFilter rentalIssueDate() {
        if (rentalIssueDate == null) {
            rentalIssueDate = new InstantFilter();
        }
        return rentalIssueDate;
    }

    public void setRentalIssueDate(InstantFilter rentalIssueDate) {
        this.rentalIssueDate = rentalIssueDate;
    }

    public InstantFilter getRentalReturnDate() {
        return rentalReturnDate;
    }

    public InstantFilter rentalReturnDate() {
        if (rentalReturnDate == null) {
            rentalReturnDate = new InstantFilter();
        }
        return rentalReturnDate;
    }

    public void setRentalReturnDate(InstantFilter rentalReturnDate) {
        this.rentalReturnDate = rentalReturnDate;
    }

    public OrderStatusFilter getRentalOrderStatus() {
        return rentalOrderStatus;
    }

    public OrderStatusFilter rentalOrderStatus() {
        if (rentalOrderStatus == null) {
            rentalOrderStatus = new OrderStatusFilter();
        }
        return rentalOrderStatus;
    }

    public void setRentalOrderStatus(OrderStatusFilter rentalOrderStatus) {
        this.rentalOrderStatus = rentalOrderStatus;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RentalOrderCriteria that = (RentalOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rentalIssueDate, that.rentalIssueDate) &&
            Objects.equals(rentalReturnDate, that.rentalReturnDate) &&
            Objects.equals(rentalOrderStatus, that.rentalOrderStatus) &&
            Objects.equals(code, that.code) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(billLineItemId, that.billLineItemId) &&
            Objects.equals(orderItemId, that.orderItemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rentalIssueDate, rentalReturnDate, rentalOrderStatus, code, customerId, billLineItemId, orderItemId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalOrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rentalIssueDate != null ? "rentalIssueDate=" + rentalIssueDate + ", " : "") +
            (rentalReturnDate != null ? "rentalReturnDate=" + rentalReturnDate + ", " : "") +
            (rentalOrderStatus != null ? "rentalOrderStatus=" + rentalOrderStatus + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (billLineItemId != null ? "billLineItemId=" + billLineItemId + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            "}";
    }
}
