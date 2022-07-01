package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Module;
import com.esi.gnote.service.dto.ModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Module} and its DTO {@link ModuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModuleMapper extends EntityMapper<ModuleDTO, Module> {}
