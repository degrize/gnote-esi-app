package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlancheMapperTest {

    private PlancheMapper plancheMapper;

    @BeforeEach
    public void setUp() {
        plancheMapper = new PlancheMapperImpl();
    }
}
