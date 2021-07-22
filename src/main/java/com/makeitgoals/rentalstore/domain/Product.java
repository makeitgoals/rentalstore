package com.makeitgoals.rentalstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @NotNull
    @Column(name = "product_size", nullable = false)
    private String productSize;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price_per_day", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerDay;

    @Lob
    @Column(name = "product_image")
    private byte[] productImage;

    @Column(name = "product_image_content_type")
    private String productImageContentType;

    @ManyToOne
    private ProductCategory productCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public Product productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductSize() {
        return this.productSize;
    }

    public Product productSize(String productSize) {
        this.productSize = productSize;
        return this;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public BigDecimal getPricePerDay() {
        return this.pricePerDay;
    }

    public Product pricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
        return this;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public byte[] getProductImage() {
        return this.productImage;
    }

    public Product productImage(byte[] productImage) {
        this.productImage = productImage;
        return this;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getProductImageContentType() {
        return this.productImageContentType;
    }

    public Product productImageContentType(String productImageContentType) {
        this.productImageContentType = productImageContentType;
        return this;
    }

    public void setProductImageContentType(String productImageContentType) {
        this.productImageContentType = productImageContentType;
    }

    public ProductCategory getProductCategory() {
        return this.productCategory;
    }

    public Product productCategory(ProductCategory productCategory) {
        this.setProductCategory(productCategory);
        return this;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", productSize='" + getProductSize() + "'" +
            ", pricePerDay=" + getPricePerDay() +
            ", productImage='" + getProductImage() + "'" +
            ", productImageContentType='" + getProductImageContentType() + "'" +
            "}";
    }
}
