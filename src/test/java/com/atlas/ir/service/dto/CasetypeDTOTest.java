package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CasetypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasetypeDTO.class);
        CasetypeDTO casetypeDTO1 = new CasetypeDTO();
        casetypeDTO1.setId(1L);
        CasetypeDTO casetypeDTO2 = new CasetypeDTO();
        assertThat(casetypeDTO1).isNotEqualTo(casetypeDTO2);
        casetypeDTO2.setId(casetypeDTO1.getId());
        assertThat(casetypeDTO1).isEqualTo(casetypeDTO2);
        casetypeDTO2.setId(2L);
        assertThat(casetypeDTO1).isNotEqualTo(casetypeDTO2);
        casetypeDTO1.setId(null);
        assertThat(casetypeDTO1).isNotEqualTo(casetypeDTO2);
    }
}
