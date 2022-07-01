package com.esi.gnote.repository;

import com.esi.gnote.domain.DemandeInspecteurDE;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemandeInspecteurDE entity.
 */
@Repository
public interface DemandeInspecteurDERepository
    extends DemandeInspecteurDERepositoryWithBagRelationships, JpaRepository<DemandeInspecteurDE, Long> {
    default Optional<DemandeInspecteurDE> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<DemandeInspecteurDE> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<DemandeInspecteurDE> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
