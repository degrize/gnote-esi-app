package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatiereMapperTest {

    private MatiereMapper matiereMapper;

    @BeforeEach
    public void setUp() {
        matiereMapper = new MatiereMapperImpl();
    }
}
