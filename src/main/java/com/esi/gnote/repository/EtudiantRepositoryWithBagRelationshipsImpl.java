package com.esi.gnote.repository;

import com.esi.gnote.domain.Etudiant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EtudiantRepositoryWithBagRelationshipsImpl implements EtudiantRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Etudiant> fetchBagRelationships(Optional<Etudiant> etudiant) {
        return etudiant.map(this::fetchClasses);
    }

    @Override
    public Page<Etudiant> fetchBagRelationships(Page<Etudiant> etudiants) {
        return new PageImpl<>(fetchBagRelationships(etudiants.getContent()), etudiants.getPageable(), etudiants.getTotalElements());
    }

    @Override
    public List<Etudiant> fetchBagRelationships(List<Etudiant> etudiants) {
        return Optional.of(etudiants).map(this::fetchClasses).orElse(Collections.emptyList());
    }

    Etudiant fetchClasses(Etudiant result) {
        return entityManager
            .createQuery(
                "select etudiant from Etudiant etudiant left join fetch etudiant.classes where etudiant is :etudiant",
                Etudiant.class
            )
            .setParameter("etudiant", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Etudiant> fetchClasses(List<Etudiant> etudiants) {
        return entityManager
            .createQuery(
                "select distinct etudiant from Etudiant etudiant left join fetch etudiant.classes where etudiant in :etudiants",
                Etudiant.class
            )
            .setParameter("etudiants", etudiants)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
