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
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.Company} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private StringFilter dealsIn;

    private StringFilter officeAddress;

    private StringFilter companyPhoneNumber;

    private StringFilter email;

    public CompanyCriteria() {}

    public CompanyCriteria(CompanyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.dealsIn = other.dealsIn == null ? null : other.dealsIn.copy();
        this.officeAddress = other.officeAddress == null ? null : other.officeAddress.copy();
        this.companyPhoneNumber = other.companyPhoneNumber == null ? null : other.companyPhoneNumber.copy();
        this.email = other.email == null ? null : other.email.copy();
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
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

    public StringFilter getCompanyName() {
        return companyName;
    }

    public StringFilter companyName() {
        if (companyName == null) {
            companyName = new StringFilter();
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getDealsIn() {
        return dealsIn;
    }

    public StringFilter dealsIn() {
        if (dealsIn == null) {
            dealsIn = new StringFilter();
        }
        return dealsIn;
    }

    public void setDealsIn(StringFilter dealsIn) {
        this.dealsIn = dealsIn;
    }

    public StringFilter getOfficeAddress() {
        return officeAddress;
    }

    public StringFilter officeAddress() {
        if (officeAddress == null) {
            officeAddress = new StringFilter();
        }
        return officeAddress;
    }

    public void setOfficeAddress(StringFilter officeAddress) {
        this.officeAddress = officeAddress;
    }

    public StringFilter getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    public StringFilter companyPhoneNumber() {
        if (companyPhoneNumber == null) {
            companyPhoneNumber = new StringFilter();
        }
        return companyPhoneNumber;
    }

    public void setCompanyPhoneNumber(StringFilter companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompanyCriteria that = (CompanyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(dealsIn, that.dealsIn) &&
            Objects.equals(officeAddress, that.officeAddress) &&
            Objects.equals(companyPhoneNumber, that.companyPhoneNumber) &&
            Objects.equals(email, that.email)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, dealsIn, officeAddress, companyPhoneNumber, email);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (companyName != null ? "companyName=" + companyName + ", " : "") +
            (dealsIn != null ? "dealsIn=" + dealsIn + ", " : "") +
            (officeAddress != null ? "officeAddress=" + officeAddress + ", " : "") +
            (companyPhoneNumber != null ? "companyPhoneNumber=" + companyPhoneNumber + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            "}";
    }
}
