package com.makeitgoals.rentalstore.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.makeitgoals.rentalstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RentalOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentalOrder.class);
        RentalOrder rentalOrder1 = new RentalOrder();
        rentalOrder1.setId(1L);
        RentalOrder rentalOrder2 = new RentalOrder();
        rentalOrder2.setId(rentalOrder1.getId());
        assertThat(rentalOrder1).isEqualTo(rentalOrder2);
        rentalOrder2.setId(2L);
        assertThat(rentalOrder1).isNotEqualTo(rentalOrder2);
        rentalOrder1.setId(null);
        assertThat(rentalOrder1).isNotEqualTo(rentalOrder2);
    }
}
