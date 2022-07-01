package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Matiere;
import com.esi.gnote.domain.Module;
import com.esi.gnote.service.dto.MatiereDTO;
import com.esi.gnote.service.dto.ModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Matiere} and its DTO {@link MatiereDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatiereMapper extends EntityMapper<MatiereDTO, Matiere> {
    @Mapping(target = "module", source = "module", qualifiedByName = "moduleId")
    MatiereDTO toDto(Matiere s);

    @Named("moduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModuleDTO toDtoModuleId(Module module);
}
