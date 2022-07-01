package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbsenceMapperTest {

    private AbsenceMapper absenceMapper;

    @BeforeEach
    public void setUp() {
        absenceMapper = new AbsenceMapperImpl();
    }
}
