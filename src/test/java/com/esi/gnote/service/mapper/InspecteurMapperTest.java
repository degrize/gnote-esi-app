package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InspecteurMapperTest {

    private InspecteurMapper inspecteurMapper;

    @BeforeEach
    public void setUp() {
        inspecteurMapper = new InspecteurMapperImpl();
    }
}
