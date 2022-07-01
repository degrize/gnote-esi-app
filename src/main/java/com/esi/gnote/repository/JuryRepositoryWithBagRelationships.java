package com.esi.gnote.repository;

import com.esi.gnote.domain.Jury;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface JuryRepositoryWithBagRelationships {
    Optional<Jury> fetchBagRelationships(Optional<Jury> jury);

    List<Jury> fetchBagRelationships(List<Jury> juries);

    Page<Jury> fetchBagRelationships(Page<Jury> juries);
}
