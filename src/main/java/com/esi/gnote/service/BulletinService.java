package com.esi.gnote.service;

import com.esi.gnote.service.dto.BulletinDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Bulletin}.
 */
public interface BulletinService {
    /**
     * Save a bulletin.
     *
     * @param bulletinDTO the entity to save.
     * @return the persisted entity.
     */
    BulletinDTO save(BulletinDTO bulletinDTO);

    /**
     * Updates a bulletin.
     *
     * @param bulletinDTO the entity to update.
     * @return the persisted entity.
     */
    BulletinDTO update(BulletinDTO bulletinDTO);

    /**
     * Partially updates a bulletin.
     *
     * @param bulletinDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BulletinDTO> partialUpdate(BulletinDTO bulletinDTO);

    /**
     * Get all the bulletins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BulletinDTO> findAll(Pageable pageable);

    /**
     * Get all the bulletins with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BulletinDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" bulletin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BulletinDTO> findOne(Long id);

    /**
     * Delete the "id" bulletin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
