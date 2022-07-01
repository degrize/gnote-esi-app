package com.esi.gnote.repository;

import com.esi.gnote.domain.Soutenance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SoutenanceRepositoryWithBagRelationships {
    Optional<Soutenance> fetchBagRelationships(Optional<Soutenance> soutenance);

    List<Soutenance> fetchBagRelationships(List<Soutenance> soutenances);

    Page<Soutenance> fetchBagRelationships(Page<Soutenance> soutenances);
}
