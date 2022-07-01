package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Jury;
import com.esi.gnote.domain.Professeur;
import com.esi.gnote.domain.Soutenance;
import com.esi.gnote.service.dto.JuryDTO;
import com.esi.gnote.service.dto.ProfesseurDTO;
import com.esi.gnote.service.dto.SoutenanceDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Jury} and its DTO {@link JuryDTO}.
 */
@Mapper(componentModel = "spring")
public interface JuryMapper extends EntityMapper<JuryDTO, Jury> {
    @Mapping(target = "professeurs", source = "professeurs", qualifiedByName = "professeurNomProfSet")
    @Mapping(target = "soutenance", source = "soutenance", qualifiedByName = "soutenanceId")
    JuryDTO toDto(Jury s);

    @Mapping(target = "removeProfesseur", ignore = true)
    Jury toEntity(JuryDTO juryDTO);

    @Named("professeurNomProf")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomProf", source = "nomProf")
    ProfesseurDTO toDtoProfesseurNomProf(Professeur professeur);

    @Named("professeurNomProfSet")
    default Set<ProfesseurDTO> toDtoProfesseurNomProfSet(Set<Professeur> professeur) {
        return professeur.stream().map(this::toDtoProfesseurNomProf).collect(Collectors.toSet());
    }

    @Named("soutenanceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SoutenanceDTO toDtoSoutenanceId(Soutenance soutenance);
}
