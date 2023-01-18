package com.atlas.ir.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsurancecaseMapperTest {

    private InsurancecaseMapper insurancecaseMapper;

    @BeforeEach
    public void setUp() {
        insurancecaseMapper = new InsurancecaseMapperImpl();
    }
}
