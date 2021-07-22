package com.makeitgoals.rentalstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "customer", "billLineItems", "orderItems" }, allowSetters = true)
    private RentalOrder rentalOrder;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productCategory" }, allowSetters = true)
    private Product product;

    @OneToMany(mappedBy = "orderItem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "orderItem", "customer" }, allowSetters = true)
    private Set<ItemBalanceByCustomer> itemBalanceByCustomers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderItem id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public RentalOrder getRentalOrder() {
        return this.rentalOrder;
    }

    public OrderItem rentalOrder(RentalOrder rentalOrder) {
        this.setRentalOrder(rentalOrder);
        return this;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public Product getProduct() {
        return this.product;
    }

    public OrderItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<ItemBalanceByCustomer> getItemBalanceByCustomers() {
        return this.itemBalanceByCustomers;
    }

    public OrderItem itemBalanceByCustomers(Set<ItemBalanceByCustomer> itemBalanceByCustomers) {
        this.setItemBalanceByCustomers(itemBalanceByCustomers);
        return this;
    }

    public OrderItem addItemBalanceByCustomer(ItemBalanceByCustomer itemBalanceByCustomer) {
        this.itemBalanceByCustomers.add(itemBalanceByCustomer);
        itemBalanceByCustomer.setOrderItem(this);
        return this;
    }

    public OrderItem removeItemBalanceByCustomer(ItemBalanceByCustomer itemBalanceByCustomer) {
        this.itemBalanceByCustomers.remove(itemBalanceByCustomer);
        itemBalanceByCustomer.setOrderItem(null);
        return this;
    }

    public void setItemBalanceByCustomers(Set<ItemBalanceByCustomer> itemBalanceByCustomers) {
        if (this.itemBalanceByCustomers != null) {
            this.itemBalanceByCustomers.forEach(i -> i.setOrderItem(null));
        }
        if (itemBalanceByCustomers != null) {
            itemBalanceByCustomers.forEach(i -> i.setOrderItem(this));
        }
        this.itemBalanceByCustomers = itemBalanceByCustomers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
