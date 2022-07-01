package com.esi.gnote.repository;

import com.esi.gnote.domain.Inspecteur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface InspecteurRepositoryWithBagRelationships {
    Optional<Inspecteur> fetchBagRelationships(Optional<Inspecteur> inspecteur);

    List<Inspecteur> fetchBagRelationships(List<Inspecteur> inspecteurs);

    Page<Inspecteur> fetchBagRelationships(Page<Inspecteur> inspecteurs);
}
