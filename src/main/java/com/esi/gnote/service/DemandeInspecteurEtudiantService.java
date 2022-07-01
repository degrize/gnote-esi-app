package com.esi.gnote.service;

import com.esi.gnote.service.dto.DemandeInspecteurEtudiantDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.DemandeInspecteurEtudiant}.
 */
public interface DemandeInspecteurEtudiantService {
    /**
     * Save a demandeInspecteurEtudiant.
     *
     * @param demandeInspecteurEtudiantDTO the entity to save.
     * @return the persisted entity.
     */
    DemandeInspecteurEtudiantDTO save(DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO);

    /**
     * Updates a demandeInspecteurEtudiant.
     *
     * @param demandeInspecteurEtudiantDTO the entity to update.
     * @return the persisted entity.
     */
    DemandeInspecteurEtudiantDTO update(DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO);

    /**
     * Partially updates a demandeInspecteurEtudiant.
     *
     * @param demandeInspecteurEtudiantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemandeInspecteurEtudiantDTO> partialUpdate(DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO);

    /**
     * Get all the demandeInspecteurEtudiants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeInspecteurEtudiantDTO> findAll(Pageable pageable);

    /**
     * Get all the demandeInspecteurEtudiants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeInspecteurEtudiantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" demandeInspecteurEtudiant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemandeInspecteurEtudiantDTO> findOne(Long id);

    /**
     * Delete the "id" demandeInspecteurEtudiant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
