package com.atlas.ir.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsuranceMapperTest {

    private InsuranceMapper insuranceMapper;

    @BeforeEach
    public void setUp() {
        insuranceMapper = new InsuranceMapperImpl();
    }
}
