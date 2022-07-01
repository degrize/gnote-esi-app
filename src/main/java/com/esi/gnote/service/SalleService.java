package com.esi.gnote.service;

import com.esi.gnote.service.dto.SalleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Salle}.
 */
public interface SalleService {
    /**
     * Save a salle.
     *
     * @param salleDTO the entity to save.
     * @return the persisted entity.
     */
    SalleDTO save(SalleDTO salleDTO);

    /**
     * Updates a salle.
     *
     * @param salleDTO the entity to update.
     * @return the persisted entity.
     */
    SalleDTO update(SalleDTO salleDTO);

    /**
     * Partially updates a salle.
     *
     * @param salleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SalleDTO> partialUpdate(SalleDTO salleDTO);

    /**
     * Get all the salles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SalleDTO> findAll(Pageable pageable);

    /**
     * Get all the salles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SalleDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" salle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SalleDTO> findOne(Long id);

    /**
     * Delete the "id" salle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
