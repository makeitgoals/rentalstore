package com.makeitgoals.rentalstore.service.dto;

import com.makeitgoals.rentalstore.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.makeitgoals.rentalstore.domain.RentalOrder} entity.
 */
public class RentalOrderDTO implements Serializable {

    private Long id;

    private Instant rentalIssueDate;

    private Instant rentalReturnDate;

    @NotNull
    private OrderStatus rentalOrderStatus;

    @NotNull
    private String code;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getRentalIssueDate() {
        return rentalIssueDate;
    }

    public void setRentalIssueDate(Instant rentalIssueDate) {
        this.rentalIssueDate = rentalIssueDate;
    }

    public Instant getRentalReturnDate() {
        return rentalReturnDate;
    }

    public void setRentalReturnDate(Instant rentalReturnDate) {
        this.rentalReturnDate = rentalReturnDate;
    }

    public OrderStatus getRentalOrderStatus() {
        return rentalOrderStatus;
    }

    public void setRentalOrderStatus(OrderStatus rentalOrderStatus) {
        this.rentalOrderStatus = rentalOrderStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(o instanceof RentalOrderDTO)) {
            return false;
        }

        RentalOrderDTO rentalOrderDTO = (RentalOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rentalOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalOrderDTO{" +
            "id=" + getId() +
            ", rentalIssueDate='" + getRentalIssueDate() + "'" +
            ", rentalReturnDate='" + getRentalReturnDate() + "'" +
            ", rentalOrderStatus='" + getRentalOrderStatus() + "'" +
            ", code='" + getCode() + "'" +
            ", customer=" + getCustomer() +
            "}";
    }
}
