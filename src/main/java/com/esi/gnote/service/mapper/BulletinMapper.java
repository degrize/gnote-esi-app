package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Bulletin;
import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.domain.Professeur;
import com.esi.gnote.domain.Semestre;
import com.esi.gnote.service.dto.BulletinDTO;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.dto.ProfesseurDTO;
import com.esi.gnote.service.dto.SemestreDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bulletin} and its DTO {@link BulletinDTO}.
 */
@Mapper(componentModel = "spring")
public interface BulletinMapper extends EntityMapper<BulletinDTO, Bulletin> {
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantMatriculeET")
    @Mapping(target = "semestre", source = "semestre", qualifiedByName = "semestreNomSemestre")
    @Mapping(target = "professeurs", source = "professeurs", qualifiedByName = "professeurNomProfSet")
    BulletinDTO toDto(Bulletin s);

    @Mapping(target = "removeProfesseur", ignore = true)
    Bulletin toEntity(BulletinDTO bulletinDTO);

    @Named("etudiantMatriculeET")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matriculeET", source = "matriculeET")
    EtudiantDTO toDtoEtudiantMatriculeET(Etudiant etudiant);

    @Named("semestreNomSemestre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomSemestre", source = "nomSemestre")
    SemestreDTO toDtoSemestreNomSemestre(Semestre semestre);

    @Named("professeurNomProf")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomProf", source = "nomProf")
    ProfesseurDTO toDtoProfesseurNomProf(Professeur professeur);

    @Named("professeurNomProfSet")
    default Set<ProfesseurDTO> toDtoProfesseurNomProfSet(Set<Professeur> professeur) {
        return professeur.stream().map(this::toDtoProfesseurNomProf).collect(Collectors.toSet());
    }
}
