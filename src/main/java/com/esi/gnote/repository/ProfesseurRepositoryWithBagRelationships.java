package com.esi.gnote.repository;

import com.esi.gnote.domain.Professeur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProfesseurRepositoryWithBagRelationships {
    Optional<Professeur> fetchBagRelationships(Optional<Professeur> professeur);

    List<Professeur> fetchBagRelationships(List<Professeur> professeurs);

    Page<Professeur> fetchBagRelationships(Page<Professeur> professeurs);
}
