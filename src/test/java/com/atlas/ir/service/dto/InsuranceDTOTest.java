package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceDTO.class);
        InsuranceDTO insuranceDTO1 = new InsuranceDTO();
        insuranceDTO1.setId(1L);
        InsuranceDTO insuranceDTO2 = new InsuranceDTO();
        assertThat(insuranceDTO1).isNotEqualTo(insuranceDTO2);
        insuranceDTO2.setId(insuranceDTO1.getId());
        assertThat(insuranceDTO1).isEqualTo(insuranceDTO2);
        insuranceDTO2.setId(2L);
        assertThat(insuranceDTO1).isNotEqualTo(insuranceDTO2);
        insuranceDTO1.setId(null);
        assertThat(insuranceDTO1).isNotEqualTo(insuranceDTO2);
    }
}
