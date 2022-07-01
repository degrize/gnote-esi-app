package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Absence;
import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.domain.Inspecteur;
import com.esi.gnote.domain.Matiere;
import com.esi.gnote.domain.Professeur;
import com.esi.gnote.service.dto.AbsenceDTO;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.dto.InspecteurDTO;
import com.esi.gnote.service.dto.MatiereDTO;
import com.esi.gnote.service.dto.ProfesseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Absence} and its DTO {@link AbsenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {
    @Mapping(target = "professeur", source = "professeur", qualifiedByName = "professeurNomProf")
    @Mapping(target = "inspecteur", source = "inspecteur", qualifiedByName = "inspecteurNomInspecteur")
    @Mapping(target = "matiere", source = "matiere", qualifiedByName = "matiereNomEC")
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantMatriculeET")
    AbsenceDTO toDto(Absence s);

    @Named("professeurNomProf")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomProf", source = "nomProf")
    ProfesseurDTO toDtoProfesseurNomProf(Professeur professeur);

    @Named("inspecteurNomInspecteur")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomInspecteur", source = "nomInspecteur")
    InspecteurDTO toDtoInspecteurNomInspecteur(Inspecteur inspecteur);

    @Named("matiereNomEC")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomEC", source = "nomEC")
    MatiereDTO toDtoMatiereNomEC(Matiere matiere);

    @Named("etudiantMatriculeET")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matriculeET", source = "matriculeET")
    EtudiantDTO toDtoEtudiantMatriculeET(Etudiant etudiant);
}
