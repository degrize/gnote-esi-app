package com.esi.gnote.service.mapper;

import com.esi.gnote.domain.Bulletin;
import com.esi.gnote.domain.Etudiant;
import com.esi.gnote.domain.RecupererBulletin;
import com.esi.gnote.service.dto.BulletinDTO;
import com.esi.gnote.service.dto.EtudiantDTO;
import com.esi.gnote.service.dto.RecupererBulletinDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecupererBulletin} and its DTO {@link RecupererBulletinDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecupererBulletinMapper extends EntityMapper<RecupererBulletinDTO, RecupererBulletin> {
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantMatriculeET")
    @Mapping(target = "bulletin", source = "bulletin", qualifiedByName = "bulletinObservation")
    RecupererBulletinDTO toDto(RecupererBulletin s);

    @Named("etudiantMatriculeET")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matriculeET", source = "matriculeET")
    EtudiantDTO toDtoEtudiantMatriculeET(Etudiant etudiant);

    @Named("bulletinObservation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "observation", source = "observation")
    BulletinDTO toDtoBulletinObservation(Bulletin bulletin);
}
