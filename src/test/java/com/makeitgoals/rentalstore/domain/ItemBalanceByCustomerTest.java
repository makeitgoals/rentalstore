package com.makeitgoals.rentalstore.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.makeitgoals.rentalstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemBalanceByCustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemBalanceByCustomer.class);
        ItemBalanceByCustomer itemBalanceByCustomer1 = new ItemBalanceByCustomer();
        itemBalanceByCustomer1.setId(1L);
        ItemBalanceByCustomer itemBalanceByCustomer2 = new ItemBalanceByCustomer();
        itemBalanceByCustomer2.setId(itemBalanceByCustomer1.getId());
        assertThat(itemBalanceByCustomer1).isEqualTo(itemBalanceByCustomer2);
        itemBalanceByCustomer2.setId(2L);
        assertThat(itemBalanceByCustomer1).isNotEqualTo(itemBalanceByCustomer2);
        itemBalanceByCustomer1.setId(null);
        assertThat(itemBalanceByCustomer1).isNotEqualTo(itemBalanceByCustomer2);
    }
}
