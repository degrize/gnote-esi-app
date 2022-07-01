package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Horaire;
import com.esi.gnote.repository.HoraireRepository;
import com.esi.gnote.service.dto.HoraireDTO;
import com.esi.gnote.service.mapper.HoraireMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HoraireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HoraireResourceIT {

    private static final LocalDate DEFAULT_DATE_SOUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SOUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_EFFET = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EFFET = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/horaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HoraireRepository horaireRepository;

    @Autowired
    private HoraireMapper horaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHoraireMockMvc;

    private Horaire horaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horaire createEntity(EntityManager em) {
        Horaire horaire = new Horaire().dateSout(DEFAULT_DATE_SOUT).dateEffet(DEFAULT_DATE_EFFET);
        return horaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horaire createUpdatedEntity(EntityManager em) {
        Horaire horaire = new Horaire().dateSout(UPDATED_DATE_SOUT).dateEffet(UPDATED_DATE_EFFET);
        return horaire;
    }

    @BeforeEach
    public void initTest() {
        horaire = createEntity(em);
    }

    @Test
    @Transactional
    void createHoraire() throws Exception {
        int databaseSizeBeforeCreate = horaireRepository.findAll().size();
        // Create the Horaire
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);
        restHoraireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaireDTO)))
            .andExpect(status().isCreated());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeCreate + 1);
        Horaire testHoraire = horaireList.get(horaireList.size() - 1);
        assertThat(testHoraire.getDateSout()).isEqualTo(DEFAULT_DATE_SOUT);
        assertThat(testHoraire.getDateEffet()).isEqualTo(DEFAULT_DATE_EFFET);
    }

    @Test
    @Transactional
    void createHoraireWithExistingId() throws Exception {
        // Create the Horaire with an existing ID
        horaire.setId(1L);
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);

        int databaseSizeBeforeCreate = horaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoraireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateSoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = horaireRepository.findAll().size();
        // set the field null
        horaire.setDateSout(null);

        // Create the Horaire, which fails.
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);

        restHoraireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaireDTO)))
            .andExpect(status().isBadRequest());

        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHoraires() throws Exception {
        // Initialize the database
        horaireRepository.saveAndFlush(horaire);

        // Get all the horaireList
        restHoraireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateSout").value(hasItem(DEFAULT_DATE_SOUT.toString())))
            .andExpect(jsonPath("$.[*].dateEffet").value(hasItem(DEFAULT_DATE_EFFET.toString())));
    }

    @Test
    @Transactional
    void getHoraire() throws Exception {
        // Initialize the database
        horaireRepository.saveAndFlush(horaire);

        // Get the horaire
        restHoraireMockMvc
            .perform(get(ENTITY_API_URL_ID, horaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(horaire.getId().intValue()))
            .andExpect(jsonPath("$.dateSout").value(DEFAULT_DATE_SOUT.toString()))
            .andExpect(jsonPath("$.dateEffet").value(DEFAULT_DATE_EFFET.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHoraire() throws Exception {
        // Get the horaire
        restHoraireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHoraire() throws Exception {
        // Initialize the database
        horaireRepository.saveAndFlush(horaire);

        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();

        // Update the horaire
        Horaire updatedHoraire = horaireRepository.findById(horaire.getId()).get();
        // Disconnect from session so that the updates on updatedHoraire are not directly saved in db
        em.detach(updatedHoraire);
        updatedHoraire.dateSout(UPDATED_DATE_SOUT).dateEffet(UPDATED_DATE_EFFET);
        HoraireDTO horaireDTO = horaireMapper.toDto(updatedHoraire);

        restHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
        Horaire testHoraire = horaireList.get(horaireList.size() - 1);
        assertThat(testHoraire.getDateSout()).isEqualTo(UPDATED_DATE_SOUT);
        assertThat(testHoraire.getDateEffet()).isEqualTo(UPDATED_DATE_EFFET);
    }

    @Test
    @Transactional
    void putNonExistingHoraire() throws Exception {
        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();
        horaire.setId(count.incrementAndGet());

        // Create the Horaire
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHoraire() throws Exception {
        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();
        horaire.setId(count.incrementAndGet());

        // Create the Horaire
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHoraire() throws Exception {
        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();
        horaire.setId(count.incrementAndGet());

        // Create the Horaire
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHoraireWithPatch() throws Exception {
        // Initialize the database
        horaireRepository.saveAndFlush(horaire);

        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();

        // Update the horaire using partial update
        Horaire partialUpdatedHoraire = new Horaire();
        partialUpdatedHoraire.setId(horaire.getId());

        partialUpdatedHoraire.dateSout(UPDATED_DATE_SOUT);

        restHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoraire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoraire))
            )
            .andExpect(status().isOk());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
        Horaire testHoraire = horaireList.get(horaireList.size() - 1);
        assertThat(testHoraire.getDateSout()).isEqualTo(UPDATED_DATE_SOUT);
        assertThat(testHoraire.getDateEffet()).isEqualTo(DEFAULT_DATE_EFFET);
    }

    @Test
    @Transactional
    void fullUpdateHoraireWithPatch() throws Exception {
        // Initialize the database
        horaireRepository.saveAndFlush(horaire);

        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();

        // Update the horaire using partial update
        Horaire partialUpdatedHoraire = new Horaire();
        partialUpdatedHoraire.setId(horaire.getId());

        partialUpdatedHoraire.dateSout(UPDATED_DATE_SOUT).dateEffet(UPDATED_DATE_EFFET);

        restHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoraire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoraire))
            )
            .andExpect(status().isOk());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
        Horaire testHoraire = horaireList.get(horaireList.size() - 1);
        assertThat(testHoraire.getDateSout()).isEqualTo(UPDATED_DATE_SOUT);
        assertThat(testHoraire.getDateEffet()).isEqualTo(UPDATED_DATE_EFFET);
    }

    @Test
    @Transactional
    void patchNonExistingHoraire() throws Exception {
        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();
        horaire.setId(count.incrementAndGet());

        // Create the Horaire
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, horaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHoraire() throws Exception {
        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();
        horaire.setId(count.incrementAndGet());

        // Create the Horaire
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHoraire() throws Exception {
        int databaseSizeBeforeUpdate = horaireRepository.findAll().size();
        horaire.setId(count.incrementAndGet());

        // Create the Horaire
        HoraireDTO horaireDTO = horaireMapper.toDto(horaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(horaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Horaire in the database
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHoraire() throws Exception {
        // Initialize the database
        horaireRepository.saveAndFlush(horaire);

        int databaseSizeBeforeDelete = horaireRepository.findAll().size();

        // Delete the horaire
        restHoraireMockMvc
            .perform(delete(ENTITY_API_URL_ID, horaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Horaire> horaireList = horaireRepository.findAll();
        assertThat(horaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
