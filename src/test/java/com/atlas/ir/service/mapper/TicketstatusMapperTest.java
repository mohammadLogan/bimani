package com.atlas.ir.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketstatusMapperTest {

    private TicketstatusMapper ticketstatusMapper;

    @BeforeEach
    public void setUp() {
        ticketstatusMapper = new TicketstatusMapperImpl();
    }
}
