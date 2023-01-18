package com.atlas.ir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.atlas.ir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddresstypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Addresstype.class);
        Addresstype addresstype1 = new Addresstype();
        addresstype1.setId(1L);
        Addresstype addresstype2 = new Addresstype();
        addresstype2.setId(addresstype1.getId());
        assertThat(addresstype1).isEqualTo(addresstype2);
        addresstype2.setId(2L);
        assertThat(addresstype1).isNotEqualTo(addresstype2);
        addresstype1.setId(null);
        assertThat(addresstype1).isNotEqualTo(addresstype2);
    }
}
