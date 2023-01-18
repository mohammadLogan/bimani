package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketstatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketstatusDTO.class);
        TicketstatusDTO ticketstatusDTO1 = new TicketstatusDTO();
        ticketstatusDTO1.setId(1L);
        TicketstatusDTO ticketstatusDTO2 = new TicketstatusDTO();
        assertThat(ticketstatusDTO1).isNotEqualTo(ticketstatusDTO2);
        ticketstatusDTO2.setId(ticketstatusDTO1.getId());
        assertThat(ticketstatusDTO1).isEqualTo(ticketstatusDTO2);
        ticketstatusDTO2.setId(2L);
        assertThat(ticketstatusDTO1).isNotEqualTo(ticketstatusDTO2);
        ticketstatusDTO1.setId(null);
        assertThat(ticketstatusDTO1).isNotEqualTo(ticketstatusDTO2);
    }
}
