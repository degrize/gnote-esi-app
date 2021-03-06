package com.esi.gnote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspecteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inspecteur.class);
        Inspecteur inspecteur1 = new Inspecteur();
        inspecteur1.setId(1L);
        Inspecteur inspecteur2 = new Inspecteur();
        inspecteur2.setId(inspecteur1.getId());
        assertThat(inspecteur1).isEqualTo(inspecteur2);
        inspecteur2.setId(2L);
        assertThat(inspecteur1).isNotEqualTo(inspecteur2);
        inspecteur1.setId(null);
        assertThat(inspecteur1).isNotEqualTo(inspecteur2);
    }
}
