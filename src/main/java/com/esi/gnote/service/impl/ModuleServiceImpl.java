package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Module;
import com.esi.gnote.repository.ModuleRepository;
import com.esi.gnote.service.ModuleService;
import com.esi.gnote.service.dto.ModuleDTO;
import com.esi.gnote.service.mapper.ModuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Module}.
 */
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);

    private final ModuleRepository moduleRepository;

    private final ModuleMapper moduleMapper;

    public ModuleServiceImpl(ModuleRepository moduleRepository, ModuleMapper moduleMapper) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public ModuleDTO save(ModuleDTO moduleDTO) {
        log.debug("Request to save Module : {}", moduleDTO);
        Module module = moduleMapper.toEntity(moduleDTO);
        module = moduleRepository.save(module);
        return moduleMapper.toDto(module);
    }

    @Override
    public ModuleDTO update(ModuleDTO moduleDTO) {
        log.debug("Request to save Module : {}", moduleDTO);
        Module module = moduleMapper.toEntity(moduleDTO);
        module = moduleRepository.save(module);
        return moduleMapper.toDto(module);
    }

    @Override
    public Optional<ModuleDTO> partialUpdate(ModuleDTO moduleDTO) {
        log.debug("Request to partially update Module : {}", moduleDTO);

        return moduleRepository
            .findById(moduleDTO.getId())
            .map(existingModule -> {
                moduleMapper.partialUpdate(existingModule, moduleDTO);

                return existingModule;
            })
            .map(moduleRepository::save)
            .map(moduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Modules");
        return moduleRepository.findAll(pageable).map(moduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ModuleDTO> findOne(Long id) {
        log.debug("Request to get Module : {}", id);
        return moduleRepository.findById(id).map(moduleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Module : {}", id);
        moduleRepository.deleteById(id);
    }
}
