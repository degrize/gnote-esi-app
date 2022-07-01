package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HoraireMapperTest {

    private HoraireMapper horaireMapper;

    @BeforeEach
    public void setUp() {
        horaireMapper = new HoraireMapperImpl();
    }
}
