package com.makeitgoals.rentalstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BillLineItem.
 */
@Entity
@Table(name = "bill_line_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BillLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "details")
    private String details;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private Instant fromDate;

    @NotNull
    @Column(name = "to_date", nullable = false)
    private Instant toDate;

    @NotNull
    @Min(value = 0)
    @Column(name = "outstanding_quantity", nullable = false)
    private Integer outstandingQuantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "line_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal lineAmount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productCategory" }, allowSetters = true)
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Bill bill;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "customer", "billLineItems", "orderItems" }, allowSetters = true)
    private RentalOrder rentalOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BillLineItem id(Long id) {
        this.id = id;
        return this;
    }

    public String getDetails() {
        return this.details;
    }

    public BillLineItem details(String details) {
        this.details = details;
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public BillLineItem fromDate(Instant fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return this.toDate;
    }

    public BillLineItem toDate(Instant toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public Integer getOutstandingQuantity() {
        return this.outstandingQuantity;
    }

    public BillLineItem outstandingQuantity(Integer outstandingQuantity) {
        this.outstandingQuantity = outstandingQuantity;
        return this;
    }

    public void setOutstandingQuantity(Integer outstandingQuantity) {
        this.outstandingQuantity = outstandingQuantity;
    }

    public BigDecimal getLineAmount() {
        return this.lineAmount;
    }

    public BillLineItem lineAmount(BigDecimal lineAmount) {
        this.lineAmount = lineAmount;
        return this;
    }

    public void setLineAmount(BigDecimal lineAmount) {
        this.lineAmount = lineAmount;
    }

    public Product getProduct() {
        return this.product;
    }

    public BillLineItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Bill getBill() {
        return this.bill;
    }

    public BillLineItem bill(Bill bill) {
        this.setBill(bill);
        return this;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public RentalOrder getRentalOrder() {
        return this.rentalOrder;
    }

    public BillLineItem rentalOrder(RentalOrder rentalOrder) {
        this.setRentalOrder(rentalOrder);
        return this;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillLineItem)) {
            return false;
        }
        return id != null && id.equals(((BillLineItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillLineItem{" +
            "id=" + getId() +
            ", details='" + getDetails() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", outstandingQuantity=" + getOutstandingQuantity() +
            ", lineAmount=" + getLineAmount() +
            "}";
    }
}
