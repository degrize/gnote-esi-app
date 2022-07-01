package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeInspecteurEtudiantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeInspecteurEtudiantDTO.class);
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO1 = new DemandeInspecteurEtudiantDTO();
        demandeInspecteurEtudiantDTO1.setId(1L);
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO2 = new DemandeInspecteurEtudiantDTO();
        assertThat(demandeInspecteurEtudiantDTO1).isNotEqualTo(demandeInspecteurEtudiantDTO2);
        demandeInspecteurEtudiantDTO2.setId(demandeInspecteurEtudiantDTO1.getId());
        assertThat(demandeInspecteurEtudiantDTO1).isEqualTo(demandeInspecteurEtudiantDTO2);
        demandeInspecteurEtudiantDTO2.setId(2L);
        assertThat(demandeInspecteurEtudiantDTO1).isNotEqualTo(demandeInspecteurEtudiantDTO2);
        demandeInspecteurEtudiantDTO1.setId(null);
        assertThat(demandeInspecteurEtudiantDTO1).isNotEqualTo(demandeInspecteurEtudiantDTO2);
    }
}
