package com.atlas.ir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CasetypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Casetype.class);
        Casetype casetype1 = new Casetype();
        casetype1.setId(1L);
        Casetype casetype2 = new Casetype();
        casetype2.setId(casetype1.getId());
        assertThat(casetype1).isEqualTo(casetype2);
        casetype2.setId(2L);
        assertThat(casetype1).isNotEqualTo(casetype2);
        casetype1.setId(null);
        assertThat(casetype1).isNotEqualTo(casetype2);
    }
}
