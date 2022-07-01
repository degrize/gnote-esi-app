package com.esi.gnote.repository;

import com.esi.gnote.domain.Semestre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Semestre entity.
 */
@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    default Optional<Semestre> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Semestre> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Semestre> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct semestre from Semestre semestre left join fetch semestre.anneeScolaire",
        countQuery = "select count(distinct semestre) from Semestre semestre"
    )
    Page<Semestre> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct semestre from Semestre semestre left join fetch semestre.anneeScolaire")
    List<Semestre> findAllWithToOneRelationships();

    @Query("select semestre from Semestre semestre left join fetch semestre.anneeScolaire where semestre.id =:id")
    Optional<Semestre> findOneWithToOneRelationships(@Param("id") Long id);
}
