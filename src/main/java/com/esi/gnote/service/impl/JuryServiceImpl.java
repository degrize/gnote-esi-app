package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Jury;
import com.esi.gnote.repository.JuryRepository;
import com.esi.gnote.service.JuryService;
import com.esi.gnote.service.dto.JuryDTO;
import com.esi.gnote.service.mapper.JuryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Jury}.
 */
@Service
@Transactional
public class JuryServiceImpl implements JuryService {

    private final Logger log = LoggerFactory.getLogger(JuryServiceImpl.class);

    private final JuryRepository juryRepository;

    private final JuryMapper juryMapper;

    public JuryServiceImpl(JuryRepository juryRepository, JuryMapper juryMapper) {
        this.juryRepository = juryRepository;
        this.juryMapper = juryMapper;
    }

    @Override
    public JuryDTO save(JuryDTO juryDTO) {
        log.debug("Request to save Jury : {}", juryDTO);
        Jury jury = juryMapper.toEntity(juryDTO);
        jury = juryRepository.save(jury);
        return juryMapper.toDto(jury);
    }

    @Override
    public JuryDTO update(JuryDTO juryDTO) {
        log.debug("Request to save Jury : {}", juryDTO);
        Jury jury = juryMapper.toEntity(juryDTO);
        jury = juryRepository.save(jury);
        return juryMapper.toDto(jury);
    }

    @Override
    public Optional<JuryDTO> partialUpdate(JuryDTO juryDTO) {
        log.debug("Request to partially update Jury : {}", juryDTO);

        return juryRepository
            .findById(juryDTO.getId())
            .map(existingJury -> {
                juryMapper.partialUpdate(existingJury, juryDTO);

                return existingJury;
            })
            .map(juryRepository::save)
            .map(juryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JuryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Juries");
        return juryRepository.findAll(pageable).map(juryMapper::toDto);
    }

    public Page<JuryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return juryRepository.findAllWithEagerRelationships(pageable).map(juryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JuryDTO> findOne(Long id) {
        log.debug("Request to get Jury : {}", id);
        return juryRepository.findOneWithEagerRelationships(id).map(juryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Jury : {}", id);
        juryRepository.deleteById(id);
    }
}
