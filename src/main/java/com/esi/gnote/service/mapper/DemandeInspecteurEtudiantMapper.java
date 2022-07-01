package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.DemandeInspecteurEtudiant;
import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.domain.Inspecteur;
import com.esi.gnote.service.dto.DemandeInspecteurEtudiantDTO;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.dto.InspecteurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DemandeInspecteurEtudiant} and its DTO {@link DemandeInspecteurEtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface DemandeInspecteurEtudiantMapper extends EntityMapper<DemandeInspecteurEtudiantDTO, DemandeInspecteurEtudiant> {
    @Mapping(target = "etudiants", source = "etudiants", qualifiedByName = "etudiantMatriculeETSet")
    @Mapping(target = "inspecteurs", source = "inspecteurs", qualifiedByName = "inspecteurNomInspecteurSet")
    DemandeInspecteurEtudiantDTO toDto(DemandeInspecteurEtudiant s);

    @Mapping(target = "removeEtudiant", ignore = true)
    @Mapping(target = "removeInspecteur", ignore = true)
    DemandeInspecteurEtudiant toEntity(DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO);

    @Named("etudiantMatriculeET")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matriculeET", source = "matriculeET")
    EtudiantDTO toDtoEtudiantMatriculeET(Etudiant etudiant);

    @Named("etudiantMatriculeETSet")
    default Set<EtudiantDTO> toDtoEtudiantMatriculeETSet(Set<Etudiant> etudiant) {
        return etudiant.stream().map(this::toDtoEtudiantMatriculeET).collect(Collectors.toSet());
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
