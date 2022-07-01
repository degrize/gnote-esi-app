package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JuryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JuryDTO.class);
        JuryDTO juryDTO1 = new JuryDTO();
        juryDTO1.setId(1L);
        JuryDTO juryDTO2 = new JuryDTO();
        assertThat(juryDTO1).isNotEqualTo(juryDTO2);
        juryDTO2.setId(juryDTO1.getId());
        assertThat(juryDTO1).isEqualTo(juryDTO2);
        juryDTO2.setId(2L);
        assertThat(juryDTO1).isNotEqualTo(juryDTO2);
        juryDTO1.setId(null);
        assertThat(juryDTO1).isNotEqualTo(juryDTO2);
    }
}
