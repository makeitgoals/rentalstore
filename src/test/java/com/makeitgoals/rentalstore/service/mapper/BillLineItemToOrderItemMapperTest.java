package com.makeitgoals.rentalstore.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillLineItemToOrderItemMapperTest {

    private BillLineItemToOrderItemMapper billLineItemToOrderItemMapper;

    @BeforeEach
    public void setUp() {
        billLineItemToOrderItemMapper = new BillLineItemToOrderItemMapperImpl();
    }
}
