package com.esi.gnote.repository;

import com.esi.gnote.domain.Soutenance;
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
public class SoutenanceRepositoryWithBagRelationshipsImpl implements SoutenanceRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Soutenance> fetchBagRelationships(Optional<Soutenance> soutenance) {
        return soutenance.map(this::fetchEtudiants);
    }

    @Override
    public Page<Soutenance> fetchBagRelationships(Page<Soutenance> soutenances) {
        return new PageImpl<>(fetchBagRelationships(soutenances.getContent()), soutenances.getPageable(), soutenances.getTotalElements());
    }

    @Override
    public List<Soutenance> fetchBagRelationships(List<Soutenance> soutenances) {
        return Optional.of(soutenances).map(this::fetchEtudiants).orElse(Collections.emptyList());
    }

    Soutenance fetchEtudiants(Soutenance result) {
        return entityManager
            .createQuery(
                "select soutenance from Soutenance soutenance left join fetch soutenance.etudiants where soutenance is :soutenance",
                Soutenance.class
            )
            .setParameter("soutenance", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Soutenance> fetchEtudiants(List<Soutenance> soutenances) {
        return entityManager
            .createQuery(
                "select distinct soutenance from Soutenance soutenance left join fetch soutenance.etudiants where soutenance in :soutenances",
                Soutenance.class
            )
            .setParameter("soutenances", soutenances)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
