package com.esi.gnote.repository;

import com.esi.gnote.domain.Soutenance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Soutenance entity.
 */
@Repository
public interface SoutenanceRepository extends SoutenanceRepositoryWithBagRelationships, JpaRepository<Soutenance, Long> {
    default Optional<Soutenance> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Soutenance> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Soutenance> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct soutenance from Soutenance soutenance left join fetch soutenance.salle left join fetch soutenance.horaire",
        countQuery = "select count(distinct soutenance) from Soutenance soutenance"
    )
    Page<Soutenance> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct soutenance from Soutenance soutenance left join fetch soutenance.salle left join fetch soutenance.horaire")
    List<Soutenance> findAllWithToOneRelationships();

    @Query(
        "select soutenance from Soutenance soutenance left join fetch soutenance.salle left join fetch soutenance.horaire where soutenance.id =:id"
    )
    Optional<Soutenance> findOneWithToOneRelationships(@Param("id") Long id);
}
