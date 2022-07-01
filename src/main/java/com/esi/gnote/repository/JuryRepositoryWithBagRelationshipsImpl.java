package com.esi.gnote.repository;

import com.esi.gnote.domain.Jury;
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
public class JuryRepositoryWithBagRelationshipsImpl implements JuryRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Jury> fetchBagRelationships(Optional<Jury> jury) {
        return jury.map(this::fetchProfesseurs);
    }

    @Override
    public Page<Jury> fetchBagRelationships(Page<Jury> juries) {
        return new PageImpl<>(fetchBagRelationships(juries.getContent()), juries.getPageable(), juries.getTotalElements());
    }

    @Override
    public List<Jury> fetchBagRelationships(List<Jury> juries) {
        return Optional.of(juries).map(this::fetchProfesseurs).orElse(Collections.emptyList());
    }

    Jury fetchProfesseurs(Jury result) {
        return entityManager
            .createQuery("select jury from Jury jury left join fetch jury.professeurs where jury is :jury", Jury.class)
            .setParameter("jury", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Jury> fetchProfesseurs(List<Jury> juries) {
        return entityManager
            .createQuery("select distinct jury from Jury jury left join fetch jury.professeurs where jury in :juries", Jury.class)
            .setParameter("juries", juries)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
