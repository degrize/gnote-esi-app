package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DemandeInspecteurDEMapperTest {

    private DemandeInspecteurDEMapper demandeInspecteurDEMapper;

    @BeforeEach
    public void setUp() {
        demandeInspecteurDEMapper = new DemandeInspecteurDEMapperImpl();
    }
}
