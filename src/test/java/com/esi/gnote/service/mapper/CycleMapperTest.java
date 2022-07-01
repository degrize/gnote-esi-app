package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CycleMapperTest {

    private CycleMapper cycleMapper;

    @BeforeEach
    public void setUp() {
        cycleMapper = new CycleMapperImpl();
    }
}
