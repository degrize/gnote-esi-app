package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EncadreurMapperTest {

    private EncadreurMapper encadreurMapper;

    @BeforeEach
    public void setUp() {
        encadreurMapper = new EncadreurMapperImpl();
    }
}
