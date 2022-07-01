package com.esi.gnote.service.impl;

import com.esi.gnote.domain.DemandeInspecteurDE;
import com.esi.gnote.repository.DemandeInspecteurDERepository;
import com.esi.gnote.service.DemandeInspecteurDEService;
import com.esi.gnote.service.dto.DemandeInspecteurDEDTO;
import com.esi.gnote.service.mapper.DemandeInspecteurDEMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DemandeInspecteurDE}.
 */
@Service
@Transactional
public class DemandeInspecteurDEServiceImpl implements DemandeInspecteurDEService {

    private final Logger log = LoggerFactory.getLogger(DemandeInspecteurDEServiceImpl.class);

    private final DemandeInspecteurDERepository demandeInspecteurDERepository;

    private final DemandeInspecteurDEMapper demandeInspecteurDEMapper;

    public DemandeInspecteurDEServiceImpl(
        DemandeInspecteurDERepository demandeInspecteurDERepository,
        DemandeInspecteurDEMapper demandeInspecteurDEMapper
    ) {
        this.demandeInspecteurDERepository = demandeInspecteurDERepository;
        this.demandeInspecteurDEMapper = demandeInspecteurDEMapper;
    }

    @Override
    public DemandeInspecteurDEDTO save(DemandeInspecteurDEDTO demandeInspecteurDEDTO) {
        log.debug("Request to save DemandeInspecteurDE : {}", demandeInspecteurDEDTO);
        DemandeInspecteurDE demandeInspecteurDE = demandeInspecteurDEMapper.toEntity(demandeInspecteurDEDTO);
        demandeInspecteurDE = demandeInspecteurDERepository.save(demandeInspecteurDE);
        return demandeInspecteurDEMapper.toDto(demandeInspecteurDE);
    }

    @Override
    public DemandeInspecteurDEDTO update(DemandeInspecteurDEDTO demandeInspecteurDEDTO) {
        log.debug("Request to save DemandeInspecteurDE : {}", demandeInspecteurDEDTO);
        DemandeInspecteurDE demandeInspecteurDE = demandeInspecteurDEMapper.toEntity(demandeInspecteurDEDTO);
        demandeInspecteurDE = demandeInspecteurDERepository.save(demandeInspecteurDE);
        return demandeInspecteurDEMapper.toDto(demandeInspecteurDE);
    }

    @Override
    public Optional<DemandeInspecteurDEDTO> partialUpdate(DemandeInspecteurDEDTO demandeInspecteurDEDTO) {
        log.debug("Request to partially update DemandeInspecteurDE : {}", demandeInspecteurDEDTO);

        return demandeInspecteurDERepository
            .findById(demandeInspecteurDEDTO.getId())
            .map(existingDemandeInspecteurDE -> {
                demandeInspecteurDEMapper.partialUpdate(existingDemandeInspecteurDE, demandeInspecteurDEDTO);

                return existingDemandeInspecteurDE;
            })
            .map(demandeInspecteurDERepository::save)
            .map(demandeInspecteurDEMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemandeInspecteurDEDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DemandeInspecteurDES");
        return demandeInspecteurDERepository.findAll(pageable).map(demandeInspecteurDEMapper::toDto);
    }

    public Page<DemandeInspecteurDEDTO> findAllWithEagerRelationships(Pageable pageable) {
        return demandeInspecteurDERepository.findAllWithEagerRelationships(pageable).map(demandeInspecteurDEMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandeInspecteurDEDTO> findOne(Long id) {
        log.debug("Request to get DemandeInspecteurDE : {}", id);
        return demandeInspecteurDERepository.findOneWithEagerRelationships(id).map(demandeInspecteurDEMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandeInspecteurDE : {}", id);
        demandeInspecteurDERepository.deleteById(id);
    }
}
