package com.makeitgoals.rentalstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ItemBalanceByCustomer.
 */
@Entity
@Table(name = "item_balance_by_customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemBalanceByCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "outstanding_balance", nullable = false)
    private Integer outstandingBalance;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productCategory" }, allowSetters = true)
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "rentalOrder", "product", "itemBalanceByCustomers" }, allowSetters = true)
    private OrderItem orderItem;

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

    public ItemBalanceByCustomer id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getOutstandingBalance() {
        return this.outstandingBalance;
    }

    public ItemBalanceByCustomer outstandingBalance(Integer outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
        return this;
    }

    public void setOutstandingBalance(Integer outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public Product getProduct() {
        return this.product;
    }

    public ItemBalanceByCustomer product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public ItemBalanceByCustomer orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public ItemBalanceByCustomer customer(Customer customer) {
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
        if (!(o instanceof ItemBalanceByCustomer)) {
            return false;
        }
        return id != null && id.equals(((ItemBalanceByCustomer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemBalanceByCustomer{" +
            "id=" + getId() +
            ", outstandingBalance=" + getOutstandingBalance() +
            "}";
    }
}
