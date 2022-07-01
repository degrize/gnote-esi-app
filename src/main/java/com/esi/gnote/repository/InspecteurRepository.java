package com.esi.gnote.repository;

import com.esi.gnote.domain.Inspecteur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Inspecteur entity.
 */
@Repository
public interface InspecteurRepository extends InspecteurRepositoryWithBagRelationships, JpaRepository<Inspecteur, Long> {
    default Optional<Inspecteur> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Inspecteur> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Inspecteur> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
