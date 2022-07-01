package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Classe;
import com.esi.gnote.repository.ClasseRepository;
import com.esi.gnote.service.ClasseService;
import com.esi.gnote.service.dto.ClasseDTO;
import com.esi.gnote.service.mapper.ClasseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Classe}.
 */
@Service
@Transactional
public class ClasseServiceImpl implements ClasseService {

    private final Logger log = LoggerFactory.getLogger(ClasseServiceImpl.class);

    private final ClasseRepository classeRepository;

    private final ClasseMapper classeMapper;

    public ClasseServiceImpl(ClasseRepository classeRepository, ClasseMapper classeMapper) {
        this.classeRepository = classeRepository;
        this.classeMapper = classeMapper;
    }

    @Override
    public ClasseDTO save(ClasseDTO classeDTO) {
        log.debug("Request to save Classe : {}", classeDTO);
        Classe classe = classeMapper.toEntity(classeDTO);
        classe = classeRepository.save(classe);
        return classeMapper.toDto(classe);
    }

    @Override
    public ClasseDTO update(ClasseDTO classeDTO) {
        log.debug("Request to save Classe : {}", classeDTO);
        Classe classe = classeMapper.toEntity(classeDTO);
        classe = classeRepository.save(classe);
        return classeMapper.toDto(classe);
    }

    @Override
    public Optional<ClasseDTO> partialUpdate(ClasseDTO classeDTO) {
        log.debug("Request to partially update Classe : {}", classeDTO);

        return classeRepository
            .findById(classeDTO.getId())
            .map(existingClasse -> {
                classeMapper.partialUpdate(existingClasse, classeDTO);

                return existingClasse;
            })
            .map(classeRepository::save)
            .map(classeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClasseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Classes");
        return classeRepository.findAll(pageable).map(classeMapper::toDto);
    }

    public Page<ClasseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return classeRepository.findAllWithEagerRelationships(pageable).map(classeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClasseDTO> findOne(Long id) {
        log.debug("Request to get Classe : {}", id);
        return classeRepository.findOneWithEagerRelationships(id).map(classeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Classe : {}", id);
        classeRepository.deleteById(id);
    }
}
