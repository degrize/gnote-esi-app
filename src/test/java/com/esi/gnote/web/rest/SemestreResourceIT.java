package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Semestre;
import com.esi.gnote.repository.SemestreRepository;
import com.esi.gnote.service.SemestreService;
import com.esi.gnote.service.dto.SemestreDTO;
import com.esi.gnote.service.mapper.SemestreMapper;
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
 * Integration tests for the {@link SemestreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SemestreResourceIT {

    private static final String DEFAULT_NOM_SEMESTRE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SEMESTRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/semestres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SemestreRepository semestreRepository;

    @Mock
    private SemestreRepository semestreRepositoryMock;

    @Autowired
    private SemestreMapper semestreMapper;

    @Mock
    private SemestreService semestreServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSemestreMockMvc;

    private Semestre semestre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semestre createEntity(EntityManager em) {
        Semestre semestre = new Semestre().nomSemestre(DEFAULT_NOM_SEMESTRE);
        return semestre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semestre createUpdatedEntity(EntityManager em) {
        Semestre semestre = new Semestre().nomSemestre(UPDATED_NOM_SEMESTRE);
        return semestre;
    }

    @BeforeEach
    public void initTest() {
        semestre = createEntity(em);
    }

    @Test
    @Transactional
    void createSemestre() throws Exception {
        int databaseSizeBeforeCreate = semestreRepository.findAll().size();
        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);
        restSemestreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semestreDTO)))
            .andExpect(status().isCreated());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeCreate + 1);
        Semestre testSemestre = semestreList.get(semestreList.size() - 1);
        assertThat(testSemestre.getNomSemestre()).isEqualTo(DEFAULT_NOM_SEMESTRE);
    }

    @Test
    @Transactional
    void createSemestreWithExistingId() throws Exception {
        // Create the Semestre with an existing ID
        semestre.setId(1L);
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        int databaseSizeBeforeCreate = semestreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemestreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semestreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomSemestreIsRequired() throws Exception {
        int databaseSizeBeforeTest = semestreRepository.findAll().size();
        // set the field null
        semestre.setNomSemestre(null);

        // Create the Semestre, which fails.
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        restSemestreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semestreDTO)))
            .andExpect(status().isBadRequest());

        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSemestres() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        // Get all the semestreList
        restSemestreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semestre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSemestre").value(hasItem(DEFAULT_NOM_SEMESTRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSemestresWithEagerRelationshipsIsEnabled() throws Exception {
        when(semestreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSemestreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(semestreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSemestresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(semestreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSemestreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(semestreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSemestre() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        // Get the semestre
        restSemestreMockMvc
            .perform(get(ENTITY_API_URL_ID, semestre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semestre.getId().intValue()))
            .andExpect(jsonPath("$.nomSemestre").value(DEFAULT_NOM_SEMESTRE));
    }

    @Test
    @Transactional
    void getNonExistingSemestre() throws Exception {
        // Get the semestre
        restSemestreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSemestre() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();

        // Update the semestre
        Semestre updatedSemestre = semestreRepository.findById(semestre.getId()).get();
        // Disconnect from session so that the updates on updatedSemestre are not directly saved in db
        em.detach(updatedSemestre);
        updatedSemestre.nomSemestre(UPDATED_NOM_SEMESTRE);
        SemestreDTO semestreDTO = semestreMapper.toDto(updatedSemestre);

        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semestreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semestreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
        Semestre testSemestre = semestreList.get(semestreList.size() - 1);
        assertThat(testSemestre.getNomSemestre()).isEqualTo(UPDATED_NOM_SEMESTRE);
    }

    @Test
    @Transactional
    void putNonExistingSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(count.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semestreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semestreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(count.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semestreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(count.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semestreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSemestreWithPatch() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();

        // Update the semestre using partial update
        Semestre partialUpdatedSemestre = new Semestre();
        partialUpdatedSemestre.setId(semestre.getId());

        partialUpdatedSemestre.nomSemestre(UPDATED_NOM_SEMESTRE);

        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemestre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSemestre))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
        Semestre testSemestre = semestreList.get(semestreList.size() - 1);
        assertThat(testSemestre.getNomSemestre()).isEqualTo(UPDATED_NOM_SEMESTRE);
    }

    @Test
    @Transactional
    void fullUpdateSemestreWithPatch() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();

        // Update the semestre using partial update
        Semestre partialUpdatedSemestre = new Semestre();
        partialUpdatedSemestre.setId(semestre.getId());

        partialUpdatedSemestre.nomSemestre(UPDATED_NOM_SEMESTRE);

        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemestre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSemestre))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
        Semestre testSemestre = semestreList.get(semestreList.size() - 1);
        assertThat(testSemestre.getNomSemestre()).isEqualTo(UPDATED_NOM_SEMESTRE);
    }

    @Test
    @Transactional
    void patchNonExistingSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(count.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, semestreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(semestreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(count.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(semestreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(count.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(semestreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSemestre() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        int databaseSizeBeforeDelete = semestreRepository.findAll().size();

        // Delete the semestre
        restSemestreMockMvc
            .perform(delete(ENTITY_API_URL_ID, semestre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
