package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Soutenance;
import com.esi.gnote.domain.enumeration.TypeSoutenance;
import com.esi.gnote.repository.SoutenanceRepository;
import com.esi.gnote.service.SoutenanceService;
import com.esi.gnote.service.dto.SoutenanceDTO;
import com.esi.gnote.service.mapper.SoutenanceMapper;
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
 * Integration tests for the {@link SoutenanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SoutenanceResourceIT {

    private static final TypeSoutenance DEFAULT_TYPE_SOUT = TypeSoutenance.PFE;
    private static final TypeSoutenance UPDATED_TYPE_SOUT = TypeSoutenance.UP_PRO;

    private static final String DEFAULT_THEME_SOUT = "AAAAAAAAAA";
    private static final String UPDATED_THEME_SOUT = "BBBBBBBBBB";

    private static final Double DEFAULT_NOTE_SOUT = 1D;
    private static final Double UPDATED_NOTE_SOUT = 2D;

    private static final String ENTITY_API_URL = "/api/soutenances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SoutenanceRepository soutenanceRepository;

    @Mock
    private SoutenanceRepository soutenanceRepositoryMock;

    @Autowired
    private SoutenanceMapper soutenanceMapper;

    @Mock
    private SoutenanceService soutenanceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSoutenanceMockMvc;

    private Soutenance soutenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Soutenance createEntity(EntityManager em) {
        Soutenance soutenance = new Soutenance().typeSout(DEFAULT_TYPE_SOUT).themeSout(DEFAULT_THEME_SOUT).noteSout(DEFAULT_NOTE_SOUT);
        return soutenance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Soutenance createUpdatedEntity(EntityManager em) {
        Soutenance soutenance = new Soutenance().typeSout(UPDATED_TYPE_SOUT).themeSout(UPDATED_THEME_SOUT).noteSout(UPDATED_NOTE_SOUT);
        return soutenance;
    }

    @BeforeEach
    public void initTest() {
        soutenance = createEntity(em);
    }

    @Test
    @Transactional
    void createSoutenance() throws Exception {
        int databaseSizeBeforeCreate = soutenanceRepository.findAll().size();
        // Create the Soutenance
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);
        restSoutenanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soutenanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeCreate + 1);
        Soutenance testSoutenance = soutenanceList.get(soutenanceList.size() - 1);
        assertThat(testSoutenance.getTypeSout()).isEqualTo(DEFAULT_TYPE_SOUT);
        assertThat(testSoutenance.getThemeSout()).isEqualTo(DEFAULT_THEME_SOUT);
        assertThat(testSoutenance.getNoteSout()).isEqualTo(DEFAULT_NOTE_SOUT);
    }

    @Test
    @Transactional
    void createSoutenanceWithExistingId() throws Exception {
        // Create the Soutenance with an existing ID
        soutenance.setId(1L);
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        int databaseSizeBeforeCreate = soutenanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoutenanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soutenanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeSoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = soutenanceRepository.findAll().size();
        // set the field null
        soutenance.setTypeSout(null);

        // Create the Soutenance, which fails.
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        restSoutenanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soutenanceDTO)))
            .andExpect(status().isBadRequest());

        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkThemeSoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = soutenanceRepository.findAll().size();
        // set the field null
        soutenance.setThemeSout(null);

        // Create the Soutenance, which fails.
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        restSoutenanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soutenanceDTO)))
            .andExpect(status().isBadRequest());

        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoteSoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = soutenanceRepository.findAll().size();
        // set the field null
        soutenance.setNoteSout(null);

        // Create the Soutenance, which fails.
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        restSoutenanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soutenanceDTO)))
            .andExpect(status().isBadRequest());

        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSoutenances() throws Exception {
        // Initialize the database
        soutenanceRepository.saveAndFlush(soutenance);

        // Get all the soutenanceList
        restSoutenanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(soutenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeSout").value(hasItem(DEFAULT_TYPE_SOUT.toString())))
            .andExpect(jsonPath("$.[*].themeSout").value(hasItem(DEFAULT_THEME_SOUT)))
            .andExpect(jsonPath("$.[*].noteSout").value(hasItem(DEFAULT_NOTE_SOUT.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSoutenancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(soutenanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSoutenanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(soutenanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSoutenancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(soutenanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSoutenanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(soutenanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSoutenance() throws Exception {
        // Initialize the database
        soutenanceRepository.saveAndFlush(soutenance);

        // Get the soutenance
        restSoutenanceMockMvc
            .perform(get(ENTITY_API_URL_ID, soutenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(soutenance.getId().intValue()))
            .andExpect(jsonPath("$.typeSout").value(DEFAULT_TYPE_SOUT.toString()))
            .andExpect(jsonPath("$.themeSout").value(DEFAULT_THEME_SOUT))
            .andExpect(jsonPath("$.noteSout").value(DEFAULT_NOTE_SOUT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingSoutenance() throws Exception {
        // Get the soutenance
        restSoutenanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSoutenance() throws Exception {
        // Initialize the database
        soutenanceRepository.saveAndFlush(soutenance);

        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();

        // Update the soutenance
        Soutenance updatedSoutenance = soutenanceRepository.findById(soutenance.getId()).get();
        // Disconnect from session so that the updates on updatedSoutenance are not directly saved in db
        em.detach(updatedSoutenance);
        updatedSoutenance.typeSout(UPDATED_TYPE_SOUT).themeSout(UPDATED_THEME_SOUT).noteSout(UPDATED_NOTE_SOUT);
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(updatedSoutenance);

        restSoutenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, soutenanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soutenanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
        Soutenance testSoutenance = soutenanceList.get(soutenanceList.size() - 1);
        assertThat(testSoutenance.getTypeSout()).isEqualTo(UPDATED_TYPE_SOUT);
        assertThat(testSoutenance.getThemeSout()).isEqualTo(UPDATED_THEME_SOUT);
        assertThat(testSoutenance.getNoteSout()).isEqualTo(UPDATED_NOTE_SOUT);
    }

    @Test
    @Transactional
    void putNonExistingSoutenance() throws Exception {
        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();
        soutenance.setId(count.incrementAndGet());

        // Create the Soutenance
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoutenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, soutenanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soutenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSoutenance() throws Exception {
        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();
        soutenance.setId(count.incrementAndGet());

        // Create the Soutenance
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoutenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soutenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSoutenance() throws Exception {
        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();
        soutenance.setId(count.incrementAndGet());

        // Create the Soutenance
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoutenanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soutenanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSoutenanceWithPatch() throws Exception {
        // Initialize the database
        soutenanceRepository.saveAndFlush(soutenance);

        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();

        // Update the soutenance using partial update
        Soutenance partialUpdatedSoutenance = new Soutenance();
        partialUpdatedSoutenance.setId(soutenance.getId());

        partialUpdatedSoutenance.typeSout(UPDATED_TYPE_SOUT).noteSout(UPDATED_NOTE_SOUT);

        restSoutenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoutenance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoutenance))
            )
            .andExpect(status().isOk());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
        Soutenance testSoutenance = soutenanceList.get(soutenanceList.size() - 1);
        assertThat(testSoutenance.getTypeSout()).isEqualTo(UPDATED_TYPE_SOUT);
        assertThat(testSoutenance.getThemeSout()).isEqualTo(DEFAULT_THEME_SOUT);
        assertThat(testSoutenance.getNoteSout()).isEqualTo(UPDATED_NOTE_SOUT);
    }

    @Test
    @Transactional
    void fullUpdateSoutenanceWithPatch() throws Exception {
        // Initialize the database
        soutenanceRepository.saveAndFlush(soutenance);

        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();

        // Update the soutenance using partial update
        Soutenance partialUpdatedSoutenance = new Soutenance();
        partialUpdatedSoutenance.setId(soutenance.getId());

        partialUpdatedSoutenance.typeSout(UPDATED_TYPE_SOUT).themeSout(UPDATED_THEME_SOUT).noteSout(UPDATED_NOTE_SOUT);

        restSoutenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoutenance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoutenance))
            )
            .andExpect(status().isOk());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
        Soutenance testSoutenance = soutenanceList.get(soutenanceList.size() - 1);
        assertThat(testSoutenance.getTypeSout()).isEqualTo(UPDATED_TYPE_SOUT);
        assertThat(testSoutenance.getThemeSout()).isEqualTo(UPDATED_THEME_SOUT);
        assertThat(testSoutenance.getNoteSout()).isEqualTo(UPDATED_NOTE_SOUT);
    }

    @Test
    @Transactional
    void patchNonExistingSoutenance() throws Exception {
        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();
        soutenance.setId(count.incrementAndGet());

        // Create the Soutenance
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoutenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, soutenanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soutenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSoutenance() throws Exception {
        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();
        soutenance.setId(count.incrementAndGet());

        // Create the Soutenance
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoutenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soutenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSoutenance() throws Exception {
        int databaseSizeBeforeUpdate = soutenanceRepository.findAll().size();
        soutenance.setId(count.incrementAndGet());

        // Create the Soutenance
        SoutenanceDTO soutenanceDTO = soutenanceMapper.toDto(soutenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoutenanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(soutenanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Soutenance in the database
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSoutenance() throws Exception {
        // Initialize the database
        soutenanceRepository.saveAndFlush(soutenance);

        int databaseSizeBeforeDelete = soutenanceRepository.findAll().size();

        // Delete the soutenance
        restSoutenanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, soutenance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Soutenance> soutenanceList = soutenanceRepository.findAll();
        assertThat(soutenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
