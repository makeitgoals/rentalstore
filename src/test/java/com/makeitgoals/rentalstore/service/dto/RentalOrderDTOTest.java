package com.makeitgoals.rentalstore.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.makeitgoals.rentalstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RentalOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentalOrderDTO.class);
        RentalOrderDTO rentalOrderDTO1 = new RentalOrderDTO();
        rentalOrderDTO1.setId(1L);
        RentalOrderDTO rentalOrderDTO2 = new RentalOrderDTO();
        assertThat(rentalOrderDTO1).isNotEqualTo(rentalOrderDTO2);
        rentalOrderDTO2.setId(rentalOrderDTO1.getId());
        assertThat(rentalOrderDTO1).isEqualTo(rentalOrderDTO2);
        rentalOrderDTO2.setId(2L);
        assertThat(rentalOrderDTO1).isNotEqualTo(rentalOrderDTO2);
        rentalOrderDTO1.setId(null);
        assertThat(rentalOrderDTO1).isNotEqualTo(rentalOrderDTO2);
    }
}
