package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Classe;
import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.domain.Matiere;
import com.esi.gnote.domain.Professeur;
import com.esi.gnote.service.dto.ClasseDTO;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.dto.MatiereDTO;
import com.esi.gnote.service.dto.ProfesseurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Professeur} and its DTO {@link ProfesseurDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfesseurMapper extends EntityMapper<ProfesseurDTO, Professeur> {
    @Mapping(target = "etudiants", source = "etudiants", qualifiedByName = "etudiantMatriculeETSet")
    @Mapping(target = "classes", source = "classes", qualifiedByName = "classeNomClasseSet")
    @Mapping(target = "matieres", source = "matieres", qualifiedByName = "matiereNomECSet")
    ProfesseurDTO toDto(Professeur s);

    @Mapping(target = "removeEtudiant", ignore = true)
    @Mapping(target = "removeClasse", ignore = true)
    @Mapping(target = "removeMatiere", ignore = true)
    Professeur toEntity(ProfesseurDTO professeurDTO);

    @Named("etudiantMatriculeET")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matriculeET", source = "matriculeET")
    EtudiantDTO toDtoEtudiantMatriculeET(Etudiant etudiant);

    @Named("etudiantMatriculeETSet")
    default Set<EtudiantDTO> toDtoEtudiantMatriculeETSet(Set<Etudiant> etudiant) {
        return etudiant.stream().map(this::toDtoEtudiantMatriculeET).collect(Collectors.toSet());
    }

    @Named("classeNomClasse")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomClasse", source = "nomClasse")
    ClasseDTO toDtoClasseNomClasse(Classe classe);

    @Named("classeNomClasseSet")
    default Set<ClasseDTO> toDtoClasseNomClasseSet(Set<Classe> classe) {
        return classe.stream().map(this::toDtoClasseNomClasse).collect(Collectors.toSet());
    }

    @Named("matiereNomEC")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomEC", source = "nomEC")
    MatiereDTO toDtoMatiereNomEC(Matiere matiere);

    @Named("matiereNomECSet")
    default Set<MatiereDTO> toDtoMatiereNomECSet(Set<Matiere> matiere) {
        return matiere.stream().map(this::toDtoMatiereNomEC).collect(Collectors.toSet());
    }
}
