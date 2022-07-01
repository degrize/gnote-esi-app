package com.esi.gnote.repository;

import com.esi.gnote.domain.Encadreur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Encadreur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EncadreurRepository extends JpaRepository<Encadreur, Long> {}
