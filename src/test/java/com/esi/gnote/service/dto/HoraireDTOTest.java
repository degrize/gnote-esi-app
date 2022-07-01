package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoraireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HoraireDTO.class);
        HoraireDTO horaireDTO1 = new HoraireDTO();
        horaireDTO1.setId(1L);
        HoraireDTO horaireDTO2 = new HoraireDTO();
        assertThat(horaireDTO1).isNotEqualTo(horaireDTO2);
        horaireDTO2.setId(horaireDTO1.getId());
        assertThat(horaireDTO1).isEqualTo(horaireDTO2);
        horaireDTO2.setId(2L);
        assertThat(horaireDTO1).isNotEqualTo(horaireDTO2);
        horaireDTO1.setId(null);
        assertThat(horaireDTO1).isNotEqualTo(horaireDTO2);
    }
}
