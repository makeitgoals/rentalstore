package com.makeitgoals.rentalstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.makeitgoals.rentalstore.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RentalOrder.
 */
@Entity
@Table(name = "rental_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RentalOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "rental_issue_date")
    private Instant rentalIssueDate;

    @Column(name = "rental_return_date")
    private Instant rentalReturnDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rental_order_status", nullable = false)
    private OrderStatus rentalOrderStatus;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne
    @JsonIgnoreProperties(value = { "itemBalanceByCustomers", "rentalOrders", "bills", "payments" }, allowSetters = true)
    private Customer customer;

    @OneToMany(mappedBy = "rentalOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "bill", "rentalOrder" }, allowSetters = true)
    private Set<BillLineItem> billLineItems = new HashSet<>();

    @OneToMany(mappedBy = "rentalOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rentalOrder", "product", "itemBalanceByCustomers" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RentalOrder id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getRentalIssueDate() {
        return this.rentalIssueDate;
    }

    public RentalOrder rentalIssueDate(Instant rentalIssueDate) {
        this.rentalIssueDate = rentalIssueDate;
        return this;
    }

    public void setRentalIssueDate(Instant rentalIssueDate) {
        this.rentalIssueDate = rentalIssueDate;
    }

    public Instant getRentalReturnDate() {
        return this.rentalReturnDate;
    }

    public RentalOrder rentalReturnDate(Instant rentalReturnDate) {
        this.rentalReturnDate = rentalReturnDate;
        return this;
    }

    public void setRentalReturnDate(Instant rentalReturnDate) {
        this.rentalReturnDate = rentalReturnDate;
    }

    public OrderStatus getRentalOrderStatus() {
        return this.rentalOrderStatus;
    }

    public RentalOrder rentalOrderStatus(OrderStatus rentalOrderStatus) {
        this.rentalOrderStatus = rentalOrderStatus;
        return this;
    }

    public void setRentalOrderStatus(OrderStatus rentalOrderStatus) {
        this.rentalOrderStatus = rentalOrderStatus;
    }

    public String getCode() {
        return this.code;
    }

    public RentalOrder code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public RentalOrder customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<BillLineItem> getBillLineItems() {
        return this.billLineItems;
    }

    public RentalOrder billLineItems(Set<BillLineItem> billLineItems) {
        this.setBillLineItems(billLineItems);
        return this;
    }

    public RentalOrder addBillLineItem(BillLineItem billLineItem) {
        this.billLineItems.add(billLineItem);
        billLineItem.setRentalOrder(this);
        return this;
    }

    public RentalOrder removeBillLineItem(BillLineItem billLineItem) {
        this.billLineItems.remove(billLineItem);
        billLineItem.setRentalOrder(null);
        return this;
    }

    public void setBillLineItems(Set<BillLineItem> billLineItems) {
        if (this.billLineItems != null) {
            this.billLineItems.forEach(i -> i.setRentalOrder(null));
        }
        if (billLineItems != null) {
            billLineItems.forEach(i -> i.setRentalOrder(this));
        }
        this.billLineItems = billLineItems;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public RentalOrder orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public RentalOrder addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setRentalOrder(this);
        return this;
    }

    public RentalOrder removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setRentalOrder(null);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setRentalOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setRentalOrder(this));
        }
        this.orderItems = orderItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentalOrder)) {
            return false;
        }
        return id != null && id.equals(((RentalOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalOrder{" +
            "id=" + getId() +
            ", rentalIssueDate='" + getRentalIssueDate() + "'" +
            ", rentalReturnDate='" + getRentalReturnDate() + "'" +
            ", rentalOrderStatus='" + getRentalOrderStatus() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
