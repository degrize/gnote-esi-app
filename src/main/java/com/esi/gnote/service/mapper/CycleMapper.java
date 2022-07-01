package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Cycle;
import com.esi.gnote.service.dto.CycleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cycle} and its DTO {@link CycleDTO}.
 */
@Mapper(componentModel = "spring")
public interface CycleMapper extends EntityMapper<CycleDTO, Cycle> {}
