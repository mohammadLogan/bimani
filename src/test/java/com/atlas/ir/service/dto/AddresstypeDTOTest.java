package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddresstypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddresstypeDTO.class);
        AddresstypeDTO addresstypeDTO1 = new AddresstypeDTO();
        addresstypeDTO1.setId(1L);
        AddresstypeDTO addresstypeDTO2 = new AddresstypeDTO();
        assertThat(addresstypeDTO1).isNotEqualTo(addresstypeDTO2);
        addresstypeDTO2.setId(addresstypeDTO1.getId());
        assertThat(addresstypeDTO1).isEqualTo(addresstypeDTO2);
        addresstypeDTO2.setId(2L);
        assertThat(addresstypeDTO1).isNotEqualTo(addresstypeDTO2);
        addresstypeDTO1.setId(null);
        assertThat(addresstypeDTO1).isNotEqualTo(addresstypeDTO2);
    }
}
