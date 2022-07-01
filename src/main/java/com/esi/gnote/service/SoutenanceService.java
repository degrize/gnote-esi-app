package com.esi.gnote.service;

import com.esi.gnote.service.dto.SoutenanceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Soutenance}.
 */
public interface SoutenanceService {
    /**
     * Save a soutenance.
     *
     * @param soutenanceDTO the entity to save.
     * @return the persisted entity.
     */
    SoutenanceDTO save(SoutenanceDTO soutenanceDTO);

    /**
     * Updates a soutenance.
     *
     * @param soutenanceDTO the entity to update.
     * @return the persisted entity.
     */
    SoutenanceDTO update(SoutenanceDTO soutenanceDTO);

    /**
     * Partially updates a soutenance.
     *
     * @param soutenanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SoutenanceDTO> partialUpdate(SoutenanceDTO soutenanceDTO);

    /**
     * Get all the soutenances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SoutenanceDTO> findAll(Pageable pageable);

    /**
     * Get all the soutenances with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SoutenanceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" soutenance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SoutenanceDTO> findOne(Long id);

    /**
     * Delete the "id" soutenance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
