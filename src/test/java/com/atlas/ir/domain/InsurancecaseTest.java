package com.atlas.ir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsurancecaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Insurancecase.class);
        Insurancecase insurancecase1 = new Insurancecase();
        insurancecase1.setId(1L);
        Insurancecase insurancecase2 = new Insurancecase();
        insurancecase2.setId(insurancecase1.getId());
        assertThat(insurancecase1).isEqualTo(insurancecase2);
        insurancecase2.setId(2L);
        assertThat(insurancecase1).isNotEqualTo(insurancecase2);
        insurancecase1.setId(null);
        assertThat(insurancecase1).isNotEqualTo(insurancecase2);
    }
}
