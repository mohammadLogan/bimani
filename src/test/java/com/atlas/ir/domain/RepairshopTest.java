package com.atlas.ir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepairshopTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repairshop.class);
        Repairshop repairshop1 = new Repairshop();
        repairshop1.setId(1L);
        Repairshop repairshop2 = new Repairshop();
        repairshop2.setId(repairshop1.getId());
        assertThat(repairshop1).isEqualTo(repairshop2);
        repairshop2.setId(2L);
        assertThat(repairshop1).isNotEqualTo(repairshop2);
        repairshop1.setId(null);
        assertThat(repairshop1).isNotEqualTo(repairshop2);
    }
}
