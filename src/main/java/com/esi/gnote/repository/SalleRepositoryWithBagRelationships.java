package com.esi.gnote.repository;

import com.esi.gnote.domain.Salle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SalleRepositoryWithBagRelationships {
    Optional<Salle> fetchBagRelationships(Optional<Salle> salle);

    List<Salle> fetchBagRelationships(List<Salle> salles);

    Page<Salle> fetchBagRelationships(Page<Salle> salles);
}
