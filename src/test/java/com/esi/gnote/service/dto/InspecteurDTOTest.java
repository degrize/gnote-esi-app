package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspecteurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspecteurDTO.class);
        InspecteurDTO inspecteurDTO1 = new InspecteurDTO();
        inspecteurDTO1.setId(1L);
        InspecteurDTO inspecteurDTO2 = new InspecteurDTO();
        assertThat(inspecteurDTO1).isNotEqualTo(inspecteurDTO2);
        inspecteurDTO2.setId(inspecteurDTO1.getId());
        assertThat(inspecteurDTO1).isEqualTo(inspecteurDTO2);
        inspecteurDTO2.setId(2L);
        assertThat(inspecteurDTO1).isNotEqualTo(inspecteurDTO2);
        inspecteurDTO1.setId(null);
        assertThat(inspecteurDTO1).isNotEqualTo(inspecteurDTO2);
    }
}
