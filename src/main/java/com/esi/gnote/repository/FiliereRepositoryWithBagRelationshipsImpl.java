package com.esi.gnote.repository;

import com.esi.gnote.domain.Filiere;
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
public class FiliereRepositoryWithBagRelationshipsImpl implements FiliereRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Filiere> fetchBagRelationships(Optional<Filiere> filiere) {
        return filiere.map(this::fetchModules);
    }

    @Override
    public Page<Filiere> fetchBagRelationships(Page<Filiere> filieres) {
        return new PageImpl<>(fetchBagRelationships(filieres.getContent()), filieres.getPageable(), filieres.getTotalElements());
    }

    @Override
    public List<Filiere> fetchBagRelationships(List<Filiere> filieres) {
        return Optional.of(filieres).map(this::fetchModules).orElse(Collections.emptyList());
    }

    Filiere fetchModules(Filiere result) {
        return entityManager
            .createQuery("select filiere from Filiere filiere left join fetch filiere.modules where filiere is :filiere", Filiere.class)
            .setParameter("filiere", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Filiere> fetchModules(List<Filiere> filieres) {
        return entityManager
            .createQuery(
                "select distinct filiere from Filiere filiere left join fetch filiere.modules where filiere in :filieres",
                Filiere.class
            )
            .setParameter("filieres", filieres)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
