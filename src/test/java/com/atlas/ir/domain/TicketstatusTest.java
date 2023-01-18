package com.atlas.ir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketstatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ticketstatus.class);
        Ticketstatus ticketstatus1 = new Ticketstatus();
        ticketstatus1.setId(1L);
        Ticketstatus ticketstatus2 = new Ticketstatus();
        ticketstatus2.setId(ticketstatus1.getId());
        assertThat(ticketstatus1).isEqualTo(ticketstatus2);
        ticketstatus2.setId(2L);
        assertThat(ticketstatus1).isNotEqualTo(ticketstatus2);
        ticketstatus1.setId(null);
        assertThat(ticketstatus1).isNotEqualTo(ticketstatus2);
    }
}
