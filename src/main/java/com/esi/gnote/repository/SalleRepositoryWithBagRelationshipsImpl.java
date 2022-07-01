package com.esi.gnote.repository;

import com.esi.gnote.domain.Salle;
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
public class SalleRepositoryWithBagRelationshipsImpl implements SalleRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Salle> fetchBagRelationships(Optional<Salle> salle) {
        return salle.map(this::fetchHoraires);
    }

    @Override
    public Page<Salle> fetchBagRelationships(Page<Salle> salles) {
        return new PageImpl<>(fetchBagRelationships(salles.getContent()), salles.getPageable(), salles.getTotalElements());
    }

    @Override
    public List<Salle> fetchBagRelationships(List<Salle> salles) {
        return Optional.of(salles).map(this::fetchHoraires).orElse(Collections.emptyList());
    }

    Salle fetchHoraires(Salle result) {
        return entityManager
            .createQuery("select salle from Salle salle left join fetch salle.horaires where salle is :salle", Salle.class)
            .setParameter("salle", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Salle> fetchHoraires(List<Salle> salles) {
        return entityManager
            .createQuery("select distinct salle from Salle salle left join fetch salle.horaires where salle in :salles", Salle.class)
            .setParameter("salles", salles)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
