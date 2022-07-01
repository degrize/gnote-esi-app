package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.AnneeScolaire;
import com.esi.gnote.domain.Semestre;
import com.esi.gnote.service.dto.AnneeScolaireDTO;
import com.esi.gnote.service.dto.SemestreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Semestre} and its DTO {@link SemestreDTO}.
 */
@Mapper(componentModel = "spring")
public interface SemestreMapper extends EntityMapper<SemestreDTO, Semestre> {
    @Mapping(target = "anneeScolaire", source = "anneeScolaire", qualifiedByName = "anneeScolairePeriode")
    SemestreDTO toDto(Semestre s);

    @Named("anneeScolairePeriode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "periode", source = "periode")
    AnneeScolaireDTO toDtoAnneeScolairePeriode(AnneeScolaire anneeScolaire);
}
