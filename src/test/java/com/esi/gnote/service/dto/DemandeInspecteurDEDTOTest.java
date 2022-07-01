package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeInspecteurDEDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeInspecteurDEDTO.class);
        DemandeInspecteurDEDTO demandeInspecteurDEDTO1 = new DemandeInspecteurDEDTO();
        demandeInspecteurDEDTO1.setId(1L);
        DemandeInspecteurDEDTO demandeInspecteurDEDTO2 = new DemandeInspecteurDEDTO();
        assertThat(demandeInspecteurDEDTO1).isNotEqualTo(demandeInspecteurDEDTO2);
        demandeInspecteurDEDTO2.setId(demandeInspecteurDEDTO1.getId());
        assertThat(demandeInspecteurDEDTO1).isEqualTo(demandeInspecteurDEDTO2);
        demandeInspecteurDEDTO2.setId(2L);
        assertThat(demandeInspecteurDEDTO1).isNotEqualTo(demandeInspecteurDEDTO2);
        demandeInspecteurDEDTO1.setId(null);
        assertThat(demandeInspecteurDEDTO1).isNotEqualTo(demandeInspecteurDEDTO2);
    }
}
