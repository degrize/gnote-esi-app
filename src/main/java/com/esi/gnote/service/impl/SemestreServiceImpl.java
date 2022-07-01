package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Semestre;
import com.esi.gnote.repository.SemestreRepository;
import com.esi.gnote.service.SemestreService;
import com.esi.gnote.service.dto.SemestreDTO;
import com.esi.gnote.service.mapper.SemestreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Semestre}.
 */
@Service
@Transactional
public class SemestreServiceImpl implements SemestreService {

    private final Logger log = LoggerFactory.getLogger(SemestreServiceImpl.class);

    private final SemestreRepository semestreRepository;

    private final SemestreMapper semestreMapper;

    public SemestreServiceImpl(SemestreRepository semestreRepository, SemestreMapper semestreMapper) {
        this.semestreRepository = semestreRepository;
        this.semestreMapper = semestreMapper;
    }

    @Override
    public SemestreDTO save(SemestreDTO semestreDTO) {
        log.debug("Request to save Semestre : {}", semestreDTO);
        Semestre semestre = semestreMapper.toEntity(semestreDTO);
        semestre = semestreRepository.save(semestre);
        return semestreMapper.toDto(semestre);
    }

    @Override
    public SemestreDTO update(SemestreDTO semestreDTO) {
        log.debug("Request to save Semestre : {}", semestreDTO);
        Semestre semestre = semestreMapper.toEntity(semestreDTO);
        semestre = semestreRepository.save(semestre);
        return semestreMapper.toDto(semestre);
    }

    @Override
    public Optional<SemestreDTO> partialUpdate(SemestreDTO semestreDTO) {
        log.debug("Request to partially update Semestre : {}", semestreDTO);

        return semestreRepository
            .findById(semestreDTO.getId())
            .map(existingSemestre -> {
                semestreMapper.partialUpdate(existingSemestre, semestreDTO);

                return existingSemestre;
            })
            .map(semestreRepository::save)
            .map(semestreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SemestreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Semestres");
        return semestreRepository.findAll(pageable).map(semestreMapper::toDto);
    }

    public Page<SemestreDTO> findAllWithEagerRelationships(Pageable pageable) {
        return semestreRepository.findAllWithEagerRelationships(pageable).map(semestreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SemestreDTO> findOne(Long id) {
        log.debug("Request to get Semestre : {}", id);
        return semestreRepository.findOneWithEagerRelationships(id).map(semestreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Semestre : {}", id);
        semestreRepository.deleteById(id);
    }
}
