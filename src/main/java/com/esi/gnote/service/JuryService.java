package com.esi.gnote.service;

import com.esi.gnote.service.dto.JuryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Jury}.
 */
public interface JuryService {
    /**
     * Save a jury.
     *
     * @param juryDTO the entity to save.
     * @return the persisted entity.
     */
    JuryDTO save(JuryDTO juryDTO);

    /**
     * Updates a jury.
     *
     * @param juryDTO the entity to update.
     * @return the persisted entity.
     */
    JuryDTO update(JuryDTO juryDTO);

    /**
     * Partially updates a jury.
     *
     * @param juryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JuryDTO> partialUpdate(JuryDTO juryDTO);

    /**
     * Get all the juries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JuryDTO> findAll(Pageable pageable);

    /**
     * Get all the juries with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JuryDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" jury.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JuryDTO> findOne(Long id);

    /**
     * Delete the "id" jury.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
