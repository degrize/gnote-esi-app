package com.esi.gnote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.esi.gnote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecupererBulletinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecupererBulletin.class);
        RecupererBulletin recupererBulletin1 = new RecupererBulletin();
        recupererBulletin1.setId(1L);
        RecupererBulletin recupererBulletin2 = new RecupererBulletin();
        recupererBulletin2.setId(recupererBulletin1.getId());
        assertThat(recupererBulletin1).isEqualTo(recupererBulletin2);
        recupererBulletin2.setId(2L);
        assertThat(recupererBulletin1).isNotEqualTo(recupererBulletin2);
        recupererBulletin1.setId(null);
        assertThat(recupererBulletin1).isNotEqualTo(recupererBulletin2);
    }
}
