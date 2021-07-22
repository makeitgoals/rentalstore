package com.makeitgoals.rentalstore.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.makeitgoals.rentalstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillLineItemToOrderItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillLineItemToOrderItemDTO.class);
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO1 = new BillLineItemToOrderItemDTO();
        billLineItemToOrderItemDTO1.setId(1L);
        BillLineItemToOrderItemDTO billLineItemToOrderItemDTO2 = new BillLineItemToOrderItemDTO();
        assertThat(billLineItemToOrderItemDTO1).isNotEqualTo(billLineItemToOrderItemDTO2);
        billLineItemToOrderItemDTO2.setId(billLineItemToOrderItemDTO1.getId());
        assertThat(billLineItemToOrderItemDTO1).isEqualTo(billLineItemToOrderItemDTO2);
        billLineItemToOrderItemDTO2.setId(2L);
        assertThat(billLineItemToOrderItemDTO1).isNotEqualTo(billLineItemToOrderItemDTO2);
        billLineItemToOrderItemDTO1.setId(null);
        assertThat(billLineItemToOrderItemDTO1).isNotEqualTo(billLineItemToOrderItemDTO2);
    }
}
