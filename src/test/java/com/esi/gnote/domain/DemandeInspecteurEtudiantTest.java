package com.esi.gnote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeInspecteurEtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeInspecteurEtudiant.class);
        DemandeInspecteurEtudiant demandeInspecteurEtudiant1 = new DemandeInspecteurEtudiant();
        demandeInspecteurEtudiant1.setId(1L);
        DemandeInspecteurEtudiant demandeInspecteurEtudiant2 = new DemandeInspecteurEtudiant();
        demandeInspecteurEtudiant2.setId(demandeInspecteurEtudiant1.getId());
        assertThat(demandeInspecteurEtudiant1).isEqualTo(demandeInspecteurEtudiant2);
        demandeInspecteurEtudiant2.setId(2L);
        assertThat(demandeInspecteurEtudiant1).isNotEqualTo(demandeInspecteurEtudiant2);
        demandeInspecteurEtudiant1.setId(null);
        assertThat(demandeInspecteurEtudiant1).isNotEqualTo(demandeInspecteurEtudiant2);
    }
}
