package com.atlas.ir.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepairshopMapperTest {

    private RepairshopMapper repairshopMapper;

    @BeforeEach
    public void setUp() {
        repairshopMapper = new RepairshopMapperImpl();
    }
}
