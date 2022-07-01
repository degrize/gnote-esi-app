package com.esi.gnote.repository;

import com.esi.gnote.domain.Bulletin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BulletinRepositoryWithBagRelationships {
    Optional<Bulletin> fetchBagRelationships(Optional<Bulletin> bulletin);

    List<Bulletin> fetchBagRelationships(List<Bulletin> bulletins);

    Page<Bulletin> fetchBagRelationships(Page<Bulletin> bulletins);
}
