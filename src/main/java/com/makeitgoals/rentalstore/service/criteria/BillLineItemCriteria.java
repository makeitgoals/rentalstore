package com.makeitgoals.rentalstore.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.BillLineItem} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.BillLineItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bill-line-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BillLineItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter details;

    private InstantFilter fromDate;

    private InstantFilter toDate;

    private IntegerFilter outstandingQuantity;

    private BigDecimalFilter lineAmount;

    private LongFilter productId;

    private LongFilter billId;

    private LongFilter rentalOrderId;

    public BillLineItemCriteria() {}

    public BillLineItemCriteria(BillLineItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.details = other.details == null ? null : other.details.copy();
        this.fromDate = other.fromDate == null ? null : other.fromDate.copy();
        this.toDate = other.toDate == null ? null : other.toDate.copy();
        this.outstandingQuantity = other.outstandingQuantity == null ? null : other.outstandingQuantity.copy();
        this.lineAmount = other.lineAmount == null ? null : other.lineAmount.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.billId = other.billId == null ? null : other.billId.copy();
        this.rentalOrderId = other.rentalOrderId == null ? null : other.rentalOrderId.copy();
    }

    @Override
    public BillLineItemCriteria copy() {
        return new BillLineItemCriteria(this);
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

    public InstantFilter getFromDate() {
        return fromDate;
    }

    public InstantFilter fromDate() {
        if (fromDate == null) {
            fromDate = new InstantFilter();
        }
        return fromDate;
    }

    public void setFromDate(InstantFilter fromDate) {
        this.fromDate = fromDate;
    }

    public InstantFilter getToDate() {
        return toDate;
    }

    public InstantFilter toDate() {
        if (toDate == null) {
            toDate = new InstantFilter();
        }
        return toDate;
    }

    public void setToDate(InstantFilter toDate) {
        this.toDate = toDate;
    }

    public IntegerFilter getOutstandingQuantity() {
        return outstandingQuantity;
    }

    public IntegerFilter outstandingQuantity() {
        if (outstandingQuantity == null) {
            outstandingQuantity = new IntegerFilter();
        }
        return outstandingQuantity;
    }

    public void setOutstandingQuantity(IntegerFilter outstandingQuantity) {
        this.outstandingQuantity = outstandingQuantity;
    }

    public BigDecimalFilter getLineAmount() {
        return lineAmount;
    }

    public BigDecimalFilter lineAmount() {
        if (lineAmount == null) {
            lineAmount = new BigDecimalFilter();
        }
        return lineAmount;
    }

    public void setLineAmount(BigDecimalFilter lineAmount) {
        this.lineAmount = lineAmount;
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

    public LongFilter getBillId() {
        return billId;
    }

    public LongFilter billId() {
        if (billId == null) {
            billId = new LongFilter();
        }
        return billId;
    }

    public void setBillId(LongFilter billId) {
        this.billId = billId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BillLineItemCriteria that = (BillLineItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(details, that.details) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(toDate, that.toDate) &&
            Objects.equals(outstandingQuantity, that.outstandingQuantity) &&
            Objects.equals(lineAmount, that.lineAmount) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(billId, that.billId) &&
            Objects.equals(rentalOrderId, that.rentalOrderId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, details, fromDate, toDate, outstandingQuantity, lineAmount, productId, billId, rentalOrderId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillLineItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (details != null ? "details=" + details + ", " : "") +
            (fromDate != null ? "fromDate=" + fromDate + ", " : "") +
            (toDate != null ? "toDate=" + toDate + ", " : "") +
            (outstandingQuantity != null ? "outstandingQuantity=" + outstandingQuantity + ", " : "") +
            (lineAmount != null ? "lineAmount=" + lineAmount + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (billId != null ? "billId=" + billId + ", " : "") +
            (rentalOrderId != null ? "rentalOrderId=" + rentalOrderId + ", " : "") +
            "}";
    }
}
