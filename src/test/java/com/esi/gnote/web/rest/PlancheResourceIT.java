package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Planche;
import com.esi.gnote.repository.PlancheRepository;
import com.esi.gnote.service.PlancheService;
import com.esi.gnote.service.dto.PlancheDTO;
import com.esi.gnote.service.mapper.PlancheMapper;
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
 * Integration tests for the {@link PlancheResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlancheResourceIT {

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/planches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlancheRepository plancheRepository;

    @Mock
    private PlancheRepository plancheRepositoryMock;

    @Autowired
    private PlancheMapper plancheMapper;

    @Mock
    private PlancheService plancheServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlancheMockMvc;

    private Planche planche;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planche createEntity(EntityManager em) {
        Planche planche = new Planche().observation(DEFAULT_OBSERVATION);
        return planche;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planche createUpdatedEntity(EntityManager em) {
        Planche planche = new Planche().observation(UPDATED_OBSERVATION);
        return planche;
    }

    @BeforeEach
    public void initTest() {
        planche = createEntity(em);
    }

    @Test
    @Transactional
    void createPlanche() throws Exception {
        int databaseSizeBeforeCreate = plancheRepository.findAll().size();
        // Create the Planche
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);
        restPlancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plancheDTO)))
            .andExpect(status().isCreated());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeCreate + 1);
        Planche testPlanche = plancheList.get(plancheList.size() - 1);
        assertThat(testPlanche.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void createPlancheWithExistingId() throws Exception {
        // Create the Planche with an existing ID
        planche.setId(1L);
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);

        int databaseSizeBeforeCreate = plancheRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plancheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkObservationIsRequired() throws Exception {
        int databaseSizeBeforeTest = plancheRepository.findAll().size();
        // set the field null
        planche.setObservation(null);

        // Create the Planche, which fails.
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);

        restPlancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plancheDTO)))
            .andExpect(status().isBadRequest());

        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlanches() throws Exception {
        // Initialize the database
        plancheRepository.saveAndFlush(planche);

        // Get all the plancheList
        restPlancheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planche.getId().intValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(plancheServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlancheMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(plancheServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(plancheServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlancheMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(plancheServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPlanche() throws Exception {
        // Initialize the database
        plancheRepository.saveAndFlush(planche);

        // Get the planche
        restPlancheMockMvc
            .perform(get(ENTITY_API_URL_ID, planche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planche.getId().intValue()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getNonExistingPlanche() throws Exception {
        // Get the planche
        restPlancheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlanche() throws Exception {
        // Initialize the database
        plancheRepository.saveAndFlush(planche);

        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();

        // Update the planche
        Planche updatedPlanche = plancheRepository.findById(planche.getId()).get();
        // Disconnect from session so that the updates on updatedPlanche are not directly saved in db
        em.detach(updatedPlanche);
        updatedPlanche.observation(UPDATED_OBSERVATION);
        PlancheDTO plancheDTO = plancheMapper.toDto(updatedPlanche);

        restPlancheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plancheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plancheDTO))
            )
            .andExpect(status().isOk());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
        Planche testPlanche = plancheList.get(plancheList.size() - 1);
        assertThat(testPlanche.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void putNonExistingPlanche() throws Exception {
        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();
        planche.setId(count.incrementAndGet());

        // Create the Planche
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlancheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plancheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plancheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanche() throws Exception {
        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();
        planche.setId(count.incrementAndGet());

        // Create the Planche
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlancheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plancheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanche() throws Exception {
        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();
        planche.setId(count.incrementAndGet());

        // Create the Planche
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlancheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plancheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlancheWithPatch() throws Exception {
        // Initialize the database
        plancheRepository.saveAndFlush(planche);

        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();

        // Update the planche using partial update
        Planche partialUpdatedPlanche = new Planche();
        partialUpdatedPlanche.setId(planche.getId());

        restPlancheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanche.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanche))
            )
            .andExpect(status().isOk());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
        Planche testPlanche = plancheList.get(plancheList.size() - 1);
        assertThat(testPlanche.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdatePlancheWithPatch() throws Exception {
        // Initialize the database
        plancheRepository.saveAndFlush(planche);

        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();

        // Update the planche using partial update
        Planche partialUpdatedPlanche = new Planche();
        partialUpdatedPlanche.setId(planche.getId());

        partialUpdatedPlanche.observation(UPDATED_OBSERVATION);

        restPlancheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanche.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanche))
            )
            .andExpect(status().isOk());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
        Planche testPlanche = plancheList.get(plancheList.size() - 1);
        assertThat(testPlanche.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingPlanche() throws Exception {
        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();
        planche.setId(count.incrementAndGet());

        // Create the Planche
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlancheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plancheDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plancheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanche() throws Exception {
        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();
        planche.setId(count.incrementAndGet());

        // Create the Planche
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlancheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plancheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanche() throws Exception {
        int databaseSizeBeforeUpdate = plancheRepository.findAll().size();
        planche.setId(count.incrementAndGet());

        // Create the Planche
        PlancheDTO plancheDTO = plancheMapper.toDto(planche);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlancheMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plancheDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planche in the database
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanche() throws Exception {
        // Initialize the database
        plancheRepository.saveAndFlush(planche);

        int databaseSizeBeforeDelete = plancheRepository.findAll().size();

        // Delete the planche
        restPlancheMockMvc
            .perform(delete(ENTITY_API_URL_ID, planche.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Planche> plancheList = plancheRepository.findAll();
        assertThat(plancheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
