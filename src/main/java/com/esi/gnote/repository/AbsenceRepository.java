package com.esi.gnote.repository;

import com.esi.gnote.domain.Absence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Absence entity.
 */
@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    default Optional<Absence> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Absence> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Absence> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct absence from Absence absence left join fetch absence.professeur left join fetch absence.inspecteur left join fetch absence.matiere left join fetch absence.etudiant",
        countQuery = "select count(distinct absence) from Absence absence"
    )
    Page<Absence> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct absence from Absence absence left join fetch absence.professeur left join fetch absence.inspecteur left join fetch absence.matiere left join fetch absence.etudiant"
    )
    List<Absence> findAllWithToOneRelationships();

    @Query(
        "select absence from Absence absence left join fetch absence.professeur left join fetch absence.inspecteur left join fetch absence.matiere left join fetch absence.etudiant where absence.id =:id"
    )
    Optional<Absence> findOneWithToOneRelationships(@Param("id") Long id);
}
