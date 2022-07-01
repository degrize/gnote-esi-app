package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.domain.Inspecteur;
import com.esi.gnote.domain.Professeur;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.dto.InspecteurDTO;
import com.esi.gnote.service.dto.ProfesseurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inspecteur} and its DTO {@link InspecteurDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspecteurMapper extends EntityMapper<InspecteurDTO, Inspecteur> {
    @Mapping(target = "professeurs", source = "professeurs", qualifiedByName = "professeurNomProfSet")
    @Mapping(target = "etudiants", source = "etudiants", qualifiedByName = "etudiantMatriculeETSet")
    InspecteurDTO toDto(Inspecteur s);

    @Mapping(target = "removeProfesseur", ignore = true)
    @Mapping(target = "removeEtudiant", ignore = true)
    Inspecteur toEntity(InspecteurDTO inspecteurDTO);

    @Named("professeurNomProf")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomProf", source = "nomProf")
    ProfesseurDTO toDtoProfesseurNomProf(Professeur professeur);

    @Named("professeurNomProfSet")
    default Set<ProfesseurDTO> toDtoProfesseurNomProfSet(Set<Professeur> professeur) {
        return professeur.stream().map(this::toDtoProfesseurNomProf).collect(Collectors.toSet());
    }

    @Named("etudiantMatriculeET")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matriculeET", source = "matriculeET")
    EtudiantDTO toDtoEtudiantMatriculeET(Etudiant etudiant);

    @Named("etudiantMatriculeETSet")
    default Set<EtudiantDTO> toDtoEtudiantMatriculeETSet(Set<Etudiant> etudiant) {
        return etudiant.stream().map(this::toDtoEtudiantMatriculeET).collect(Collectors.toSet());
    }
}
