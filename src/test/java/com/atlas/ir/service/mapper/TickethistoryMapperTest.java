package com.atlas.ir.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TickethistoryMapperTest {

    private TickethistoryMapper tickethistoryMapper;

    @BeforeEach
    public void setUp() {
        tickethistoryMapper = new TickethistoryMapperImpl();
    }
}
