package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.domain.Horaire;
import com.esi.gnote.domain.Salle;
import com.esi.gnote.domain.Soutenance;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.dto.HoraireDTO;
import com.esi.gnote.service.dto.SalleDTO;
import com.esi.gnote.service.dto.SoutenanceDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Soutenance} and its DTO {@link SoutenanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface SoutenanceMapper extends EntityMapper<SoutenanceDTO, Soutenance> {
    @Mapping(target = "salle", source = "salle", qualifiedByName = "salleNumeroSalle")
    @Mapping(target = "horaire", source = "horaire", qualifiedByName = "horaireDateSout")
    @Mapping(target = "etudiants", source = "etudiants", qualifiedByName = "etudiantMatriculeETSet")
    SoutenanceDTO toDto(Soutenance s);

    @Mapping(target = "removeEtudiant", ignore = true)
    Soutenance toEntity(SoutenanceDTO soutenanceDTO);

    @Named("salleNumeroSalle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroSalle", source = "numeroSalle")
    SalleDTO toDtoSalleNumeroSalle(Salle salle);

    @Named("horaireDateSout")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dateSout", source = "dateSout")
    HoraireDTO toDtoHoraireDateSout(Horaire horaire);

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
