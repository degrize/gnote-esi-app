package com.esi.gnote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlancheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planche.class);
        Planche planche1 = new Planche();
        planche1.setId(1L);
        Planche planche2 = new Planche();
        planche2.setId(planche1.getId());
        assertThat(planche1).isEqualTo(planche2);
        planche2.setId(2L);
        assertThat(planche1).isNotEqualTo(planche2);
        planche1.setId(null);
        assertThat(planche1).isNotEqualTo(planche2);
    }
}
