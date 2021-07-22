package com.makeitgoals.rentalstore.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.makeitgoals.rentalstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillLineItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillLineItem.class);
        BillLineItem billLineItem1 = new BillLineItem();
        billLineItem1.setId(1L);
        BillLineItem billLineItem2 = new BillLineItem();
        billLineItem2.setId(billLineItem1.getId());
        assertThat(billLineItem1).isEqualTo(billLineItem2);
        billLineItem2.setId(2L);
        assertThat(billLineItem1).isNotEqualTo(billLineItem2);
        billLineItem1.setId(null);
        assertThat(billLineItem1).isNotEqualTo(billLineItem2);
    }
}
