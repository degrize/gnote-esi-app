package com.esi.gnote.repository;

import com.esi.gnote.domain.Horaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Horaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HoraireRepository extends JpaRepository<Horaire, Long> {}
