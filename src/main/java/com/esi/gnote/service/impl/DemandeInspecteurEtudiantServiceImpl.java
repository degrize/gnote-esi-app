package com.esi.gnote.service.impl;

import com.esi.gnote.domain.DemandeInspecteurEtudiant;
import com.esi.gnote.repository.DemandeInspecteurEtudiantRepository;
import com.esi.gnote.service.DemandeInspecteurEtudiantService;
import com.esi.gnote.service.dto.DemandeInspecteurEtudiantDTO;
import com.esi.gnote.service.mapper.DemandeInspecteurEtudiantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DemandeInspecteurEtudiant}.
 */
@Service
@Transactional
public class DemandeInspecteurEtudiantServiceImpl implements DemandeInspecteurEtudiantService {

    private final Logger log = LoggerFactory.getLogger(DemandeInspecteurEtudiantServiceImpl.class);

    private final DemandeInspecteurEtudiantRepository demandeInspecteurEtudiantRepository;

    private final DemandeInspecteurEtudiantMapper demandeInspecteurEtudiantMapper;

    public DemandeInspecteurEtudiantServiceImpl(
        DemandeInspecteurEtudiantRepository demandeInspecteurEtudiantRepository,
        DemandeInspecteurEtudiantMapper demandeInspecteurEtudiantMapper
    ) {
        this.demandeInspecteurEtudiantRepository = demandeInspecteurEtudiantRepository;
        this.demandeInspecteurEtudiantMapper = demandeInspecteurEtudiantMapper;
    }

    @Override
    public DemandeInspecteurEtudiantDTO save(DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO) {
        log.debug("Request to save DemandeInspecteurEtudiant : {}", demandeInspecteurEtudiantDTO);
        DemandeInspecteurEtudiant demandeInspecteurEtudiant = demandeInspecteurEtudiantMapper.toEntity(demandeInspecteurEtudiantDTO);
        demandeInspecteurEtudiant = demandeInspecteurEtudiantRepository.save(demandeInspecteurEtudiant);
        return demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);
    }

    @Override
    public DemandeInspecteurEtudiantDTO update(DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO) {
        log.debug("Request to save DemandeInspecteurEtudiant : {}", demandeInspecteurEtudiantDTO);
        DemandeInspecteurEtudiant demandeInspecteurEtudiant = demandeInspecteurEtudiantMapper.toEntity(demandeInspecteurEtudiantDTO);
        demandeInspecteurEtudiant = demandeInspecteurEtudiantRepository.save(demandeInspecteurEtudiant);
        return demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);
    }

    @Override
    public Optional<DemandeInspecteurEtudiantDTO> partialUpdate(DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO) {
        log.debug("Request to partially update DemandeInspecteurEtudiant : {}", demandeInspecteurEtudiantDTO);

        return demandeInspecteurEtudiantRepository
            .findById(demandeInspecteurEtudiantDTO.getId())
            .map(existingDemandeInspecteurEtudiant -> {
                demandeInspecteurEtudiantMapper.partialUpdate(existingDemandeInspecteurEtudiant, demandeInspecteurEtudiantDTO);

                return existingDemandeInspecteurEtudiant;
            })
            .map(demandeInspecteurEtudiantRepository::save)
            .map(demandeInspecteurEtudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemandeInspecteurEtudiantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DemandeInspecteurEtudiants");
        return demandeInspecteurEtudiantRepository.findAll(pageable).map(demandeInspecteurEtudiantMapper::toDto);
    }

    public Page<DemandeInspecteurEtudiantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return demandeInspecteurEtudiantRepository.findAllWithEagerRelationships(pageable).map(demandeInspecteurEtudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandeInspecteurEtudiantDTO> findOne(Long id) {
        log.debug("Request to get DemandeInspecteurEtudiant : {}", id);
        return demandeInspecteurEtudiantRepository.findOneWithEagerRelationships(id).map(demandeInspecteurEtudiantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandeInspecteurEtudiant : {}", id);
        demandeInspecteurEtudiantRepository.deleteById(id);
    }
}
