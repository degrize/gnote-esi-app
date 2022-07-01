package com.esi.gnote.repository;

import com.esi.gnote.domain.Professeur;
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
public class ProfesseurRepositoryWithBagRelationshipsImpl implements ProfesseurRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Professeur> fetchBagRelationships(Optional<Professeur> professeur) {
        return professeur.map(this::fetchEtudiants).map(this::fetchClasses).map(this::fetchMatieres);
    }

    @Override
    public Page<Professeur> fetchBagRelationships(Page<Professeur> professeurs) {
        return new PageImpl<>(fetchBagRelationships(professeurs.getContent()), professeurs.getPageable(), professeurs.getTotalElements());
    }

    @Override
    public List<Professeur> fetchBagRelationships(List<Professeur> professeurs) {
        return Optional
            .of(professeurs)
            .map(this::fetchEtudiants)
            .map(this::fetchClasses)
            .map(this::fetchMatieres)
            .orElse(Collections.emptyList());
    }

    Professeur fetchEtudiants(Professeur result) {
        return entityManager
            .createQuery(
                "select professeur from Professeur professeur left join fetch professeur.etudiants where professeur is :professeur",
                Professeur.class
            )
            .setParameter("professeur", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Professeur> fetchEtudiants(List<Professeur> professeurs) {
        return entityManager
            .createQuery(
                "select distinct professeur from Professeur professeur left join fetch professeur.etudiants where professeur in :professeurs",
                Professeur.class
            )
            .setParameter("professeurs", professeurs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Professeur fetchClasses(Professeur result) {
        return entityManager
            .createQuery(
                "select professeur from Professeur professeur left join fetch professeur.classes where professeur is :professeur",
                Professeur.class
            )
            .setParameter("professeur", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Professeur> fetchClasses(List<Professeur> professeurs) {
        return entityManager
            .createQuery(
                "select distinct professeur from Professeur professeur left join fetch professeur.classes where professeur in :professeurs",
                Professeur.class
            )
            .setParameter("professeurs", professeurs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Professeur fetchMatieres(Professeur result) {
        return entityManager
            .createQuery(
                "select professeur from Professeur professeur left join fetch professeur.matieres where professeur is :professeur",
                Professeur.class
            )
            .setParameter("professeur", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Professeur> fetchMatieres(List<Professeur> professeurs) {
        return entityManager
            .createQuery(
                "select distinct professeur from Professeur professeur left join fetch professeur.matieres where professeur in :professeurs",
                Professeur.class
            )
            .setParameter("professeurs", professeurs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
