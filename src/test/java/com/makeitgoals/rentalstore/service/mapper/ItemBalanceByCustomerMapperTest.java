package com.makeitgoals.rentalstore.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemBalanceByCustomerMapperTest {

    private ItemBalanceByCustomerMapper itemBalanceByCustomerMapper;

    @BeforeEach
    public void setUp() {
        itemBalanceByCustomerMapper = new ItemBalanceByCustomerMapperImpl();
    }
}
