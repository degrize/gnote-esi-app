package com.esi.gnote.repository;

import com.esi.gnote.domain.DemandeInspecteurDE;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DemandeInspecteurDERepositoryWithBagRelationships {
    Optional<DemandeInspecteurDE> fetchBagRelationships(Optional<DemandeInspecteurDE> demandeInspecteurDE);

    List<DemandeInspecteurDE> fetchBagRelationships(List<DemandeInspecteurDE> demandeInspecteurDES);

    Page<DemandeInspecteurDE> fetchBagRelationships(Page<DemandeInspecteurDE> demandeInspecteurDES);
}
