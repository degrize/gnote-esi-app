package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Horaire;
import com.esi.gnote.service.dto.HoraireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Horaire} and its DTO {@link HoraireDTO}.
 */
@Mapper(componentModel = "spring")
public interface HoraireMapper extends EntityMapper<HoraireDTO, Horaire> {}
