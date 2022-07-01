package com.esi.gnote.service.impl;

import com.esi.gnote.domain.AnneeScolaire;
import com.esi.gnote.repository.AnneeScolaireRepository;
import com.esi.gnote.service.AnneeScolaireService;
import com.esi.gnote.service.dto.AnneeScolaireDTO;
import com.esi.gnote.service.mapper.AnneeScolaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AnneeScolaire}.
 */
@Service
@Transactional
public class AnneeScolaireServiceImpl implements AnneeScolaireService {

    private final Logger log = LoggerFactory.getLogger(AnneeScolaireServiceImpl.class);

    private final AnneeScolaireRepository anneeScolaireRepository;

    private final AnneeScolaireMapper anneeScolaireMapper;

    public AnneeScolaireServiceImpl(AnneeScolaireRepository anneeScolaireRepository, AnneeScolaireMapper anneeScolaireMapper) {
        this.anneeScolaireRepository = anneeScolaireRepository;
        this.anneeScolaireMapper = anneeScolaireMapper;
    }

    @Override
    public AnneeScolaireDTO save(AnneeScolaireDTO anneeScolaireDTO) {
        log.debug("Request to save AnneeScolaire : {}", anneeScolaireDTO);
        AnneeScolaire anneeScolaire = anneeScolaireMapper.toEntity(anneeScolaireDTO);
        anneeScolaire = anneeScolaireRepository.save(anneeScolaire);
        return anneeScolaireMapper.toDto(anneeScolaire);
    }

    @Override
    public AnneeScolaireDTO update(AnneeScolaireDTO anneeScolaireDTO) {
        log.debug("Request to save AnneeScolaire : {}", anneeScolaireDTO);
        AnneeScolaire anneeScolaire = anneeScolaireMapper.toEntity(anneeScolaireDTO);
        anneeScolaire = anneeScolaireRepository.save(anneeScolaire);
        return anneeScolaireMapper.toDto(anneeScolaire);
    }

    @Override
    public Optional<AnneeScolaireDTO> partialUpdate(AnneeScolaireDTO anneeScolaireDTO) {
        log.debug("Request to partially update AnneeScolaire : {}", anneeScolaireDTO);

        return anneeScolaireRepository
            .findById(anneeScolaireDTO.getId())
            .map(existingAnneeScolaire -> {
                anneeScolaireMapper.partialUpdate(existingAnneeScolaire, anneeScolaireDTO);

                return existingAnneeScolaire;
            })
            .map(anneeScolaireRepository::save)
            .map(anneeScolaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnneeScolaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnneeScolaires");
        return anneeScolaireRepository.findAll(pageable).map(anneeScolaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnneeScolaireDTO> findOne(Long id) {
        log.debug("Request to get AnneeScolaire : {}", id);
        return anneeScolaireRepository.findById(id).map(anneeScolaireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnneeScolaire : {}", id);
        anneeScolaireRepository.deleteById(id);
    }
}
