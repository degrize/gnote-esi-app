package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Planche;
import com.esi.gnote.domain.Semestre;
import com.esi.gnote.service.dto.PlancheDTO;
import com.esi.gnote.service.dto.SemestreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Planche} and its DTO {@link PlancheDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlancheMapper extends EntityMapper<PlancheDTO, Planche> {
    @Mapping(target = "semestre", source = "semestre", qualifiedByName = "semestreNomSemestre")
    PlancheDTO toDto(Planche s);

    @Named("semestreNomSemestre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomSemestre", source = "nomSemestre")
    SemestreDTO toDtoSemestreNomSemestre(Semestre semestre);
}
