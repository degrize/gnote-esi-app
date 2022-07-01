package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.AnneeScolaire;
import com.esi.gnote.service.dto.AnneeScolaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnneeScolaire} and its DTO {@link AnneeScolaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnneeScolaireMapper extends EntityMapper<AnneeScolaireDTO, AnneeScolaire> {}
