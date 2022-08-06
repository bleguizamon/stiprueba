package com.sti.stiprueba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sti.stiprueba.IntegrationTest;
import com.sti.stiprueba.domain.Actividad;
import com.sti.stiprueba.domain.enumeration.Estatus;
import com.sti.stiprueba.repository.ActividadRepository;
import com.sti.stiprueba.service.ActividadService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ActividadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ActividadResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Estatus DEFAULT_ESTATUS = Estatus.REALIZADA;
    private static final Estatus UPDATED_ESTATUS = Estatus.PENDIENTE;

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DIAS_RETRASO = 1;
    private static final Integer UPDATED_DIAS_RETRASO = 2;

    private static final String ENTITY_API_URL = "/api/actividads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActividadRepository actividadRepository;

    @Mock
    private ActividadRepository actividadRepositoryMock;

    @Mock
    private ActividadService actividadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActividadMockMvc;

    private Actividad actividad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actividad createEntity(EntityManager em) {
        Actividad actividad = new Actividad()
            .descripcion(DEFAULT_DESCRIPCION)
            .estatus(DEFAULT_ESTATUS)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .diasRetraso(DEFAULT_DIAS_RETRASO);
        return actividad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actividad createUpdatedEntity(EntityManager em) {
        Actividad actividad = new Actividad()
            .descripcion(UPDATED_DESCRIPCION)
            .estatus(UPDATED_ESTATUS)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .diasRetraso(UPDATED_DIAS_RETRASO);
        return actividad;
    }

    @BeforeEach
    public void initTest() {
        actividad = createEntity(em);
    }

    @Test
    @Transactional
    void createActividad() throws Exception {
        int databaseSizeBeforeCreate = actividadRepository.findAll().size();
        // Create the Actividad
        restActividadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actividad)))
            .andExpect(status().isCreated());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeCreate + 1);
        Actividad testActividad = actividadList.get(actividadList.size() - 1);
        assertThat(testActividad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testActividad.getEstatus()).isEqualTo(DEFAULT_ESTATUS);
        assertThat(testActividad.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testActividad.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testActividad.getDiasRetraso()).isEqualTo(DEFAULT_DIAS_RETRASO);
    }

    @Test
    @Transactional
    void createActividadWithExistingId() throws Exception {
        // Create the Actividad with an existing ID
        actividad.setId(1L);

        int databaseSizeBeforeCreate = actividadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActividadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actividad)))
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = actividadRepository.findAll().size();
        // set the field null
        actividad.setDescripcion(null);

        // Create the Actividad, which fails.

        restActividadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actividad)))
            .andExpect(status().isBadRequest());

        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActividads() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        // Get all the actividadList
        restActividadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actividad.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].estatus").value(hasItem(DEFAULT_ESTATUS.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].diasRetraso").value(hasItem(DEFAULT_DIAS_RETRASO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllActividadsWithEagerRelationshipsIsEnabled() throws Exception {
        when(actividadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restActividadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(actividadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllActividadsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(actividadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restActividadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(actividadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        // Get the actividad
        restActividadMockMvc
            .perform(get(ENTITY_API_URL_ID, actividad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(actividad.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.estatus").value(DEFAULT_ESTATUS.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.diasRetraso").value(DEFAULT_DIAS_RETRASO));
    }

    @Test
    @Transactional
    void getNonExistingActividad() throws Exception {
        // Get the actividad
        restActividadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();

        // Update the actividad
        Actividad updatedActividad = actividadRepository.findById(actividad.getId()).get();
        // Disconnect from session so that the updates on updatedActividad are not directly saved in db
        em.detach(updatedActividad);
        updatedActividad
            .descripcion(UPDATED_DESCRIPCION)
            .estatus(UPDATED_ESTATUS)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .diasRetraso(UPDATED_DIAS_RETRASO);

        restActividadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActividad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedActividad))
            )
            .andExpect(status().isOk());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
        Actividad testActividad = actividadList.get(actividadList.size() - 1);
        assertThat(testActividad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testActividad.getEstatus()).isEqualTo(UPDATED_ESTATUS);
        assertThat(testActividad.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testActividad.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testActividad.getDiasRetraso()).isEqualTo(UPDATED_DIAS_RETRASO);
    }

    @Test
    @Transactional
    void putNonExistingActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actividad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actividad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actividad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actividad)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActividadWithPatch() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();

        // Update the actividad using partial update
        Actividad partialUpdatedActividad = new Actividad();
        partialUpdatedActividad.setId(actividad.getId());

        partialUpdatedActividad.fechaInicio(UPDATED_FECHA_INICIO);

        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActividad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActividad))
            )
            .andExpect(status().isOk());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
        Actividad testActividad = actividadList.get(actividadList.size() - 1);
        assertThat(testActividad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testActividad.getEstatus()).isEqualTo(DEFAULT_ESTATUS);
        assertThat(testActividad.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testActividad.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testActividad.getDiasRetraso()).isEqualTo(DEFAULT_DIAS_RETRASO);
    }

    @Test
    @Transactional
    void fullUpdateActividadWithPatch() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();

        // Update the actividad using partial update
        Actividad partialUpdatedActividad = new Actividad();
        partialUpdatedActividad.setId(actividad.getId());

        partialUpdatedActividad
            .descripcion(UPDATED_DESCRIPCION)
            .estatus(UPDATED_ESTATUS)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .diasRetraso(UPDATED_DIAS_RETRASO);

        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActividad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActividad))
            )
            .andExpect(status().isOk());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
        Actividad testActividad = actividadList.get(actividadList.size() - 1);
        assertThat(testActividad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testActividad.getEstatus()).isEqualTo(UPDATED_ESTATUS);
        assertThat(testActividad.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testActividad.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testActividad.getDiasRetraso()).isEqualTo(UPDATED_DIAS_RETRASO);
    }

    @Test
    @Transactional
    void patchNonExistingActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actividad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actividad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actividad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActividad() throws Exception {
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();
        actividad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActividadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(actividad))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actividad in the database
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        int databaseSizeBeforeDelete = actividadRepository.findAll().size();

        // Delete the actividad
        restActividadMockMvc
            .perform(delete(ENTITY_API_URL_ID, actividad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Actividad> actividadList = actividadRepository.findAll();
        assertThat(actividadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
