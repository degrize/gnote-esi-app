package com.esi.gnote.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DemandeInspecteurEtudiantMapperTest {

    private DemandeInspecteurEtudiantMapper demandeInspecteurEtudiantMapper;

    @BeforeEach
    public void setUp() {
        demandeInspecteurEtudiantMapper = new DemandeInspecteurEtudiantMapperImpl();
    }
}
