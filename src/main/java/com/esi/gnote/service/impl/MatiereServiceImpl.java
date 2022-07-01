package com.esi.gnote.service.impl;

import com.esi.gnote.domain.Matiere;
import com.esi.gnote.repository.MatiereRepository;
import com.esi.gnote.service.MatiereService;
import com.esi.gnote.service.dto.MatiereDTO;
import com.esi.gnote.service.mapper.MatiereMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Matiere}.
 */
@Service
@Transactional
public class MatiereServiceImpl implements MatiereService {

    private final Logger log = LoggerFactory.getLogger(MatiereServiceImpl.class);

    private final MatiereRepository matiereRepository;

    private final MatiereMapper matiereMapper;

    public MatiereServiceImpl(MatiereRepository matiereRepository, MatiereMapper matiereMapper) {
        this.matiereRepository = matiereRepository;
        this.matiereMapper = matiereMapper;
    }

    @Override
    public MatiereDTO save(MatiereDTO matiereDTO) {
        log.debug("Request to save Matiere : {}", matiereDTO);
        Matiere matiere = matiereMapper.toEntity(matiereDTO);
        matiere = matiereRepository.save(matiere);
        return matiereMapper.toDto(matiere);
    }

    @Override
    public MatiereDTO update(MatiereDTO matiereDTO) {
        log.debug("Request to save Matiere : {}", matiereDTO);
        Matiere matiere = matiereMapper.toEntity(matiereDTO);
        matiere = matiereRepository.save(matiere);
        return matiereMapper.toDto(matiere);
    }

    @Override
    public Optional<MatiereDTO> partialUpdate(MatiereDTO matiereDTO) {
        log.debug("Request to partially update Matiere : {}", matiereDTO);

        return matiereRepository
            .findById(matiereDTO.getId())
            .map(existingMatiere -> {
                matiereMapper.partialUpdate(existingMatiere, matiereDTO);

                return existingMatiere;
            })
            .map(matiereRepository::save)
            .map(matiereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatiereDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Matieres");
        return matiereRepository.findAll(pageable).map(matiereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MatiereDTO> findOne(Long id) {
        log.debug("Request to get Matiere : {}", id);
        return matiereRepository.findById(id).map(matiereMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Matiere : {}", id);
        matiereRepository.deleteById(id);
    }
}
