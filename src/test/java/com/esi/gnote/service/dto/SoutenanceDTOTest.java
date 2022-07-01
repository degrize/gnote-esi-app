package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoutenanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoutenanceDTO.class);
        SoutenanceDTO soutenanceDTO1 = new SoutenanceDTO();
        soutenanceDTO1.setId(1L);
        SoutenanceDTO soutenanceDTO2 = new SoutenanceDTO();
        assertThat(soutenanceDTO1).isNotEqualTo(soutenanceDTO2);
        soutenanceDTO2.setId(soutenanceDTO1.getId());
        assertThat(soutenanceDTO1).isEqualTo(soutenanceDTO2);
        soutenanceDTO2.setId(2L);
        assertThat(soutenanceDTO1).isNotEqualTo(soutenanceDTO2);
        soutenanceDTO1.setId(null);
        assertThat(soutenanceDTO1).isNotEqualTo(soutenanceDTO2);
    }
}
