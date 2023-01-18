package com.atlas.ir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepairshopDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepairshopDTO.class);
        RepairshopDTO repairshopDTO1 = new RepairshopDTO();
        repairshopDTO1.setId(1L);
        RepairshopDTO repairshopDTO2 = new RepairshopDTO();
        assertThat(repairshopDTO1).isNotEqualTo(repairshopDTO2);
        repairshopDTO2.setId(repairshopDTO1.getId());
        assertThat(repairshopDTO1).isEqualTo(repairshopDTO2);
        repairshopDTO2.setId(2L);
        assertThat(repairshopDTO1).isNotEqualTo(repairshopDTO2);
        repairshopDTO1.setId(null);
        assertThat(repairshopDTO1).isNotEqualTo(repairshopDTO2);
    }
}
