package com.makeitgoals.rentalstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BillLineItemToOrderItem.
 */
@Entity
@Table(name = "bill_line_item_to_order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BillLineItemToOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "details")
    private String details;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rentalOrder", "product", "itemBalanceByCustomers" }, allowSetters = true)
    private OrderItem orderItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customer", "billLineItems", "orderItems" }, allowSetters = true)
    private RentalOrder rentalOrder;

    @ManyToOne
    @JsonIgnoreProperties(value = { "product", "bill", "rentalOrder" }, allowSetters = true)
    private BillLineItem billLineItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BillLineItemToOrderItem id(Long id) {
        this.id = id;
        return this;
    }

    public String getDetails() {
        return this.details;
    }

    public BillLineItemToOrderItem details(String details) {
        this.details = details;
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public BillLineItemToOrderItem orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public RentalOrder getRentalOrder() {
        return this.rentalOrder;
    }

    public BillLineItemToOrderItem rentalOrder(RentalOrder rentalOrder) {
        this.setRentalOrder(rentalOrder);
        return this;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public BillLineItem getBillLineItem() {
        return this.billLineItem;
    }

    public BillLineItemToOrderItem billLineItem(BillLineItem billLineItem) {
        this.setBillLineItem(billLineItem);
        return this;
    }

    public void setBillLineItem(BillLineItem billLineItem) {
        this.billLineItem = billLineItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillLineItemToOrderItem)) {
            return false;
        }
        return id != null && id.equals(((BillLineItemToOrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillLineItemToOrderItem{" +
            "id=" + getId() +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
