package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EncadreurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EncadreurDTO.class);
        EncadreurDTO encadreurDTO1 = new EncadreurDTO();
        encadreurDTO1.setId(1L);
        EncadreurDTO encadreurDTO2 = new EncadreurDTO();
        assertThat(encadreurDTO1).isNotEqualTo(encadreurDTO2);
        encadreurDTO2.setId(encadreurDTO1.getId());
        assertThat(encadreurDTO1).isEqualTo(encadreurDTO2);
        encadreurDTO2.setId(2L);
        assertThat(encadreurDTO1).isNotEqualTo(encadreurDTO2);
        encadreurDTO1.setId(null);
        assertThat(encadreurDTO1).isNotEqualTo(encadreurDTO2);
    }
}
