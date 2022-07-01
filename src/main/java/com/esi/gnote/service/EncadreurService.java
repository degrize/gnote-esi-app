package com.esi.gnote.service;

import com.esi.gnote.service.dto.EncadreurDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Encadreur}.
 */
public interface EncadreurService {
    /**
     * Save a encadreur.
     *
     * @param encadreurDTO the entity to save.
     * @return the persisted entity.
     */
    EncadreurDTO save(EncadreurDTO encadreurDTO);

    /**
     * Updates a encadreur.
     *
     * @param encadreurDTO the entity to update.
     * @return the persisted entity.
     */
    EncadreurDTO update(EncadreurDTO encadreurDTO);

    /**
     * Partially updates a encadreur.
     *
     * @param encadreurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EncadreurDTO> partialUpdate(EncadreurDTO encadreurDTO);

    /**
     * Get all the encadreurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EncadreurDTO> findAll(Pageable pageable);

    /**
     * Get the "id" encadreur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EncadreurDTO> findOne(Long id);

    /**
     * Delete the "id" encadreur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
