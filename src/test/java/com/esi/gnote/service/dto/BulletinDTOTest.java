package com.esi.gnote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BulletinDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BulletinDTO.class);
        BulletinDTO bulletinDTO1 = new BulletinDTO();
        bulletinDTO1.setId(1L);
        BulletinDTO bulletinDTO2 = new BulletinDTO();
        assertThat(bulletinDTO1).isNotEqualTo(bulletinDTO2);
        bulletinDTO2.setId(bulletinDTO1.getId());
        assertThat(bulletinDTO1).isEqualTo(bulletinDTO2);
        bulletinDTO2.setId(2L);
        assertThat(bulletinDTO1).isNotEqualTo(bulletinDTO2);
        bulletinDTO1.setId(null);
        assertThat(bulletinDTO1).isNotEqualTo(bulletinDTO2);
    }
}
