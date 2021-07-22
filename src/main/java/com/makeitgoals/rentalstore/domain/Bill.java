package com.makeitgoals.rentalstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.makeitgoals.rentalstore.domain.enumeration.BillStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "cost_bill_number")
    private Integer costBillNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "bill_status", nullable = false)
    private BillStatus billStatus;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "bill_total", precision = 21, scale = 2, nullable = false)
    private BigDecimal billTotal;

    @Min(value = 0L)
    @Column(name = "tax_percent")
    private Long taxPercent;

    @DecimalMin(value = "0")
    @Column(name = "bill_total_with_tax", precision = 21, scale = 2)
    private BigDecimal billTotalWithTax;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "itemBalanceByCustomers", "rentalOrders", "bills", "payments" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bill id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Bill code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCostBillNumber() {
        return this.costBillNumber;
    }

    public Bill costBillNumber(Integer costBillNumber) {
        this.costBillNumber = costBillNumber;
        return this;
    }

    public void setCostBillNumber(Integer costBillNumber) {
        this.costBillNumber = costBillNumber;
    }

    public BillStatus getBillStatus() {
        return this.billStatus;
    }

    public Bill billStatus(BillStatus billStatus) {
        this.billStatus = billStatus;
        return this;
    }

    public void setBillStatus(BillStatus billStatus) {
        this.billStatus = billStatus;
    }

    public BigDecimal getBillTotal() {
        return this.billTotal;
    }

    public Bill billTotal(BigDecimal billTotal) {
        this.billTotal = billTotal;
        return this;
    }

    public void setBillTotal(BigDecimal billTotal) {
        this.billTotal = billTotal;
    }

    public Long getTaxPercent() {
        return this.taxPercent;
    }

    public Bill taxPercent(Long taxPercent) {
        this.taxPercent = taxPercent;
        return this;
    }

    public void setTaxPercent(Long taxPercent) {
        this.taxPercent = taxPercent;
    }

    public BigDecimal getBillTotalWithTax() {
        return this.billTotalWithTax;
    }

    public Bill billTotalWithTax(BigDecimal billTotalWithTax) {
        this.billTotalWithTax = billTotalWithTax;
        return this;
    }

    public void setBillTotalWithTax(BigDecimal billTotalWithTax) {
        this.billTotalWithTax = billTotalWithTax;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Bill customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bill)) {
            return false;
        }
        return id != null && id.equals(((Bill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", costBillNumber=" + getCostBillNumber() +
            ", billStatus='" + getBillStatus() + "'" +
            ", billTotal=" + getBillTotal() +
            ", taxPercent=" + getTaxPercent() +
            ", billTotalWithTax=" + getBillTotalWithTax() +
            "}";
    }
}
