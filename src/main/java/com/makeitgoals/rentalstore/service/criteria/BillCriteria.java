package com.makeitgoals.rentalstore.service.criteria;

import com.makeitgoals.rentalstore.domain.enumeration.BillStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.Bill} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.BillResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bills?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BillCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BillStatus
     */
    public static class BillStatusFilter extends Filter<BillStatus> {

        public BillStatusFilter() {}

        public BillStatusFilter(BillStatusFilter filter) {
            super(filter);
        }

        @Override
        public BillStatusFilter copy() {
            return new BillStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private IntegerFilter costBillNumber;

    private BillStatusFilter billStatus;

    private BigDecimalFilter billTotal;

    private LongFilter taxPercent;

    private BigDecimalFilter billTotalWithTax;

    private LongFilter customerId;

    public BillCriteria() {}

    public BillCriteria(BillCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.costBillNumber = other.costBillNumber == null ? null : other.costBillNumber.copy();
        this.billStatus = other.billStatus == null ? null : other.billStatus.copy();
        this.billTotal = other.billTotal == null ? null : other.billTotal.copy();
        this.taxPercent = other.taxPercent == null ? null : other.taxPercent.copy();
        this.billTotalWithTax = other.billTotalWithTax == null ? null : other.billTotalWithTax.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
    }

    @Override
    public BillCriteria copy() {
        return new BillCriteria(this);
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

    public IntegerFilter getCostBillNumber() {
        return costBillNumber;
    }

    public IntegerFilter costBillNumber() {
        if (costBillNumber == null) {
            costBillNumber = new IntegerFilter();
        }
        return costBillNumber;
    }

    public void setCostBillNumber(IntegerFilter costBillNumber) {
        this.costBillNumber = costBillNumber;
    }

    public BillStatusFilter getBillStatus() {
        return billStatus;
    }

    public BillStatusFilter billStatus() {
        if (billStatus == null) {
            billStatus = new BillStatusFilter();
        }
        return billStatus;
    }

    public void setBillStatus(BillStatusFilter billStatus) {
        this.billStatus = billStatus;
    }

    public BigDecimalFilter getBillTotal() {
        return billTotal;
    }

    public BigDecimalFilter billTotal() {
        if (billTotal == null) {
            billTotal = new BigDecimalFilter();
        }
        return billTotal;
    }

    public void setBillTotal(BigDecimalFilter billTotal) {
        this.billTotal = billTotal;
    }

    public LongFilter getTaxPercent() {
        return taxPercent;
    }

    public LongFilter taxPercent() {
        if (taxPercent == null) {
            taxPercent = new LongFilter();
        }
        return taxPercent;
    }

    public void setTaxPercent(LongFilter taxPercent) {
        this.taxPercent = taxPercent;
    }

    public BigDecimalFilter getBillTotalWithTax() {
        return billTotalWithTax;
    }

    public BigDecimalFilter billTotalWithTax() {
        if (billTotalWithTax == null) {
            billTotalWithTax = new BigDecimalFilter();
        }
        return billTotalWithTax;
    }

    public void setBillTotalWithTax(BigDecimalFilter billTotalWithTax) {
        this.billTotalWithTax = billTotalWithTax;
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
        final BillCriteria that = (BillCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(costBillNumber, that.costBillNumber) &&
            Objects.equals(billStatus, that.billStatus) &&
            Objects.equals(billTotal, that.billTotal) &&
            Objects.equals(taxPercent, that.taxPercent) &&
            Objects.equals(billTotalWithTax, that.billTotalWithTax) &&
            Objects.equals(customerId, that.customerId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, costBillNumber, billStatus, billTotal, taxPercent, billTotalWithTax, customerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (costBillNumber != null ? "costBillNumber=" + costBillNumber + ", " : "") +
            (billStatus != null ? "billStatus=" + billStatus + ", " : "") +
            (billTotal != null ? "billTotal=" + billTotal + ", " : "") +
            (taxPercent != null ? "taxPercent=" + taxPercent + ", " : "") +
            (billTotalWithTax != null ? "billTotalWithTax=" + billTotalWithTax + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }
}
