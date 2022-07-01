package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Cycle;
import com.esi.gnote.repository.CycleRepository;
import com.esi.gnote.service.CycleService;
import com.esi.gnote.service.dto.CycleDTO;
import com.esi.gnote.service.mapper.CycleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cycle}.
 */
@Service
@Transactional
public class CycleServiceImpl implements CycleService {

    private final Logger log = LoggerFactory.getLogger(CycleServiceImpl.class);

    private final CycleRepository cycleRepository;

    private final CycleMapper cycleMapper;

    public CycleServiceImpl(CycleRepository cycleRepository, CycleMapper cycleMapper) {
        this.cycleRepository = cycleRepository;
        this.cycleMapper = cycleMapper;
    }

    @Override
    public CycleDTO save(CycleDTO cycleDTO) {
        log.debug("Request to save Cycle : {}", cycleDTO);
        Cycle cycle = cycleMapper.toEntity(cycleDTO);
        cycle = cycleRepository.save(cycle);
        return cycleMapper.toDto(cycle);
    }

    @Override
    public CycleDTO update(CycleDTO cycleDTO) {
        log.debug("Request to save Cycle : {}", cycleDTO);
        Cycle cycle = cycleMapper.toEntity(cycleDTO);
        cycle = cycleRepository.save(cycle);
        return cycleMapper.toDto(cycle);
    }

    @Override
    public Optional<CycleDTO> partialUpdate(CycleDTO cycleDTO) {
        log.debug("Request to partially update Cycle : {}", cycleDTO);

        return cycleRepository
            .findById(cycleDTO.getId())
            .map(existingCycle -> {
                cycleMapper.partialUpdate(existingCycle, cycleDTO);

                return existingCycle;
            })
            .map(cycleRepository::save)
            .map(cycleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CycleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cycles");
        return cycleRepository.findAll(pageable).map(cycleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CycleDTO> findOne(Long id) {
        log.debug("Request to get Cycle : {}", id);
        return cycleRepository.findById(id).map(cycleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cycle : {}", id);
        cycleRepository.deleteById(id);
    }
}
