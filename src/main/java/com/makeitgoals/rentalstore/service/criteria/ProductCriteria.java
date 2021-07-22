package com.makeitgoals.rentalstore.service.criteria;

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
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.Product} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter productName;

    private StringFilter productDescription;

    private StringFilter productSize;

    private BigDecimalFilter pricePerDay;

    private LongFilter productCategoryId;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.productName = other.productName == null ? null : other.productName.copy();
        this.productDescription = other.productDescription == null ? null : other.productDescription.copy();
        this.productSize = other.productSize == null ? null : other.productSize.copy();
        this.pricePerDay = other.pricePerDay == null ? null : other.pricePerDay.copy();
        this.productCategoryId = other.productCategoryId == null ? null : other.productCategoryId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public StringFilter getProductName() {
        return productName;
    }

    public StringFilter productName() {
        if (productName == null) {
            productName = new StringFilter();
        }
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public StringFilter getProductDescription() {
        return productDescription;
    }

    public StringFilter productDescription() {
        if (productDescription == null) {
            productDescription = new StringFilter();
        }
        return productDescription;
    }

    public void setProductDescription(StringFilter productDescription) {
        this.productDescription = productDescription;
    }

    public StringFilter getProductSize() {
        return productSize;
    }

    public StringFilter productSize() {
        if (productSize == null) {
            productSize = new StringFilter();
        }
        return productSize;
    }

    public void setProductSize(StringFilter productSize) {
        this.productSize = productSize;
    }

    public BigDecimalFilter getPricePerDay() {
        return pricePerDay;
    }

    public BigDecimalFilter pricePerDay() {
        if (pricePerDay == null) {
            pricePerDay = new BigDecimalFilter();
        }
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimalFilter pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public LongFilter getProductCategoryId() {
        return productCategoryId;
    }

    public LongFilter productCategoryId() {
        if (productCategoryId == null) {
            productCategoryId = new LongFilter();
        }
        return productCategoryId;
    }

    public void setProductCategoryId(LongFilter productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(productDescription, that.productDescription) &&
            Objects.equals(productSize, that.productSize) &&
            Objects.equals(pricePerDay, that.pricePerDay) &&
            Objects.equals(productCategoryId, that.productCategoryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, productDescription, productSize, pricePerDay, productCategoryId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (productName != null ? "productName=" + productName + ", " : "") +
            (productDescription != null ? "productDescription=" + productDescription + ", " : "") +
            (productSize != null ? "productSize=" + productSize + ", " : "") +
            (pricePerDay != null ? "pricePerDay=" + pricePerDay + ", " : "") +
            (productCategoryId != null ? "productCategoryId=" + productCategoryId + ", " : "") +
            "}";
    }
}
