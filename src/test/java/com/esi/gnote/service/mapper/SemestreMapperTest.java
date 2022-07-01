package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SemestreMapperTest {

    private SemestreMapper semestreMapper;

    @BeforeEach
    public void setUp() {
        semestreMapper = new SemestreMapperImpl();
    }
}
