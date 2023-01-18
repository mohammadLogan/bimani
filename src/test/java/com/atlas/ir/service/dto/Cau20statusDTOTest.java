package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Cau20statusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cau20statusDTO.class);
        Cau20statusDTO cau20statusDTO1 = new Cau20statusDTO();
        cau20statusDTO1.setId(1L);
        Cau20statusDTO cau20statusDTO2 = new Cau20statusDTO();
        assertThat(cau20statusDTO1).isNotEqualTo(cau20statusDTO2);
        cau20statusDTO2.setId(cau20statusDTO1.getId());
        assertThat(cau20statusDTO1).isEqualTo(cau20statusDTO2);
        cau20statusDTO2.setId(2L);
        assertThat(cau20statusDTO1).isNotEqualTo(cau20statusDTO2);
        cau20statusDTO1.setId(null);
        assertThat(cau20statusDTO1).isNotEqualTo(cau20statusDTO2);
    }
}
