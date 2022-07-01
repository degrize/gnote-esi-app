package com.esi.gnote.repository;

import com.esi.gnote.domain.Module;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Module entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {}
