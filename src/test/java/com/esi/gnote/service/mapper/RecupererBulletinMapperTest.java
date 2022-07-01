package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecupererBulletinMapperTest {

    private RecupererBulletinMapper recupererBulletinMapper;

    @BeforeEach
    public void setUp() {
        recupererBulletinMapper = new RecupererBulletinMapperImpl();
    }
}
