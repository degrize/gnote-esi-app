package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.AnneeScolaire;
import com.esi.gnote.repository.AnneeScolaireRepository;
import com.esi.gnote.service.dto.AnneeScolaireDTO;
import com.esi.gnote.service.mapper.AnneeScolaireMapper;
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
 * Integration tests for the {@link AnneeScolaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnneeScolaireResourceIT {

    private static final String DEFAULT_PERIODE = "AAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/annee-scolaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnneeScolaireRepository anneeScolaireRepository;

    @Autowired
    private AnneeScolaireMapper anneeScolaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnneeScolaireMockMvc;

    private AnneeScolaire anneeScolaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeScolaire createEntity(EntityManager em) {
        AnneeScolaire anneeScolaire = new AnneeScolaire().periode(DEFAULT_PERIODE).dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
        return anneeScolaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeScolaire createUpdatedEntity(EntityManager em) {
        AnneeScolaire anneeScolaire = new AnneeScolaire().periode(UPDATED_PERIODE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        return anneeScolaire;
    }

    @BeforeEach
    public void initTest() {
        anneeScolaire = createEntity(em);
    }

    @Test
    @Transactional
    void createAnneeScolaire() throws Exception {
        int databaseSizeBeforeCreate = anneeScolaireRepository.findAll().size();
        // Create the AnneeScolaire
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);
        restAnneeScolaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeCreate + 1);
        AnneeScolaire testAnneeScolaire = anneeScolaireList.get(anneeScolaireList.size() - 1);
        assertThat(testAnneeScolaire.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testAnneeScolaire.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAnneeScolaire.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createAnneeScolaireWithExistingId() throws Exception {
        // Create the AnneeScolaire with an existing ID
        anneeScolaire.setId(1L);
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        int databaseSizeBeforeCreate = anneeScolaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnneeScolaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPeriodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeScolaireRepository.findAll().size();
        // set the field null
        anneeScolaire.setPeriode(null);

        // Create the AnneeScolaire, which fails.
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        restAnneeScolaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeScolaireRepository.findAll().size();
        // set the field null
        anneeScolaire.setDateDebut(null);

        // Create the AnneeScolaire, which fails.
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        restAnneeScolaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnneeScolaires() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        // Get all the anneeScolaireList
        restAnneeScolaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anneeScolaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        // Get the anneeScolaire
        restAnneeScolaireMockMvc
            .perform(get(ENTITY_API_URL_ID, anneeScolaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(anneeScolaire.getId().intValue()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAnneeScolaire() throws Exception {
        // Get the anneeScolaire
        restAnneeScolaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();

        // Update the anneeScolaire
        AnneeScolaire updatedAnneeScolaire = anneeScolaireRepository.findById(anneeScolaire.getId()).get();
        // Disconnect from session so that the updates on updatedAnneeScolaire are not directly saved in db
        em.detach(updatedAnneeScolaire);
        updatedAnneeScolaire.periode(UPDATED_PERIODE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(updatedAnneeScolaire);

        restAnneeScolaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anneeScolaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
        AnneeScolaire testAnneeScolaire = anneeScolaireList.get(anneeScolaireList.size() - 1);
        assertThat(testAnneeScolaire.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testAnneeScolaire.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAnneeScolaire.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingAnneeScolaire() throws Exception {
        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();
        anneeScolaire.setId(count.incrementAndGet());

        // Create the AnneeScolaire
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeScolaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anneeScolaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnneeScolaire() throws Exception {
        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();
        anneeScolaire.setId(count.incrementAndGet());

        // Create the AnneeScolaire
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeScolaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnneeScolaire() throws Exception {
        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();
        anneeScolaire.setId(count.incrementAndGet());

        // Create the AnneeScolaire
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeScolaireMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnneeScolaireWithPatch() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();

        // Update the anneeScolaire using partial update
        AnneeScolaire partialUpdatedAnneeScolaire = new AnneeScolaire();
        partialUpdatedAnneeScolaire.setId(anneeScolaire.getId());

        partialUpdatedAnneeScolaire.periode(UPDATED_PERIODE).dateFin(UPDATED_DATE_FIN);

        restAnneeScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnneeScolaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnneeScolaire))
            )
            .andExpect(status().isOk());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
        AnneeScolaire testAnneeScolaire = anneeScolaireList.get(anneeScolaireList.size() - 1);
        assertThat(testAnneeScolaire.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testAnneeScolaire.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAnneeScolaire.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateAnneeScolaireWithPatch() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();

        // Update the anneeScolaire using partial update
        AnneeScolaire partialUpdatedAnneeScolaire = new AnneeScolaire();
        partialUpdatedAnneeScolaire.setId(anneeScolaire.getId());

        partialUpdatedAnneeScolaire.periode(UPDATED_PERIODE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restAnneeScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnneeScolaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnneeScolaire))
            )
            .andExpect(status().isOk());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
        AnneeScolaire testAnneeScolaire = anneeScolaireList.get(anneeScolaireList.size() - 1);
        assertThat(testAnneeScolaire.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testAnneeScolaire.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAnneeScolaire.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingAnneeScolaire() throws Exception {
        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();
        anneeScolaire.setId(count.incrementAndGet());

        // Create the AnneeScolaire
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, anneeScolaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnneeScolaire() throws Exception {
        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();
        anneeScolaire.setId(count.incrementAndGet());

        // Create the AnneeScolaire
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnneeScolaire() throws Exception {
        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();
        anneeScolaire.setId(count.incrementAndGet());

        // Create the AnneeScolaire
        AnneeScolaireDTO anneeScolaireDTO = anneeScolaireMapper.toDto(anneeScolaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeScolaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeScolaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        int databaseSizeBeforeDelete = anneeScolaireRepository.findAll().size();

        // Delete the anneeScolaire
        restAnneeScolaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, anneeScolaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
