package com.esi.gnote.repository;

import com.esi.gnote.domain.Cycle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cycle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {}
