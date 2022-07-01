package com.esi.gnote.service;

import com.esi.gnote.service.dto.PlancheDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Planche}.
 */
public interface PlancheService {
    /**
     * Save a planche.
     *
     * @param plancheDTO the entity to save.
     * @return the persisted entity.
     */
    PlancheDTO save(PlancheDTO plancheDTO);

    /**
     * Updates a planche.
     *
     * @param plancheDTO the entity to update.
     * @return the persisted entity.
     */
    PlancheDTO update(PlancheDTO plancheDTO);

    /**
     * Partially updates a planche.
     *
     * @param plancheDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlancheDTO> partialUpdate(PlancheDTO plancheDTO);

    /**
     * Get all the planches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlancheDTO> findAll(Pageable pageable);

    /**
     * Get all the planches with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlancheDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" planche.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlancheDTO> findOne(Long id);

    /**
     * Delete the "id" planche.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
