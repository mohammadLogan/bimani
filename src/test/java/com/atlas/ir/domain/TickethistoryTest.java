package com.atlas.ir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TickethistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tickethistory.class);
        Tickethistory tickethistory1 = new Tickethistory();
        tickethistory1.setId(1L);
        Tickethistory tickethistory2 = new Tickethistory();
        tickethistory2.setId(tickethistory1.getId());
        assertThat(tickethistory1).isEqualTo(tickethistory2);
        tickethistory2.setId(2L);
        assertThat(tickethistory1).isNotEqualTo(tickethistory2);
        tickethistory1.setId(null);
        assertThat(tickethistory1).isNotEqualTo(tickethistory2);
    }
}
