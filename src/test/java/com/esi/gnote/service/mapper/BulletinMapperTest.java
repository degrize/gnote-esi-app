package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BulletinMapperTest {

    private BulletinMapper bulletinMapper;

    @BeforeEach
    public void setUp() {
        bulletinMapper = new BulletinMapperImpl();
    }
}
