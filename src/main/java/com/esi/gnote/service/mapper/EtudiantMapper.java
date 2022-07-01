package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Classe;
import com.esi.gnote.domain.Encadreur;
import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.service.dto.ClasseDTO;
import com.esi.gnote.service.dto.EncadreurDTO;
import com.esi.gnote.service.dto.EtudiantDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Etudiant} and its DTO {@link EtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EtudiantMapper extends EntityMapper<EtudiantDTO, Etudiant> {
    @Mapping(target = "classes", source = "classes", qualifiedByName = "classeNomClasseSet")
    @Mapping(target = "encadreur", source = "encadreur", qualifiedByName = "encadreurId")
    EtudiantDTO toDto(Etudiant s);

    @Mapping(target = "removeClasse", ignore = true)
    Etudiant toEntity(EtudiantDTO etudiantDTO);

    @Named("classeNomClasse")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomClasse", source = "nomClasse")
    ClasseDTO toDtoClasseNomClasse(Classe classe);

    @Named("classeNomClasseSet")
    default Set<ClasseDTO> toDtoClasseNomClasseSet(Set<Classe> classe) {
        return classe.stream().map(this::toDtoClasseNomClasse).collect(Collectors.toSet());
    }

    @Named("encadreurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EncadreurDTO toDtoEncadreurId(Encadreur encadreur);
}
