package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Encadreur;
import com.esi.gnote.service.dto.EncadreurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Encadreur} and its DTO {@link EncadreurDTO}.
 */
@Mapper(componentModel = "spring")
public interface EncadreurMapper extends EntityMapper<EncadreurDTO, Encadreur> {}
