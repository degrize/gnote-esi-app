package com.esi.gnote.repository;

import com.esi.gnote.domain.Bulletin;
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
public class BulletinRepositoryWithBagRelationshipsImpl implements BulletinRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Bulletin> fetchBagRelationships(Optional<Bulletin> bulletin) {
        return bulletin.map(this::fetchProfesseurs);
    }

    @Override
    public Page<Bulletin> fetchBagRelationships(Page<Bulletin> bulletins) {
        return new PageImpl<>(fetchBagRelationships(bulletins.getContent()), bulletins.getPageable(), bulletins.getTotalElements());
    }

    @Override
    public List<Bulletin> fetchBagRelationships(List<Bulletin> bulletins) {
        return Optional.of(bulletins).map(this::fetchProfesseurs).orElse(Collections.emptyList());
    }

    Bulletin fetchProfesseurs(Bulletin result) {
        return entityManager
            .createQuery(
                "select bulletin from Bulletin bulletin left join fetch bulletin.professeurs where bulletin is :bulletin",
                Bulletin.class
            )
            .setParameter("bulletin", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Bulletin> fetchProfesseurs(List<Bulletin> bulletins) {
        return entityManager
            .createQuery(
                "select distinct bulletin from Bulletin bulletin left join fetch bulletin.professeurs where bulletin in :bulletins",
                Bulletin.class
            )
            .setParameter("bulletins", bulletins)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
