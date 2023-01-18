package com.atlas.ir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Caseaccidentu20Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caseaccidentu20.class);
        Caseaccidentu20 caseaccidentu201 = new Caseaccidentu20();
        caseaccidentu201.setId(1L);
        Caseaccidentu20 caseaccidentu202 = new Caseaccidentu20();
        caseaccidentu202.setId(caseaccidentu201.getId());
        assertThat(caseaccidentu201).isEqualTo(caseaccidentu202);
        caseaccidentu202.setId(2L);
        assertThat(caseaccidentu201).isNotEqualTo(caseaccidentu202);
        caseaccidentu201.setId(null);
        assertThat(caseaccidentu201).isNotEqualTo(caseaccidentu202);
    }
}
