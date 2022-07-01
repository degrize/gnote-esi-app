package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.repository.EtudiantRepository;
import com.esi.gnote.service.EtudiantService;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.mapper.EtudiantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etudiant}.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final Logger log = LoggerFactory.getLogger(EtudiantServiceImpl.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantMapper etudiantMapper;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
    }

    @Override
    public EtudiantDTO save(EtudiantDTO etudiantDTO) {
        log.debug("Request to save Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(etudiant);
    }

    @Override
    public EtudiantDTO update(EtudiantDTO etudiantDTO) {
        log.debug("Request to save Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(etudiant);
    }

    @Override
    public Optional<EtudiantDTO> partialUpdate(EtudiantDTO etudiantDTO) {
        log.debug("Request to partially update Etudiant : {}", etudiantDTO);

        return etudiantRepository
            .findById(etudiantDTO.getId())
            .map(existingEtudiant -> {
                etudiantMapper.partialUpdate(existingEtudiant, etudiantDTO);

                return existingEtudiant;
            })
            .map(etudiantRepository::save)
            .map(etudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EtudiantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Etudiants");
        return etudiantRepository.findAll(pageable).map(etudiantMapper::toDto);
    }

    public Page<EtudiantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return etudiantRepository.findAllWithEagerRelationships(pageable).map(etudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EtudiantDTO> findOne(Long id) {
        log.debug("Request to get Etudiant : {}", id);
        return etudiantRepository.findOneWithEagerRelationships(id).map(etudiantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etudiant : {}", id);
        etudiantRepository.deleteById(id);
    }
}
