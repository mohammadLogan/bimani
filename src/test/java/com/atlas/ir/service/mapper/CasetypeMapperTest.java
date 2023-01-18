package com.atlas.ir.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CasetypeMapperTest {

    private CasetypeMapper casetypeMapper;

    @BeforeEach
    public void setUp() {
        casetypeMapper = new CasetypeMapperImpl();
    }
}
