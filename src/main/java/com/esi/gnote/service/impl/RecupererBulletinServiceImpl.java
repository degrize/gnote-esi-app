package com.esi.gnote.service.impl;

import com.esi.gnote.domain.RecupererBulletin;
import com.esi.gnote.repository.RecupererBulletinRepository;
import com.esi.gnote.service.RecupererBulletinService;
import com.esi.gnote.service.dto.RecupererBulletinDTO;
import com.esi.gnote.service.mapper.RecupererBulletinMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RecupererBulletin}.
 */
@Service
@Transactional
public class RecupererBulletinServiceImpl implements RecupererBulletinService {

    private final Logger log = LoggerFactory.getLogger(RecupererBulletinServiceImpl.class);

    private final RecupererBulletinRepository recupererBulletinRepository;

    private final RecupererBulletinMapper recupererBulletinMapper;

    public RecupererBulletinServiceImpl(
        RecupererBulletinRepository recupererBulletinRepository,
        RecupererBulletinMapper recupererBulletinMapper
    ) {
        this.recupererBulletinRepository = recupererBulletinRepository;
        this.recupererBulletinMapper = recupererBulletinMapper;
    }

    @Override
    public RecupererBulletinDTO save(RecupererBulletinDTO recupererBulletinDTO) {
        log.debug("Request to save RecupererBulletin : {}", recupererBulletinDTO);
        RecupererBulletin recupererBulletin = recupererBulletinMapper.toEntity(recupererBulletinDTO);
        recupererBulletin = recupererBulletinRepository.save(recupererBulletin);
        return recupererBulletinMapper.toDto(recupererBulletin);
    }

    @Override
    public RecupererBulletinDTO update(RecupererBulletinDTO recupererBulletinDTO) {
        log.debug("Request to save RecupererBulletin : {}", recupererBulletinDTO);
        RecupererBulletin recupererBulletin = recupererBulletinMapper.toEntity(recupererBulletinDTO);
        recupererBulletin = recupererBulletinRepository.save(recupererBulletin);
        return recupererBulletinMapper.toDto(recupererBulletin);
    }

    @Override
    public Optional<RecupererBulletinDTO> partialUpdate(RecupererBulletinDTO recupererBulletinDTO) {
        log.debug("Request to partially update RecupererBulletin : {}", recupererBulletinDTO);

        return recupererBulletinRepository
            .findById(recupererBulletinDTO.getId())
            .map(existingRecupererBulletin -> {
                recupererBulletinMapper.partialUpdate(existingRecupererBulletin, recupererBulletinDTO);

                return existingRecupererBulletin;
            })
            .map(recupererBulletinRepository::save)
            .map(recupererBulletinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecupererBulletinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RecupererBulletins");
        return recupererBulletinRepository.findAll(pageable).map(recupererBulletinMapper::toDto);
    }

    public Page<RecupererBulletinDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recupererBulletinRepository.findAllWithEagerRelationships(pageable).map(recupererBulletinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecupererBulletinDTO> findOne(Long id) {
        log.debug("Request to get RecupererBulletin : {}", id);
        return recupererBulletinRepository.findOneWithEagerRelationships(id).map(recupererBulletinMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecupererBulletin : {}", id);
        recupererBulletinRepository.deleteById(id);
    }
}
