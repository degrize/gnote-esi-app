package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlancheDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlancheDTO.class);
        PlancheDTO plancheDTO1 = new PlancheDTO();
        plancheDTO1.setId(1L);
        PlancheDTO plancheDTO2 = new PlancheDTO();
        assertThat(plancheDTO1).isNotEqualTo(plancheDTO2);
        plancheDTO2.setId(plancheDTO1.getId());
        assertThat(plancheDTO1).isEqualTo(plancheDTO2);
        plancheDTO2.setId(2L);
        assertThat(plancheDTO1).isNotEqualTo(plancheDTO2);
        plancheDTO1.setId(null);
        assertThat(plancheDTO1).isNotEqualTo(plancheDTO2);
    }
}
