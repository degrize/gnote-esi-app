package com.esi.gnote.repository;

import com.esi.gnote.domain.Classe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ClasseRepositoryWithBagRelationships {
    Optional<Classe> fetchBagRelationships(Optional<Classe> classe);

    List<Classe> fetchBagRelationships(List<Classe> classes);

    Page<Classe> fetchBagRelationships(Page<Classe> classes);
}
