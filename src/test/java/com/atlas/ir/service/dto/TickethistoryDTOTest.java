package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TickethistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TickethistoryDTO.class);
        TickethistoryDTO tickethistoryDTO1 = new TickethistoryDTO();
        tickethistoryDTO1.setId(1L);
        TickethistoryDTO tickethistoryDTO2 = new TickethistoryDTO();
        assertThat(tickethistoryDTO1).isNotEqualTo(tickethistoryDTO2);
        tickethistoryDTO2.setId(tickethistoryDTO1.getId());
        assertThat(tickethistoryDTO1).isEqualTo(tickethistoryDTO2);
        tickethistoryDTO2.setId(2L);
        assertThat(tickethistoryDTO1).isNotEqualTo(tickethistoryDTO2);
        tickethistoryDTO1.setId(null);
        assertThat(tickethistoryDTO1).isNotEqualTo(tickethistoryDTO2);
    }
}
