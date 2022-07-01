package com.esi.gnote.repository;

import com.esi.gnote.domain.DemandeInspecteurEtudiant;
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
public class DemandeInspecteurEtudiantRepositoryWithBagRelationshipsImpl
    implements DemandeInspecteurEtudiantRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<DemandeInspecteurEtudiant> fetchBagRelationships(Optional<DemandeInspecteurEtudiant> demandeInspecteurEtudiant) {
        return demandeInspecteurEtudiant.map(this::fetchEtudiants).map(this::fetchInspecteurs);
    }

    @Override
    public Page<DemandeInspecteurEtudiant> fetchBagRelationships(Page<DemandeInspecteurEtudiant> demandeInspecteurEtudiants) {
        return new PageImpl<>(
            fetchBagRelationships(demandeInspecteurEtudiants.getContent()),
            demandeInspecteurEtudiants.getPageable(),
            demandeInspecteurEtudiants.getTotalElements()
        );
    }

    @Override
    public List<DemandeInspecteurEtudiant> fetchBagRelationships(List<DemandeInspecteurEtudiant> demandeInspecteurEtudiants) {
        return Optional
            .of(demandeInspecteurEtudiants)
            .map(this::fetchEtudiants)
            .map(this::fetchInspecteurs)
            .orElse(Collections.emptyList());
    }

    DemandeInspecteurEtudiant fetchEtudiants(DemandeInspecteurEtudiant result) {
        return entityManager
            .createQuery(
                "select demandeInspecteurEtudiant from DemandeInspecteurEtudiant demandeInspecteurEtudiant left join fetch demandeInspecteurEtudiant.etudiants where demandeInspecteurEtudiant is :demandeInspecteurEtudiant",
                DemandeInspecteurEtudiant.class
            )
            .setParameter("demandeInspecteurEtudiant", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<DemandeInspecteurEtudiant> fetchEtudiants(List<DemandeInspecteurEtudiant> demandeInspecteurEtudiants) {
        return entityManager
            .createQuery(
                "select distinct demandeInspecteurEtudiant from DemandeInspecteurEtudiant demandeInspecteurEtudiant left join fetch demandeInspecteurEtudiant.etudiants where demandeInspecteurEtudiant in :demandeInspecteurEtudiants",
                DemandeInspecteurEtudiant.class
            )
            .setParameter("demandeInspecteurEtudiants", demandeInspecteurEtudiants)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    DemandeInspecteurEtudiant fetchInspecteurs(DemandeInspecteurEtudiant result) {
        return entityManager
            .createQuery(
                "select demandeInspecteurEtudiant from DemandeInspecteurEtudiant demandeInspecteurEtudiant left join fetch demandeInspecteurEtudiant.inspecteurs where demandeInspecteurEtudiant is :demandeInspecteurEtudiant",
                DemandeInspecteurEtudiant.class
            )
            .setParameter("demandeInspecteurEtudiant", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<DemandeInspecteurEtudiant> fetchInspecteurs(List<DemandeInspecteurEtudiant> demandeInspecteurEtudiants) {
        return entityManager
            .createQuery(
                "select distinct demandeInspecteurEtudiant from DemandeInspecteurEtudiant demandeInspecteurEtudiant left join fetch demandeInspecteurEtudiant.inspecteurs where demandeInspecteurEtudiant in :demandeInspecteurEtudiants",
                DemandeInspecteurEtudiant.class
            )
            .setParameter("demandeInspecteurEtudiants", demandeInspecteurEtudiants)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
