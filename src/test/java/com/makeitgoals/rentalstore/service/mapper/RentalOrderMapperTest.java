package com.makeitgoals.rentalstore.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RentalOrderMapperTest {

    private RentalOrderMapper rentalOrderMapper;

    @BeforeEach
    public void setUp() {
        rentalOrderMapper = new RentalOrderMapperImpl();
    }
}
