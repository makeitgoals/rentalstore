package com.makeitgoals.rentalstore.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.makeitgoals.rentalstore.domain.BillLineItem} entity.
 */
public class BillLineItemDTO implements Serializable {

    private Long id;

    private String details;

    @NotNull
    private Instant fromDate;

    @NotNull
    private Instant toDate;

    @NotNull
    @Min(value = 0)
    private Integer outstandingQuantity;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal lineAmount;

    private ProductDTO product;

    private BillDTO bill;

    private RentalOrderDTO rentalOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public Integer getOutstandingQuantity() {
        return outstandingQuantity;
    }

    public void setOutstandingQuantity(Integer outstandingQuantity) {
        this.outstandingQuantity = outstandingQuantity;
    }

    public BigDecimal getLineAmount() {
        return lineAmount;
    }

    public void setLineAmount(BigDecimal lineAmount) {
        this.lineAmount = lineAmount;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public BillDTO getBill() {
        return bill;
    }

    public void setBill(BillDTO bill) {
        this.bill = bill;
    }

    public RentalOrderDTO getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrderDTO rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillLineItemDTO)) {
            return false;
        }

        BillLineItemDTO billLineItemDTO = (BillLineItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billLineItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillLineItemDTO{" +
            "id=" + getId() +
            ", details='" + getDetails() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", outstandingQuantity=" + getOutstandingQuantity() +
            ", lineAmount=" + getLineAmount() +
            ", product=" + getProduct() +
            ", bill=" + getBill() +
            ", rentalOrder=" + getRentalOrder() +
            "}";
    }
}
