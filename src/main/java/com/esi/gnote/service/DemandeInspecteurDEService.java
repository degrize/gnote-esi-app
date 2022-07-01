package com.esi.gnote.service;

import com.esi.gnote.service.dto.DemandeInspecteurDEDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.DemandeInspecteurDE}.
 */
public interface DemandeInspecteurDEService {
    /**
     * Save a demandeInspecteurDE.
     *
     * @param demandeInspecteurDEDTO the entity to save.
     * @return the persisted entity.
     */
    DemandeInspecteurDEDTO save(DemandeInspecteurDEDTO demandeInspecteurDEDTO);

    /**
     * Updates a demandeInspecteurDE.
     *
     * @param demandeInspecteurDEDTO the entity to update.
     * @return the persisted entity.
     */
    DemandeInspecteurDEDTO update(DemandeInspecteurDEDTO demandeInspecteurDEDTO);

    /**
     * Partially updates a demandeInspecteurDE.
     *
     * @param demandeInspecteurDEDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemandeInspecteurDEDTO> partialUpdate(DemandeInspecteurDEDTO demandeInspecteurDEDTO);

    /**
     * Get all the demandeInspecteurDES.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeInspecteurDEDTO> findAll(Pageable pageable);

    /**
     * Get all the demandeInspecteurDES with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeInspecteurDEDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" demandeInspecteurDE.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemandeInspecteurDEDTO> findOne(Long id);

    /**
     * Delete the "id" demandeInspecteurDE.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
