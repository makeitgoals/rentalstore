package com.makeitgoals.rentalstore.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.makeitgoals.rentalstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemBalanceByCustomerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemBalanceByCustomerDTO.class);
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO1 = new ItemBalanceByCustomerDTO();
        itemBalanceByCustomerDTO1.setId(1L);
        ItemBalanceByCustomerDTO itemBalanceByCustomerDTO2 = new ItemBalanceByCustomerDTO();
        assertThat(itemBalanceByCustomerDTO1).isNotEqualTo(itemBalanceByCustomerDTO2);
        itemBalanceByCustomerDTO2.setId(itemBalanceByCustomerDTO1.getId());
        assertThat(itemBalanceByCustomerDTO1).isEqualTo(itemBalanceByCustomerDTO2);
        itemBalanceByCustomerDTO2.setId(2L);
        assertThat(itemBalanceByCustomerDTO1).isNotEqualTo(itemBalanceByCustomerDTO2);
        itemBalanceByCustomerDTO1.setId(null);
        assertThat(itemBalanceByCustomerDTO1).isNotEqualTo(itemBalanceByCustomerDTO2);
    }
}
