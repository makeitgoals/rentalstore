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
 * Criteria class for the {@link com.makeitgoals.rentalstore.domain.ProductCategory} entity. This class is used
 * in {@link com.makeitgoals.rentalstore.web.rest.ProductCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter productCategoryName;

    private StringFilter productCategoryDescription;

    public ProductCategoryCriteria() {}

    public ProductCategoryCriteria(ProductCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.productCategoryName = other.productCategoryName == null ? null : other.productCategoryName.copy();
        this.productCategoryDescription = other.productCategoryDescription == null ? null : other.productCategoryDescription.copy();
    }

    @Override
    public ProductCategoryCriteria copy() {
        return new ProductCategoryCriteria(this);
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

    public StringFilter getProductCategoryName() {
        return productCategoryName;
    }

    public StringFilter productCategoryName() {
        if (productCategoryName == null) {
            productCategoryName = new StringFilter();
        }
        return productCategoryName;
    }

    public void setProductCategoryName(StringFilter productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public StringFilter getProductCategoryDescription() {
        return productCategoryDescription;
    }

    public StringFilter productCategoryDescription() {
        if (productCategoryDescription == null) {
            productCategoryDescription = new StringFilter();
        }
        return productCategoryDescription;
    }

    public void setProductCategoryDescription(StringFilter productCategoryDescription) {
        this.productCategoryDescription = productCategoryDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCategoryCriteria that = (ProductCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(productCategoryName, that.productCategoryName) &&
            Objects.equals(productCategoryDescription, that.productCategoryDescription)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productCategoryName, productCategoryDescription);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (productCategoryName != null ? "productCategoryName=" + productCategoryName + ", " : "") +
            (productCategoryDescription != null ? "productCategoryDescription=" + productCategoryDescription + ", " : "") +
            "}";
    }
}
