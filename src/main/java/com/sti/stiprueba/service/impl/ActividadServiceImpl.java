package com.sti.stiprueba.service.impl;

import com.sti.stiprueba.domain.Actividad;
import com.sti.stiprueba.repository.ActividadRepository;
import com.sti.stiprueba.service.ActividadService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Actividad}.
 */
@Service
@Transactional
public class ActividadServiceImpl implements ActividadService {

    private final Logger log = LoggerFactory.getLogger(ActividadServiceImpl.class);

    private final ActividadRepository actividadRepository;

    public ActividadServiceImpl(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    @Override
    public Actividad save(Actividad actividad) {
        log.debug("Request to save Actividad : {}", actividad);
        actividad.setFechaInicio(Instant.now());
        return actividadRepository.save(actividad);
    }

    @Override
    public Actividad update(Actividad actividad) {
        log.debug("Request to save Actividad : {}", actividad);
        int days = 0;
        if (actividad.getFechaFin().isBefore(Instant.now())) {
            days = (int) ChronoUnit.DAYS.between(actividad.getFechaFin(), Instant.now());
        }
        actividad.setDiasRetraso(days);
        return actividadRepository.save(actividad);
    }

    @Override
    public Optional<Actividad> partialUpdate(Actividad actividad) {
        log.debug("Request to partially update Actividad : {}", actividad);

        return actividadRepository
            .findById(actividad.getId())
            .map(existingActividad -> {
                if (actividad.getDescripcion() != null) {
                    existingActividad.setDescripcion(actividad.getDescripcion());
                }
                if (actividad.getEstatus() != null) {
                    existingActividad.setEstatus(actividad.getEstatus());
                }
                if (actividad.getFechaInicio() != null) {
                    existingActividad.setFechaInicio(actividad.getFechaInicio());
                }
                if (actividad.getFechaFin() != null) {
                    existingActividad.setFechaFin(actividad.getFechaFin());
                }
                if (actividad.getDiasRetraso() != null) {
                    int days = 0;
                    if (actividad.getFechaFin().isBefore(Instant.now())) {
                        days = (int) ChronoUnit.DAYS.between(actividad.getFechaFin(), Instant.now());
                    }
                    existingActividad.setDiasRetraso(days);
                }

                return existingActividad;
            })
            .map(actividadRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Actividad> findAll(Pageable pageable) {
        log.debug("Request to get all Actividads");

        Page<Actividad> actividades = actividadRepository.findAll(pageable);

        calculateLateDays(actividades);

        return actividades;
    }

    public Page<Actividad> findAllWithEagerRelationships(Pageable pageable) {
        Page<Actividad> actividades = actividadRepository.findAllWithEagerRelationships(pageable);

        calculateLateDays(actividades);

        return actividades;
    }

    private void calculateLateDays(Page<Actividad> actividades) {
        for (Actividad actividad : actividades) {
            int days = 0;
            if (actividad.getFechaFin().isBefore(Instant.now())) {
                days = (int) ChronoUnit.DAYS.between(actividad.getFechaFin(), Instant.now());
            }

            actividad.setDiasRetraso(days);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Actividad> findOne(Long id) {
        log.debug("Request to get Actividad : {}", id);

        Optional<Actividad> actividad = actividadRepository.findOneWithEagerRelationships(id);

        int days = 0;
        if (actividad.get().getFechaFin().isBefore(Instant.now())) {
            days = (int) ChronoUnit.DAYS.between(actividad.get().getFechaFin(), Instant.now());
            actividad.get().setDiasRetraso(days);
        }
        return actividadRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Actividad : {}", id);
        actividadRepository.deleteById(id);
    }
}
