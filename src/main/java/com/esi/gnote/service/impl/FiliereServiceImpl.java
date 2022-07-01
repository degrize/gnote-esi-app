package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Filiere;
import com.esi.gnote.repository.FiliereRepository;
import com.esi.gnote.service.FiliereService;
import com.esi.gnote.service.dto.FiliereDTO;
import com.esi.gnote.service.mapper.FiliereMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Filiere}.
 */
@Service
@Transactional
public class FiliereServiceImpl implements FiliereService {

    private final Logger log = LoggerFactory.getLogger(FiliereServiceImpl.class);

    private final FiliereRepository filiereRepository;

    private final FiliereMapper filiereMapper;

    public FiliereServiceImpl(FiliereRepository filiereRepository, FiliereMapper filiereMapper) {
        this.filiereRepository = filiereRepository;
        this.filiereMapper = filiereMapper;
    }

    @Override
    public FiliereDTO save(FiliereDTO filiereDTO) {
        log.debug("Request to save Filiere : {}", filiereDTO);
        Filiere filiere = filiereMapper.toEntity(filiereDTO);
        filiere = filiereRepository.save(filiere);
        return filiereMapper.toDto(filiere);
    }

    @Override
    public FiliereDTO update(FiliereDTO filiereDTO) {
        log.debug("Request to save Filiere : {}", filiereDTO);
        Filiere filiere = filiereMapper.toEntity(filiereDTO);
        filiere = filiereRepository.save(filiere);
        return filiereMapper.toDto(filiere);
    }

    @Override
    public Optional<FiliereDTO> partialUpdate(FiliereDTO filiereDTO) {
        log.debug("Request to partially update Filiere : {}", filiereDTO);

        return filiereRepository
            .findById(filiereDTO.getId())
            .map(existingFiliere -> {
                filiereMapper.partialUpdate(existingFiliere, filiereDTO);

                return existingFiliere;
            })
            .map(filiereRepository::save)
            .map(filiereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiliereDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Filieres");
        return filiereRepository.findAll(pageable).map(filiereMapper::toDto);
    }

    public Page<FiliereDTO> findAllWithEagerRelationships(Pageable pageable) {
        return filiereRepository.findAllWithEagerRelationships(pageable).map(filiereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiliereDTO> findOne(Long id) {
        log.debug("Request to get Filiere : {}", id);
        return filiereRepository.findOneWithEagerRelationships(id).map(filiereMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Filiere : {}", id);
        filiereRepository.deleteById(id);
    }
}
