package com.esi.gnote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoutenanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Soutenance.class);
        Soutenance soutenance1 = new Soutenance();
        soutenance1.setId(1L);
        Soutenance soutenance2 = new Soutenance();
        soutenance2.setId(soutenance1.getId());
        assertThat(soutenance1).isEqualTo(soutenance2);
        soutenance2.setId(2L);
        assertThat(soutenance1).isNotEqualTo(soutenance2);
        soutenance1.setId(null);
        assertThat(soutenance1).isNotEqualTo(soutenance2);
    }
}
