package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Horaire;
import com.esi.gnote.repository.HoraireRepository;
import com.esi.gnote.service.HoraireService;
import com.esi.gnote.service.dto.HoraireDTO;
import com.esi.gnote.service.mapper.HoraireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Horaire}.
 */
@Service
@Transactional
public class HoraireServiceImpl implements HoraireService {

    private final Logger log = LoggerFactory.getLogger(HoraireServiceImpl.class);

    private final HoraireRepository horaireRepository;

    private final HoraireMapper horaireMapper;

    public HoraireServiceImpl(HoraireRepository horaireRepository, HoraireMapper horaireMapper) {
        this.horaireRepository = horaireRepository;
        this.horaireMapper = horaireMapper;
    }

    @Override
    public HoraireDTO save(HoraireDTO horaireDTO) {
        log.debug("Request to save Horaire : {}", horaireDTO);
        Horaire horaire = horaireMapper.toEntity(horaireDTO);
        horaire = horaireRepository.save(horaire);
        return horaireMapper.toDto(horaire);
    }

    @Override
    public HoraireDTO update(HoraireDTO horaireDTO) {
        log.debug("Request to save Horaire : {}", horaireDTO);
        Horaire horaire = horaireMapper.toEntity(horaireDTO);
        horaire = horaireRepository.save(horaire);
        return horaireMapper.toDto(horaire);
    }

    @Override
    public Optional<HoraireDTO> partialUpdate(HoraireDTO horaireDTO) {
        log.debug("Request to partially update Horaire : {}", horaireDTO);

        return horaireRepository
            .findById(horaireDTO.getId())
            .map(existingHoraire -> {
                horaireMapper.partialUpdate(existingHoraire, horaireDTO);

                return existingHoraire;
            })
            .map(horaireRepository::save)
            .map(horaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HoraireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Horaires");
        return horaireRepository.findAll(pageable).map(horaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HoraireDTO> findOne(Long id) {
        log.debug("Request to get Horaire : {}", id);
        return horaireRepository.findById(id).map(horaireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Horaire : {}", id);
        horaireRepository.deleteById(id);
    }
}
