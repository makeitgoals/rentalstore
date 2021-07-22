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
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "site_address")
    private String siteAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "orderItem", "customer" }, allowSetters = true)
    private Set<ItemBalanceByCustomer> itemBalanceByCustomers = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer", "billLineItems", "orderItems" }, allowSetters = true)
    private Set<RentalOrder> rentalOrders = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<Bill> bills = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer id(Long id) {
        this.id = id;
        return this;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Customer customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactName() {
        return this.contactName;
    }

    public Customer contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public Customer fatherName(String fatherName) {
        this.fatherName = fatherName;
        return this;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public Customer ownerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSiteAddress() {
        return this.siteAddress;
    }

    public Customer siteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
        return this;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<ItemBalanceByCustomer> getItemBalanceByCustomers() {
        return this.itemBalanceByCustomers;
    }

    public Customer itemBalanceByCustomers(Set<ItemBalanceByCustomer> itemBalanceByCustomers) {
        this.setItemBalanceByCustomers(itemBalanceByCustomers);
        return this;
    }

    public Customer addItemBalanceByCustomer(ItemBalanceByCustomer itemBalanceByCustomer) {
        this.itemBalanceByCustomers.add(itemBalanceByCustomer);
        itemBalanceByCustomer.setCustomer(this);
        return this;
    }

    public Customer removeItemBalanceByCustomer(ItemBalanceByCustomer itemBalanceByCustomer) {
        this.itemBalanceByCustomers.remove(itemBalanceByCustomer);
        itemBalanceByCustomer.setCustomer(null);
        return this;
    }

    public void setItemBalanceByCustomers(Set<ItemBalanceByCustomer> itemBalanceByCustomers) {
        if (this.itemBalanceByCustomers != null) {
            this.itemBalanceByCustomers.forEach(i -> i.setCustomer(null));
        }
        if (itemBalanceByCustomers != null) {
            itemBalanceByCustomers.forEach(i -> i.setCustomer(this));
        }
        this.itemBalanceByCustomers = itemBalanceByCustomers;
    }

    public Set<RentalOrder> getRentalOrders() {
        return this.rentalOrders;
    }

    public Customer rentalOrders(Set<RentalOrder> rentalOrders) {
        this.setRentalOrders(rentalOrders);
        return this;
    }

    public Customer addRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrders.add(rentalOrder);
        rentalOrder.setCustomer(this);
        return this;
    }

    public Customer removeRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrders.remove(rentalOrder);
        rentalOrder.setCustomer(null);
        return this;
    }

    public void setRentalOrders(Set<RentalOrder> rentalOrders) {
        if (this.rentalOrders != null) {
            this.rentalOrders.forEach(i -> i.setCustomer(null));
        }
        if (rentalOrders != null) {
            rentalOrders.forEach(i -> i.setCustomer(this));
        }
        this.rentalOrders = rentalOrders;
    }

    public Set<Bill> getBills() {
        return this.bills;
    }

    public Customer bills(Set<Bill> bills) {
        this.setBills(bills);
        return this;
    }

    public Customer addBill(Bill bill) {
        this.bills.add(bill);
        bill.setCustomer(this);
        return this;
    }

    public Customer removeBill(Bill bill) {
        this.bills.remove(bill);
        bill.setCustomer(null);
        return this;
    }

    public void setBills(Set<Bill> bills) {
        if (this.bills != null) {
            this.bills.forEach(i -> i.setCustomer(null));
        }
        if (bills != null) {
            bills.forEach(i -> i.setCustomer(this));
        }
        this.bills = bills;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public Customer payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Customer addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setCustomer(this);
        return this;
    }

    public Customer removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setCustomer(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setCustomer(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setCustomer(this));
        }
        this.payments = payments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", customerName='" + getCustomerName() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", siteAddress='" + getSiteAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
