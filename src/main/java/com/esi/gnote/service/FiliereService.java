package com.esi.gnote.service;

import com.esi.gnote.service.dto.FiliereDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Filiere}.
 */
public interface FiliereService {
    /**
     * Save a filiere.
     *
     * @param filiereDTO the entity to save.
     * @return the persisted entity.
     */
    FiliereDTO save(FiliereDTO filiereDTO);

    /**
     * Updates a filiere.
     *
     * @param filiereDTO the entity to update.
     * @return the persisted entity.
     */
    FiliereDTO update(FiliereDTO filiereDTO);

    /**
     * Partially updates a filiere.
     *
     * @param filiereDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FiliereDTO> partialUpdate(FiliereDTO filiereDTO);

    /**
     * Get all the filieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiliereDTO> findAll(Pageable pageable);

    /**
     * Get all the filieres with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiliereDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" filiere.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FiliereDTO> findOne(Long id);

    /**
     * Delete the "id" filiere.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
