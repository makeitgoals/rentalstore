package com.makeitgoals.rentalstore.service.dto;

import com.makeitgoals.rentalstore.domain.enumeration.BillStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.makeitgoals.rentalstore.domain.Bill} entity.
 */
public class BillDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    private Integer costBillNumber;

    @NotNull
    private BillStatus billStatus;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal billTotal;

    @Min(value = 0L)
    private Long taxPercent;

    @DecimalMin(value = "0")
    private BigDecimal billTotalWithTax;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCostBillNumber() {
        return costBillNumber;
    }

    public void setCostBillNumber(Integer costBillNumber) {
        this.costBillNumber = costBillNumber;
    }

    public BillStatus getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(BillStatus billStatus) {
        this.billStatus = billStatus;
    }

    public BigDecimal getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(BigDecimal billTotal) {
        this.billTotal = billTotal;
    }

    public Long getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(Long taxPercent) {
        this.taxPercent = taxPercent;
    }

    public BigDecimal getBillTotalWithTax() {
        return billTotalWithTax;
    }

    public void setBillTotalWithTax(BigDecimal billTotalWithTax) {
        this.billTotalWithTax = billTotalWithTax;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillDTO)) {
            return false;
        }

        BillDTO billDTO = (BillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", costBillNumber=" + getCostBillNumber() +
            ", billStatus='" + getBillStatus() + "'" +
            ", billTotal=" + getBillTotal() +
            ", taxPercent=" + getTaxPercent() +
            ", billTotalWithTax=" + getBillTotalWithTax() +
            ", customer=" + getCustomer() +
            "}";
    }
}
