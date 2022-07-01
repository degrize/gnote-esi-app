package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Cycle;
import com.esi.gnote.domain.Filiere;
import com.esi.gnote.domain.Module;
import com.esi.gnote.service.dto.CycleDTO;
import com.esi.gnote.service.dto.FiliereDTO;
import com.esi.gnote.service.dto.ModuleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Filiere} and its DTO {@link FiliereDTO}.
 */
@Mapper(componentModel = "spring")
public interface FiliereMapper extends EntityMapper<FiliereDTO, Filiere> {
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "cycleId")
    @Mapping(target = "modules", source = "modules", qualifiedByName = "moduleNomUESet")
    FiliereDTO toDto(Filiere s);

    @Mapping(target = "removeModule", ignore = true)
    Filiere toEntity(FiliereDTO filiereDTO);

    @Named("cycleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CycleDTO toDtoCycleId(Cycle cycle);

    @Named("moduleNomUE")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomUE", source = "nomUE")
    ModuleDTO toDtoModuleNomUE(Module module);

    @Named("moduleNomUESet")
    default Set<ModuleDTO> toDtoModuleNomUESet(Set<Module> module) {
        return module.stream().map(this::toDtoModuleNomUE).collect(Collectors.toSet());
    }
}
