package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.domain.Matiere;
import com.esi.gnote.domain.Note;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.dto.MatiereDTO;
import com.esi.gnote.service.dto.NoteDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
    @Mapping(target = "etudiants", source = "etudiants", qualifiedByName = "etudiantMatriculeETSet")
    @Mapping(target = "matieres", source = "matieres", qualifiedByName = "matiereNomECSet")
    NoteDTO toDto(Note s);

    @Mapping(target = "removeEtudiant", ignore = true)
    @Mapping(target = "removeMatiere", ignore = true)
    Note toEntity(NoteDTO noteDTO);

    @Named("etudiantMatriculeET")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matriculeET", source = "matriculeET")
    EtudiantDTO toDtoEtudiantMatriculeET(Etudiant etudiant);

    @Named("etudiantMatriculeETSet")
    default Set<EtudiantDTO> toDtoEtudiantMatriculeETSet(Set<Etudiant> etudiant) {
        return etudiant.stream().map(this::toDtoEtudiantMatriculeET).collect(Collectors.toSet());
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
