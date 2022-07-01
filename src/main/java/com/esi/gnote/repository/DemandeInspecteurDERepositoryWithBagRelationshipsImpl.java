package com.esi.gnote.repository;

import com.esi.gnote.domain.DemandeInspecteurDE;
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
public class DemandeInspecteurDERepositoryWithBagRelationshipsImpl implements DemandeInspecteurDERepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<DemandeInspecteurDE> fetchBagRelationships(Optional<DemandeInspecteurDE> demandeInspecteurDE) {
        return demandeInspecteurDE.map(this::fetchProfesseurs).map(this::fetchInspecteurs);
    }

    @Override
    public Page<DemandeInspecteurDE> fetchBagRelationships(Page<DemandeInspecteurDE> demandeInspecteurDES) {
        return new PageImpl<>(
            fetchBagRelationships(demandeInspecteurDES.getContent()),
            demandeInspecteurDES.getPageable(),
            demandeInspecteurDES.getTotalElements()
        );
    }

    @Override
    public List<DemandeInspecteurDE> fetchBagRelationships(List<DemandeInspecteurDE> demandeInspecteurDES) {
        return Optional.of(demandeInspecteurDES).map(this::fetchProfesseurs).map(this::fetchInspecteurs).orElse(Collections.emptyList());
    }

    DemandeInspecteurDE fetchProfesseurs(DemandeInspecteurDE result) {
        return entityManager
            .createQuery(
                "select demandeInspecteurDE from DemandeInspecteurDE demandeInspecteurDE left join fetch demandeInspecteurDE.professeurs where demandeInspecteurDE is :demandeInspecteurDE",
                DemandeInspecteurDE.class
            )
            .setParameter("demandeInspecteurDE", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<DemandeInspecteurDE> fetchProfesseurs(List<DemandeInspecteurDE> demandeInspecteurDES) {
        return entityManager
            .createQuery(
                "select distinct demandeInspecteurDE from DemandeInspecteurDE demandeInspecteurDE left join fetch demandeInspecteurDE.professeurs where demandeInspecteurDE in :demandeInspecteurDES",
                DemandeInspecteurDE.class
            )
            .setParameter("demandeInspecteurDES", demandeInspecteurDES)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    DemandeInspecteurDE fetchInspecteurs(DemandeInspecteurDE result) {
        return entityManager
            .createQuery(
                "select demandeInspecteurDE from DemandeInspecteurDE demandeInspecteurDE left join fetch demandeInspecteurDE.inspecteurs where demandeInspecteurDE is :demandeInspecteurDE",
                DemandeInspecteurDE.class
            )
            .setParameter("demandeInspecteurDE", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<DemandeInspecteurDE> fetchInspecteurs(List<DemandeInspecteurDE> demandeInspecteurDES) {
        return entityManager
            .createQuery(
                "select distinct demandeInspecteurDE from DemandeInspecteurDE demandeInspecteurDE left join fetch demandeInspecteurDE.inspecteurs where demandeInspecteurDE in :demandeInspecteurDES",
                DemandeInspecteurDE.class
            )
            .setParameter("demandeInspecteurDES", demandeInspecteurDES)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
