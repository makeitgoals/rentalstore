package com.makeitgoals.rentalstore.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.makeitgoals.rentalstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillLineItemToOrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillLineItemToOrderItem.class);
        BillLineItemToOrderItem billLineItemToOrderItem1 = new BillLineItemToOrderItem();
        billLineItemToOrderItem1.setId(1L);
        BillLineItemToOrderItem billLineItemToOrderItem2 = new BillLineItemToOrderItem();
        billLineItemToOrderItem2.setId(billLineItemToOrderItem1.getId());
        assertThat(billLineItemToOrderItem1).isEqualTo(billLineItemToOrderItem2);
        billLineItemToOrderItem2.setId(2L);
        assertThat(billLineItemToOrderItem1).isNotEqualTo(billLineItemToOrderItem2);
        billLineItemToOrderItem1.setId(null);
        assertThat(billLineItemToOrderItem1).isNotEqualTo(billLineItemToOrderItem2);
    }
}
