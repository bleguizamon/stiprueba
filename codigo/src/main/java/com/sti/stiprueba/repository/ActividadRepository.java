package com.sti.stiprueba.repository;

import com.sti.stiprueba.domain.Actividad;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Actividad entity.
 */
@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    default Optional<Actividad> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Actividad> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Actividad> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct actividad from Actividad actividad left join fetch actividad.empleado",
        countQuery = "select count(distinct actividad) from Actividad actividad"
    )
    Page<Actividad> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct actividad from Actividad actividad left join fetch actividad.empleado")
    List<Actividad> findAllWithToOneRelationships();

    @Query("select actividad from Actividad actividad left join fetch actividad.empleado where actividad.id =:id")
    Optional<Actividad> findOneWithToOneRelationships(@Param("id") Long id);
}
