package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfesseurMapperTest {

    private ProfesseurMapper professeurMapper;

    @BeforeEach
    public void setUp() {
        professeurMapper = new ProfesseurMapperImpl();
    }
}
