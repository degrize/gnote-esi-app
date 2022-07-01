package com.esi.gnote.service;

import com.esi.gnote.service.dto.InspecteurDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Inspecteur}.
 */
public interface InspecteurService {
    /**
     * Save a inspecteur.
     *
     * @param inspecteurDTO the entity to save.
     * @return the persisted entity.
     */
    InspecteurDTO save(InspecteurDTO inspecteurDTO);

    /**
     * Updates a inspecteur.
     *
     * @param inspecteurDTO the entity to update.
     * @return the persisted entity.
     */
    InspecteurDTO update(InspecteurDTO inspecteurDTO);

    /**
     * Partially updates a inspecteur.
     *
     * @param inspecteurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspecteurDTO> partialUpdate(InspecteurDTO inspecteurDTO);

    /**
     * Get all the inspecteurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InspecteurDTO> findAll(Pageable pageable);

    /**
     * Get all the inspecteurs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InspecteurDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" inspecteur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspecteurDTO> findOne(Long id);

    /**
     * Delete the "id" inspecteur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
