package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Inspecteur;
import com.esi.gnote.repository.InspecteurRepository;
import com.esi.gnote.service.InspecteurService;
import com.esi.gnote.service.dto.InspecteurDTO;
import com.esi.gnote.service.mapper.InspecteurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Inspecteur}.
 */
@Service
@Transactional
public class InspecteurServiceImpl implements InspecteurService {

    private final Logger log = LoggerFactory.getLogger(InspecteurServiceImpl.class);

    private final InspecteurRepository inspecteurRepository;

    private final InspecteurMapper inspecteurMapper;

    public InspecteurServiceImpl(InspecteurRepository inspecteurRepository, InspecteurMapper inspecteurMapper) {
        this.inspecteurRepository = inspecteurRepository;
        this.inspecteurMapper = inspecteurMapper;
    }

    @Override
    public InspecteurDTO save(InspecteurDTO inspecteurDTO) {
        log.debug("Request to save Inspecteur : {}", inspecteurDTO);
        Inspecteur inspecteur = inspecteurMapper.toEntity(inspecteurDTO);
        inspecteur = inspecteurRepository.save(inspecteur);
        return inspecteurMapper.toDto(inspecteur);
    }

    @Override
    public InspecteurDTO update(InspecteurDTO inspecteurDTO) {
        log.debug("Request to save Inspecteur : {}", inspecteurDTO);
        Inspecteur inspecteur = inspecteurMapper.toEntity(inspecteurDTO);
        inspecteur = inspecteurRepository.save(inspecteur);
        return inspecteurMapper.toDto(inspecteur);
    }

    @Override
    public Optional<InspecteurDTO> partialUpdate(InspecteurDTO inspecteurDTO) {
        log.debug("Request to partially update Inspecteur : {}", inspecteurDTO);

        return inspecteurRepository
            .findById(inspecteurDTO.getId())
            .map(existingInspecteur -> {
                inspecteurMapper.partialUpdate(existingInspecteur, inspecteurDTO);

                return existingInspecteur;
            })
            .map(inspecteurRepository::save)
            .map(inspecteurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InspecteurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Inspecteurs");
        return inspecteurRepository.findAll(pageable).map(inspecteurMapper::toDto);
    }

    public Page<InspecteurDTO> findAllWithEagerRelationships(Pageable pageable) {
        return inspecteurRepository.findAllWithEagerRelationships(pageable).map(inspecteurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InspecteurDTO> findOne(Long id) {
        log.debug("Request to get Inspecteur : {}", id);
        return inspecteurRepository.findOneWithEagerRelationships(id).map(inspecteurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inspecteur : {}", id);
        inspecteurRepository.deleteById(id);
    }
}
