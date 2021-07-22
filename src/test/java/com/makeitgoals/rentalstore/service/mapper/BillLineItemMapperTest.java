package com.makeitgoals.rentalstore.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillLineItemMapperTest {

    private BillLineItemMapper billLineItemMapper;

    @BeforeEach
    public void setUp() {
        billLineItemMapper = new BillLineItemMapperImpl();
    }
}
