package com.esi.gnote.service;

import com.esi.gnote.service.dto.ModuleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Module}.
 */
public interface ModuleService {
    /**
     * Save a module.
     *
     * @param moduleDTO the entity to save.
     * @return the persisted entity.
     */
    ModuleDTO save(ModuleDTO moduleDTO);

    /**
     * Updates a module.
     *
     * @param moduleDTO the entity to update.
     * @return the persisted entity.
     */
    ModuleDTO update(ModuleDTO moduleDTO);

    /**
     * Partially updates a module.
     *
     * @param moduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ModuleDTO> partialUpdate(ModuleDTO moduleDTO);

    /**
     * Get all the modules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ModuleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" module.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ModuleDTO> findOne(Long id);

    /**
     * Delete the "id" module.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
