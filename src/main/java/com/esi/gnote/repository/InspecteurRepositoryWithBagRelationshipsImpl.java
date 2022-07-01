package com.esi.gnote.repository;

import com.esi.gnote.domain.Inspecteur;
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
public class InspecteurRepositoryWithBagRelationshipsImpl implements InspecteurRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Inspecteur> fetchBagRelationships(Optional<Inspecteur> inspecteur) {
        return inspecteur.map(this::fetchProfesseurs).map(this::fetchEtudiants);
    }

    @Override
    public Page<Inspecteur> fetchBagRelationships(Page<Inspecteur> inspecteurs) {
        return new PageImpl<>(fetchBagRelationships(inspecteurs.getContent()), inspecteurs.getPageable(), inspecteurs.getTotalElements());
    }

    @Override
    public List<Inspecteur> fetchBagRelationships(List<Inspecteur> inspecteurs) {
        return Optional.of(inspecteurs).map(this::fetchProfesseurs).map(this::fetchEtudiants).orElse(Collections.emptyList());
    }

    Inspecteur fetchProfesseurs(Inspecteur result) {
        return entityManager
            .createQuery(
                "select inspecteur from Inspecteur inspecteur left join fetch inspecteur.professeurs where inspecteur is :inspecteur",
                Inspecteur.class
            )
            .setParameter("inspecteur", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Inspecteur> fetchProfesseurs(List<Inspecteur> inspecteurs) {
        return entityManager
            .createQuery(
                "select distinct inspecteur from Inspecteur inspecteur left join fetch inspecteur.professeurs where inspecteur in :inspecteurs",
                Inspecteur.class
            )
            .setParameter("inspecteurs", inspecteurs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Inspecteur fetchEtudiants(Inspecteur result) {
        return entityManager
            .createQuery(
                "select inspecteur from Inspecteur inspecteur left join fetch inspecteur.etudiants where inspecteur is :inspecteur",
                Inspecteur.class
            )
            .setParameter("inspecteur", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Inspecteur> fetchEtudiants(List<Inspecteur> inspecteurs) {
        return entityManager
            .createQuery(
                "select distinct inspecteur from Inspecteur inspecteur left join fetch inspecteur.etudiants where inspecteur in :inspecteurs",
                Inspecteur.class
            )
            .setParameter("inspecteurs", inspecteurs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
