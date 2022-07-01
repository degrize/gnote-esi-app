package com.esi.gnote.repository;

import com.esi.gnote.domain.Bulletin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bulletin entity.
 */
@Repository
public interface BulletinRepository extends BulletinRepositoryWithBagRelationships, JpaRepository<Bulletin, Long> {
    default Optional<Bulletin> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Bulletin> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Bulletin> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct bulletin from Bulletin bulletin left join fetch bulletin.etudiant left join fetch bulletin.semestre",
        countQuery = "select count(distinct bulletin) from Bulletin bulletin"
    )
    Page<Bulletin> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct bulletin from Bulletin bulletin left join fetch bulletin.etudiant left join fetch bulletin.semestre")
    List<Bulletin> findAllWithToOneRelationships();

    @Query(
        "select bulletin from Bulletin bulletin left join fetch bulletin.etudiant left join fetch bulletin.semestre where bulletin.id =:id"
    )
    Optional<Bulletin> findOneWithToOneRelationships(@Param("id") Long id);
}
