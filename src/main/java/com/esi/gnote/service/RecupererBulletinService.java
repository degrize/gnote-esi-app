package com.esi.gnote.service;

import com.esi.gnote.service.dto.RecupererBulletinDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.RecupererBulletin}.
 */
public interface RecupererBulletinService {
    /**
     * Save a recupererBulletin.
     *
     * @param recupererBulletinDTO the entity to save.
     * @return the persisted entity.
     */
    RecupererBulletinDTO save(RecupererBulletinDTO recupererBulletinDTO);

    /**
     * Updates a recupererBulletin.
     *
     * @param recupererBulletinDTO the entity to update.
     * @return the persisted entity.
     */
    RecupererBulletinDTO update(RecupererBulletinDTO recupererBulletinDTO);

    /**
     * Partially updates a recupererBulletin.
     *
     * @param recupererBulletinDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecupererBulletinDTO> partialUpdate(RecupererBulletinDTO recupererBulletinDTO);

    /**
     * Get all the recupererBulletins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecupererBulletinDTO> findAll(Pageable pageable);

    /**
     * Get all the recupererBulletins with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecupererBulletinDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" recupererBulletin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecupererBulletinDTO> findOne(Long id);

    /**
     * Delete the "id" recupererBulletin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
