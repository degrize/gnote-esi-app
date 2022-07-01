package com.esi.gnote.repository;

import com.esi.gnote.domain.DemandeInspecteurEtudiant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemandeInspecteurEtudiant entity.
 */
@Repository
public interface DemandeInspecteurEtudiantRepository
    extends DemandeInspecteurEtudiantRepositoryWithBagRelationships, JpaRepository<DemandeInspecteurEtudiant, Long> {
    default Optional<DemandeInspecteurEtudiant> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<DemandeInspecteurEtudiant> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<DemandeInspecteurEtudiant> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
