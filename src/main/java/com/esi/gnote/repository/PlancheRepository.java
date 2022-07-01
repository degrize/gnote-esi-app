package com.esi.gnote.repository;

import com.esi.gnote.domain.Planche;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Planche entity.
 */
@Repository
public interface PlancheRepository extends JpaRepository<Planche, Long> {
    default Optional<Planche> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Planche> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Planche> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct planche from Planche planche left join fetch planche.semestre",
        countQuery = "select count(distinct planche) from Planche planche"
    )
    Page<Planche> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct planche from Planche planche left join fetch planche.semestre")
    List<Planche> findAllWithToOneRelationships();

    @Query("select planche from Planche planche left join fetch planche.semestre where planche.id =:id")
    Optional<Planche> findOneWithToOneRelationships(@Param("id") Long id);
}
