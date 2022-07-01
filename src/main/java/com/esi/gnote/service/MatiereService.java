package com.esi.gnote.service;

import com.esi.gnote.service.dto.MatiereDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Matiere}.
 */
public interface MatiereService {
    /**
     * Save a matiere.
     *
     * @param matiereDTO the entity to save.
     * @return the persisted entity.
     */
    MatiereDTO save(MatiereDTO matiereDTO);

    /**
     * Updates a matiere.
     *
     * @param matiereDTO the entity to update.
     * @return the persisted entity.
     */
    MatiereDTO update(MatiereDTO matiereDTO);

    /**
     * Partially updates a matiere.
     *
     * @param matiereDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MatiereDTO> partialUpdate(MatiereDTO matiereDTO);

    /**
     * Get all the matieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MatiereDTO> findAll(Pageable pageable);

    /**
     * Get the "id" matiere.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MatiereDTO> findOne(Long id);

    /**
     * Delete the "id" matiere.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
