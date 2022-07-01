package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.DemandeInspecteurDE;
import com.esi.gnote.domain.Inspecteur;
import com.esi.gnote.domain.Professeur;
import com.esi.gnote.service.dto.DemandeInspecteurDEDTO;
import com.esi.gnote.service.dto.InspecteurDTO;
import com.esi.gnote.service.dto.ProfesseurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DemandeInspecteurDE} and its DTO {@link DemandeInspecteurDEDTO}.
 */
@Mapper(componentModel = "spring")
public interface DemandeInspecteurDEMapper extends EntityMapper<DemandeInspecteurDEDTO, DemandeInspecteurDE> {
    @Mapping(target = "professeurs", source = "professeurs", qualifiedByName = "professeurNomProfSet")
    @Mapping(target = "inspecteurs", source = "inspecteurs", qualifiedByName = "inspecteurNomInspecteurSet")
    DemandeInspecteurDEDTO toDto(DemandeInspecteurDE s);

    @Mapping(target = "removeProfesseur", ignore = true)
    @Mapping(target = "removeInspecteur", ignore = true)
    DemandeInspecteurDE toEntity(DemandeInspecteurDEDTO demandeInspecteurDEDTO);

    @Named("professeurNomProf")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomProf", source = "nomProf")
    ProfesseurDTO toDtoProfesseurNomProf(Professeur professeur);

    @Named("professeurNomProfSet")
    default Set<ProfesseurDTO> toDtoProfesseurNomProfSet(Set<Professeur> professeur) {
        return professeur.stream().map(this::toDtoProfesseurNomProf).collect(Collectors.toSet());
    }

    @Named("inspecteurNomInspecteur")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomInspecteur", source = "nomInspecteur")
    InspecteurDTO toDtoInspecteurNomInspecteur(Inspecteur inspecteur);

    @Named("inspecteurNomInspecteurSet")
    default Set<InspecteurDTO> toDtoInspecteurNomInspecteurSet(Set<Inspecteur> inspecteur) {
        return inspecteur.stream().map(this::toDtoInspecteurNomInspecteur).collect(Collectors.toSet());
    }
}
