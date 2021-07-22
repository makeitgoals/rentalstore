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
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.Customer} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter customerName;

    private StringFilter contactName;

    private StringFilter fatherName;

    private StringFilter ownerName;

    private StringFilter siteAddress;

    private StringFilter phoneNumber;

    private StringFilter email;

    private LongFilter itemBalanceByCustomerId;

    private LongFilter rentalOrderId;

    private LongFilter billId;

    private LongFilter paymentId;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.customerName = other.customerName == null ? null : other.customerName.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.fatherName = other.fatherName == null ? null : other.fatherName.copy();
        this.ownerName = other.ownerName == null ? null : other.ownerName.copy();
        this.siteAddress = other.siteAddress == null ? null : other.siteAddress.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.itemBalanceByCustomerId = other.itemBalanceByCustomerId == null ? null : other.itemBalanceByCustomerId.copy();
        this.rentalOrderId = other.rentalOrderId == null ? null : other.rentalOrderId.copy();
        this.billId = other.billId == null ? null : other.billId.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public StringFilter getCustomerName() {
        return customerName;
    }

    public StringFilter customerName() {
        if (customerName == null) {
            customerName = new StringFilter();
        }
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public StringFilter getContactName() {
        return contactName;
    }

    public StringFilter contactName() {
        if (contactName == null) {
            contactName = new StringFilter();
        }
        return contactName;
    }

    public void setContactName(StringFilter contactName) {
        this.contactName = contactName;
    }

    public StringFilter getFatherName() {
        return fatherName;
    }

    public StringFilter fatherName() {
        if (fatherName == null) {
            fatherName = new StringFilter();
        }
        return fatherName;
    }

    public void setFatherName(StringFilter fatherName) {
        this.fatherName = fatherName;
    }

    public StringFilter getOwnerName() {
        return ownerName;
    }

    public StringFilter ownerName() {
        if (ownerName == null) {
            ownerName = new StringFilter();
        }
        return ownerName;
    }

    public void setOwnerName(StringFilter ownerName) {
        this.ownerName = ownerName;
    }

    public StringFilter getSiteAddress() {
        return siteAddress;
    }

    public StringFilter siteAddress() {
        if (siteAddress == null) {
            siteAddress = new StringFilter();
        }
        return siteAddress;
    }

    public void setSiteAddress(StringFilter siteAddress) {
        this.siteAddress = siteAddress;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(fatherName, that.fatherName) &&
            Objects.equals(ownerName, that.ownerName) &&
            Objects.equals(siteAddress, that.siteAddress) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(itemBalanceByCustomerId, that.itemBalanceByCustomerId) &&
            Objects.equals(rentalOrderId, that.rentalOrderId) &&
            Objects.equals(billId, that.billId) &&
            Objects.equals(paymentId, that.paymentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            customerName,
            contactName,
            fatherName,
            ownerName,
            siteAddress,
            phoneNumber,
            email,
            itemBalanceByCustomerId,
            rentalOrderId,
            billId,
            paymentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (customerName != null ? "customerName=" + customerName + ", " : "") +
            (contactName != null ? "contactName=" + contactName + ", " : "") +
            (fatherName != null ? "fatherName=" + fatherName + ", " : "") +
            (ownerName != null ? "ownerName=" + ownerName + ", " : "") +
            (siteAddress != null ? "siteAddress=" + siteAddress + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (itemBalanceByCustomerId != null ? "itemBalanceByCustomerId=" + itemBalanceByCustomerId + ", " : "") +
            (rentalOrderId != null ? "rentalOrderId=" + rentalOrderId + ", " : "") +
            (billId != null ? "billId=" + billId + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            "}";
    }
}
