package com.atlas.ir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Cau20statusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cau20status.class);
        Cau20status cau20status1 = new Cau20status();
        cau20status1.setId(1L);
        Cau20status cau20status2 = new Cau20status();
        cau20status2.setId(cau20status1.getId());
        assertThat(cau20status1).isEqualTo(cau20status2);
        cau20status2.setId(2L);
        assertThat(cau20status1).isNotEqualTo(cau20status2);
        cau20status1.setId(null);
        assertThat(cau20status1).isNotEqualTo(cau20status2);
    }
}
