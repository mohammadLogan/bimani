package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsurancecaseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsurancecaseDTO.class);
        InsurancecaseDTO insurancecaseDTO1 = new InsurancecaseDTO();
        insurancecaseDTO1.setId(1L);
        InsurancecaseDTO insurancecaseDTO2 = new InsurancecaseDTO();
        assertThat(insurancecaseDTO1).isNotEqualTo(insurancecaseDTO2);
        insurancecaseDTO2.setId(insurancecaseDTO1.getId());
        assertThat(insurancecaseDTO1).isEqualTo(insurancecaseDTO2);
        insurancecaseDTO2.setId(2L);
        assertThat(insurancecaseDTO1).isNotEqualTo(insurancecaseDTO2);
        insurancecaseDTO1.setId(null);
        assertThat(insurancecaseDTO1).isNotEqualTo(insurancecaseDTO2);
    }
}
