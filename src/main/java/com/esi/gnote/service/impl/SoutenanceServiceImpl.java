package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Soutenance;
import com.esi.gnote.repository.SoutenanceRepository;
import com.esi.gnote.service.SoutenanceService;
import com.esi.gnote.service.dto.SoutenanceDTO;
import com.esi.gnote.service.mapper.SoutenanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Soutenance}.
 */
@Service
@Transactional
public class SoutenanceServiceImpl implements SoutenanceService {

    private final Logger log = LoggerFactory.getLogger(SoutenanceServiceImpl.class);

    private final SoutenanceRepository soutenanceRepository;

    private final SoutenanceMapper soutenanceMapper;

    public SoutenanceServiceImpl(SoutenanceRepository soutenanceRepository, SoutenanceMapper soutenanceMapper) {
        this.soutenanceRepository = soutenanceRepository;
        this.soutenanceMapper = soutenanceMapper;
    }

    @Override
    public SoutenanceDTO save(SoutenanceDTO soutenanceDTO) {
        log.debug("Request to save Soutenance : {}", soutenanceDTO);
        Soutenance soutenance = soutenanceMapper.toEntity(soutenanceDTO);
        soutenance = soutenanceRepository.save(soutenance);
        return soutenanceMapper.toDto(soutenance);
    }

    @Override
    public SoutenanceDTO update(SoutenanceDTO soutenanceDTO) {
        log.debug("Request to save Soutenance : {}", soutenanceDTO);
        Soutenance soutenance = soutenanceMapper.toEntity(soutenanceDTO);
        soutenance = soutenanceRepository.save(soutenance);
        return soutenanceMapper.toDto(soutenance);
    }

    @Override
    public Optional<SoutenanceDTO> partialUpdate(SoutenanceDTO soutenanceDTO) {
        log.debug("Request to partially update Soutenance : {}", soutenanceDTO);

        return soutenanceRepository
            .findById(soutenanceDTO.getId())
            .map(existingSoutenance -> {
                soutenanceMapper.partialUpdate(existingSoutenance, soutenanceDTO);

                return existingSoutenance;
            })
            .map(soutenanceRepository::save)
            .map(soutenanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SoutenanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Soutenances");
        return soutenanceRepository.findAll(pageable).map(soutenanceMapper::toDto);
    }

    public Page<SoutenanceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return soutenanceRepository.findAllWithEagerRelationships(pageable).map(soutenanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SoutenanceDTO> findOne(Long id) {
        log.debug("Request to get Soutenance : {}", id);
        return soutenanceRepository.findOneWithEagerRelationships(id).map(soutenanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Soutenance : {}", id);
        soutenanceRepository.deleteById(id);
    }
}
