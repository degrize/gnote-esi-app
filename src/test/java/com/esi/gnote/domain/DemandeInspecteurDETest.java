package com.esi.gnote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeInspecteurDETest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeInspecteurDE.class);
        DemandeInspecteurDE demandeInspecteurDE1 = new DemandeInspecteurDE();
        demandeInspecteurDE1.setId(1L);
        DemandeInspecteurDE demandeInspecteurDE2 = new DemandeInspecteurDE();
        demandeInspecteurDE2.setId(demandeInspecteurDE1.getId());
        assertThat(demandeInspecteurDE1).isEqualTo(demandeInspecteurDE2);
        demandeInspecteurDE2.setId(2L);
        assertThat(demandeInspecteurDE1).isNotEqualTo(demandeInspecteurDE2);
        demandeInspecteurDE1.setId(null);
        assertThat(demandeInspecteurDE1).isNotEqualTo(demandeInspecteurDE2);
    }
}
