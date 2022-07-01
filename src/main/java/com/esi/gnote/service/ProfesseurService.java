package com.esi.gnote.service;

import com.esi.gnote.service.dto.ProfesseurDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Professeur}.
 */
public interface ProfesseurService {
    /**
     * Save a professeur.
     *
     * @param professeurDTO the entity to save.
     * @return the persisted entity.
     */
    ProfesseurDTO save(ProfesseurDTO professeurDTO);

    /**
     * Updates a professeur.
     *
     * @param professeurDTO the entity to update.
     * @return the persisted entity.
     */
    ProfesseurDTO update(ProfesseurDTO professeurDTO);

    /**
     * Partially updates a professeur.
     *
     * @param professeurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProfesseurDTO> partialUpdate(ProfesseurDTO professeurDTO);

    /**
     * Get all the professeurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProfesseurDTO> findAll(Pageable pageable);

    /**
     * Get all the professeurs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProfesseurDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" professeur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProfesseurDTO> findOne(Long id);

    /**
     * Delete the "id" professeur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
