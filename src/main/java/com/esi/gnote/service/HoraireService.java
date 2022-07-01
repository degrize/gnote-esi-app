package com.esi.gnote.service;

import com.esi.gnote.service.dto.HoraireDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.esi.gnote.domain.Horaire}.
 */
public interface HoraireService {
    /**
     * Save a horaire.
     *
     * @param horaireDTO the entity to save.
     * @return the persisted entity.
     */
    HoraireDTO save(HoraireDTO horaireDTO);

    /**
     * Updates a horaire.
     *
     * @param horaireDTO the entity to update.
     * @return the persisted entity.
     */
    HoraireDTO update(HoraireDTO horaireDTO);

    /**
     * Partially updates a horaire.
     *
     * @param horaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HoraireDTO> partialUpdate(HoraireDTO horaireDTO);

    /**
     * Get all the horaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HoraireDTO> findAll(Pageable pageable);

    /**
     * Get the "id" horaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HoraireDTO> findOne(Long id);

    /**
     * Delete the "id" horaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
