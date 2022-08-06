package com.sti.stiprueba.service;

import com.sti.stiprueba.domain.Actividad;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Actividad}.
 */
public interface ActividadService {
    /**
     * Save a actividad.
     *
     * @param actividad the entity to save.
     * @return the persisted entity.
     */
    Actividad save(Actividad actividad);

    /**
     * Updates a actividad.
     *
     * @param actividad the entity to update.
     * @return the persisted entity.
     */
    Actividad update(Actividad actividad);

    /**
     * Partially updates a actividad.
     *
     * @param actividad the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Actividad> partialUpdate(Actividad actividad);

    /**
     * Get all the actividads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Actividad> findAll(Pageable pageable);

    /**
     * Get all the actividads with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Actividad> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" actividad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Actividad> findOne(Long id);

    /**
     * Delete the "id" actividad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
