package com.esi.gnote.service;

import com.esi.gnote.service.dto.AnneeScolaireDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.AnneeScolaire}.
 */
public interface AnneeScolaireService {
    /**
     * Save a anneeScolaire.
     *
     * @param anneeScolaireDTO the entity to save.
     * @return the persisted entity.
     */
    AnneeScolaireDTO save(AnneeScolaireDTO anneeScolaireDTO);

    /**
     * Updates a anneeScolaire.
     *
     * @param anneeScolaireDTO the entity to update.
     * @return the persisted entity.
     */
    AnneeScolaireDTO update(AnneeScolaireDTO anneeScolaireDTO);

    /**
     * Partially updates a anneeScolaire.
     *
     * @param anneeScolaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnneeScolaireDTO> partialUpdate(AnneeScolaireDTO anneeScolaireDTO);

    /**
     * Get all the anneeScolaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnneeScolaireDTO> findAll(Pageable pageable);

    /**
     * Get the "id" anneeScolaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnneeScolaireDTO> findOne(Long id);

    /**
     * Delete the "id" anneeScolaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
