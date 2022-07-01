package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Encadreur;
import com.esi.gnote.repository.EncadreurRepository;
import com.esi.gnote.service.EncadreurService;
import com.esi.gnote.service.dto.EncadreurDTO;
import com.esi.gnote.service.mapper.EncadreurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Encadreur}.
 */
@Service
@Transactional
public class EncadreurServiceImpl implements EncadreurService {

    private final Logger log = LoggerFactory.getLogger(EncadreurServiceImpl.class);

    private final EncadreurRepository encadreurRepository;

    private final EncadreurMapper encadreurMapper;

    public EncadreurServiceImpl(EncadreurRepository encadreurRepository, EncadreurMapper encadreurMapper) {
        this.encadreurRepository = encadreurRepository;
        this.encadreurMapper = encadreurMapper;
    }

    @Override
    public EncadreurDTO save(EncadreurDTO encadreurDTO) {
        log.debug("Request to save Encadreur : {}", encadreurDTO);
        Encadreur encadreur = encadreurMapper.toEntity(encadreurDTO);
        encadreur = encadreurRepository.save(encadreur);
        return encadreurMapper.toDto(encadreur);
    }

    @Override
    public EncadreurDTO update(EncadreurDTO encadreurDTO) {
        log.debug("Request to save Encadreur : {}", encadreurDTO);
        Encadreur encadreur = encadreurMapper.toEntity(encadreurDTO);
        encadreur = encadreurRepository.save(encadreur);
        return encadreurMapper.toDto(encadreur);
    }

    @Override
    public Optional<EncadreurDTO> partialUpdate(EncadreurDTO encadreurDTO) {
        log.debug("Request to partially update Encadreur : {}", encadreurDTO);

        return encadreurRepository
            .findById(encadreurDTO.getId())
            .map(existingEncadreur -> {
                encadreurMapper.partialUpdate(existingEncadreur, encadreurDTO);

                return existingEncadreur;
            })
            .map(encadreurRepository::save)
            .map(encadreurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EncadreurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Encadreurs");
        return encadreurRepository.findAll(pageable).map(encadreurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EncadreurDTO> findOne(Long id) {
        log.debug("Request to get Encadreur : {}", id);
        return encadreurRepository.findById(id).map(encadreurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Encadreur : {}", id);
        encadreurRepository.deleteById(id);
    }
}
