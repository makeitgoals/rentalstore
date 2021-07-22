package com.makeitgoals.rentalstore.service.criteria;

import com.makeitgoals.rentalstore.domain.enumeration.PaymentMethod;
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
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.Payment} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaymentMethod
     */
    public static class PaymentMethodFilter extends Filter<PaymentMethod> {

        public PaymentMethodFilter() {}

        public PaymentMethodFilter(PaymentMethodFilter filter) {
            super(filter);
        }

        @Override
        public PaymentMethodFilter copy() {
            return new PaymentMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter paymentAmount;

    private InstantFilter paymentDate;

    private StringFilter paymentDetails;

    private PaymentMethodFilter paymentMethod;

    private LongFilter customerId;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.paymentDetails = other.paymentDetails == null ? null : other.paymentDetails.copy();
        this.paymentMethod = other.paymentMethod == null ? null : other.paymentMethod.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public BigDecimalFilter getPaymentAmount() {
        return paymentAmount;
    }

    public BigDecimalFilter paymentAmount() {
        if (paymentAmount == null) {
            paymentAmount = new BigDecimalFilter();
        }
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimalFilter paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public InstantFilter getPaymentDate() {
        return paymentDate;
    }

    public InstantFilter paymentDate() {
        if (paymentDate == null) {
            paymentDate = new InstantFilter();
        }
        return paymentDate;
    }

    public void setPaymentDate(InstantFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public StringFilter getPaymentDetails() {
        return paymentDetails;
    }

    public StringFilter paymentDetails() {
        if (paymentDetails == null) {
            paymentDetails = new StringFilter();
        }
        return paymentDetails;
    }

    public void setPaymentDetails(StringFilter paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public PaymentMethodFilter getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentMethodFilter paymentMethod() {
        if (paymentMethod == null) {
            paymentMethod = new PaymentMethodFilter();
        }
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(paymentDetails, that.paymentDetails) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(customerId, that.customerId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentAmount, paymentDate, paymentDetails, paymentMethod, customerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentAmount != null ? "paymentAmount=" + paymentAmount + ", " : "") +
            (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
            (paymentDetails != null ? "paymentDetails=" + paymentDetails + ", " : "") +
            (paymentMethod != null ? "paymentMethod=" + paymentMethod + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }
}
