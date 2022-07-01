package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Horaire;
import com.esi.gnote.domain.Salle;
import com.esi.gnote.service.dto.HoraireDTO;
import com.esi.gnote.service.dto.SalleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Salle} and its DTO {@link SalleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalleMapper extends EntityMapper<SalleDTO, Salle> {
    @Mapping(target = "horaires", source = "horaires", qualifiedByName = "horaireDateSoutSet")
    SalleDTO toDto(Salle s);

    @Mapping(target = "removeHoraire", ignore = true)
    Salle toEntity(SalleDTO salleDTO);

    @Named("horaireDateSout")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dateSout", source = "dateSout")
    HoraireDTO toDtoHoraireDateSout(Horaire horaire);

    @Named("horaireDateSoutSet")
    default Set<HoraireDTO> toDtoHoraireDateSoutSet(Set<Horaire> horaire) {
        return horaire.stream().map(this::toDtoHoraireDateSout).collect(Collectors.toSet());
    }
}
