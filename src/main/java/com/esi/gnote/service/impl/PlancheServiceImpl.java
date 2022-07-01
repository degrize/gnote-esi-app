package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Planche;
import com.esi.gnote.repository.PlancheRepository;
import com.esi.gnote.service.PlancheService;
import com.esi.gnote.service.dto.PlancheDTO;
import com.esi.gnote.service.mapper.PlancheMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Planche}.
 */
@Service
@Transactional
public class PlancheServiceImpl implements PlancheService {

    private final Logger log = LoggerFactory.getLogger(PlancheServiceImpl.class);

    private final PlancheRepository plancheRepository;

    private final PlancheMapper plancheMapper;

    public PlancheServiceImpl(PlancheRepository plancheRepository, PlancheMapper plancheMapper) {
        this.plancheRepository = plancheRepository;
        this.plancheMapper = plancheMapper;
    }

    @Override
    public PlancheDTO save(PlancheDTO plancheDTO) {
        log.debug("Request to save Planche : {}", plancheDTO);
        Planche planche = plancheMapper.toEntity(plancheDTO);
        planche = plancheRepository.save(planche);
        return plancheMapper.toDto(planche);
    }

    @Override
    public PlancheDTO update(PlancheDTO plancheDTO) {
        log.debug("Request to save Planche : {}", plancheDTO);
        Planche planche = plancheMapper.toEntity(plancheDTO);
        planche = plancheRepository.save(planche);
        return plancheMapper.toDto(planche);
    }

    @Override
    public Optional<PlancheDTO> partialUpdate(PlancheDTO plancheDTO) {
        log.debug("Request to partially update Planche : {}", plancheDTO);

        return plancheRepository
            .findById(plancheDTO.getId())
            .map(existingPlanche -> {
                plancheMapper.partialUpdate(existingPlanche, plancheDTO);

                return existingPlanche;
            })
            .map(plancheRepository::save)
            .map(plancheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlancheDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Planches");
        return plancheRepository.findAll(pageable).map(plancheMapper::toDto);
    }

    public Page<PlancheDTO> findAllWithEagerRelationships(Pageable pageable) {
        return plancheRepository.findAllWithEagerRelationships(pageable).map(plancheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlancheDTO> findOne(Long id) {
        log.debug("Request to get Planche : {}", id);
        return plancheRepository.findOneWithEagerRelationships(id).map(plancheMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Planche : {}", id);
        plancheRepository.deleteById(id);
    }
}
