package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Caseaccidentu20DTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caseaccidentu20DTO.class);
        Caseaccidentu20DTO caseaccidentu20DTO1 = new Caseaccidentu20DTO();
        caseaccidentu20DTO1.setId(1L);
        Caseaccidentu20DTO caseaccidentu20DTO2 = new Caseaccidentu20DTO();
        assertThat(caseaccidentu20DTO1).isNotEqualTo(caseaccidentu20DTO2);
        caseaccidentu20DTO2.setId(caseaccidentu20DTO1.getId());
        assertThat(caseaccidentu20DTO1).isEqualTo(caseaccidentu20DTO2);
        caseaccidentu20DTO2.setId(2L);
        assertThat(caseaccidentu20DTO1).isNotEqualTo(caseaccidentu20DTO2);
        caseaccidentu20DTO1.setId(null);
        assertThat(caseaccidentu20DTO1).isNotEqualTo(caseaccidentu20DTO2);
    }
}
