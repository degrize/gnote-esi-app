package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Bulletin;
import com.esi.gnote.repository.BulletinRepository;
import com.esi.gnote.service.BulletinService;
import com.esi.gnote.service.dto.BulletinDTO;
import com.esi.gnote.service.mapper.BulletinMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bulletin}.
 */
@Service
@Transactional
public class BulletinServiceImpl implements BulletinService {

    private final Logger log = LoggerFactory.getLogger(BulletinServiceImpl.class);

    private final BulletinRepository bulletinRepository;

    private final BulletinMapper bulletinMapper;

    public BulletinServiceImpl(BulletinRepository bulletinRepository, BulletinMapper bulletinMapper) {
        this.bulletinRepository = bulletinRepository;
        this.bulletinMapper = bulletinMapper;
    }

    @Override
    public BulletinDTO save(BulletinDTO bulletinDTO) {
        log.debug("Request to save Bulletin : {}", bulletinDTO);
        Bulletin bulletin = bulletinMapper.toEntity(bulletinDTO);
        bulletin = bulletinRepository.save(bulletin);
        return bulletinMapper.toDto(bulletin);
    }

    @Override
    public BulletinDTO update(BulletinDTO bulletinDTO) {
        log.debug("Request to save Bulletin : {}", bulletinDTO);
        Bulletin bulletin = bulletinMapper.toEntity(bulletinDTO);
        bulletin = bulletinRepository.save(bulletin);
        return bulletinMapper.toDto(bulletin);
    }

    @Override
    public Optional<BulletinDTO> partialUpdate(BulletinDTO bulletinDTO) {
        log.debug("Request to partially update Bulletin : {}", bulletinDTO);

        return bulletinRepository
            .findById(bulletinDTO.getId())
            .map(existingBulletin -> {
                bulletinMapper.partialUpdate(existingBulletin, bulletinDTO);

                return existingBulletin;
            })
            .map(bulletinRepository::save)
            .map(bulletinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BulletinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bulletins");
        return bulletinRepository.findAll(pageable).map(bulletinMapper::toDto);
    }

    public Page<BulletinDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bulletinRepository.findAllWithEagerRelationships(pageable).map(bulletinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BulletinDTO> findOne(Long id) {
        log.debug("Request to get Bulletin : {}", id);
        return bulletinRepository.findOneWithEagerRelationships(id).map(bulletinMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bulletin : {}", id);
        bulletinRepository.deleteById(id);
    }
}
