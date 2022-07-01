package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecupererBulletinDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecupererBulletinDTO.class);
        RecupererBulletinDTO recupererBulletinDTO1 = new RecupererBulletinDTO();
        recupererBulletinDTO1.setId(1L);
        RecupererBulletinDTO recupererBulletinDTO2 = new RecupererBulletinDTO();
        assertThat(recupererBulletinDTO1).isNotEqualTo(recupererBulletinDTO2);
        recupererBulletinDTO2.setId(recupererBulletinDTO1.getId());
        assertThat(recupererBulletinDTO1).isEqualTo(recupererBulletinDTO2);
        recupererBulletinDTO2.setId(2L);
        assertThat(recupererBulletinDTO1).isNotEqualTo(recupererBulletinDTO2);
        recupererBulletinDTO1.setId(null);
        assertThat(recupererBulletinDTO1).isNotEqualTo(recupererBulletinDTO2);
    }
}
