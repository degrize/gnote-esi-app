package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Classe;
import com.esi.gnote.domain.Filiere;
import com.esi.gnote.domain.Matiere;
import com.esi.gnote.service.dto.ClasseDTO;
import com.esi.gnote.service.dto.FiliereDTO;
import com.esi.gnote.service.dto.MatiereDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Classe} and its DTO {@link ClasseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClasseMapper extends EntityMapper<ClasseDTO, Classe> {
    @Mapping(target = "filiere", source = "filiere", qualifiedByName = "filiereNomFiliere")
    @Mapping(target = "matieres", source = "matieres", qualifiedByName = "matiereNomECSet")
    ClasseDTO toDto(Classe s);

    @Mapping(target = "removeMatiere", ignore = true)
    Classe toEntity(ClasseDTO classeDTO);

    @Named("filiereNomFiliere")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomFiliere", source = "nomFiliere")
    FiliereDTO toDtoFiliereNomFiliere(Filiere filiere);

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
