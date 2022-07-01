package com.esi.gnote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EncadreurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Encadreur.class);
        Encadreur encadreur1 = new Encadreur();
        encadreur1.setId(1L);
        Encadreur encadreur2 = new Encadreur();
        encadreur2.setId(encadreur1.getId());
        assertThat(encadreur1).isEqualTo(encadreur2);
        encadreur2.setId(2L);
        assertThat(encadreur1).isNotEqualTo(encadreur2);
        encadreur1.setId(null);
        assertThat(encadreur1).isNotEqualTo(encadreur2);
    }
}
