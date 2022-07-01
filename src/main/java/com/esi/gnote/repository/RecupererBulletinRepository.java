package com.esi.gnote.repository;

import com.esi.gnote.domain.RecupererBulletin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RecupererBulletin entity.
 */
@Repository
public interface RecupererBulletinRepository extends JpaRepository<RecupererBulletin, Long> {
    default Optional<RecupererBulletin> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RecupererBulletin> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RecupererBulletin> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct recupererBulletin from RecupererBulletin recupererBulletin left join fetch recupererBulletin.etudiant left join fetch recupererBulletin.bulletin",
        countQuery = "select count(distinct recupererBulletin) from RecupererBulletin recupererBulletin"
    )
    Page<RecupererBulletin> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct recupererBulletin from RecupererBulletin recupererBulletin left join fetch recupererBulletin.etudiant left join fetch recupererBulletin.bulletin"
    )
    List<RecupererBulletin> findAllWithToOneRelationships();

    @Query(
        "select recupererBulletin from RecupererBulletin recupererBulletin left join fetch recupererBulletin.etudiant left join fetch recupererBulletin.bulletin where recupererBulletin.id =:id"
    )
    Optional<RecupererBulletin> findOneWithToOneRelationships(@Param("id") Long id);
}
