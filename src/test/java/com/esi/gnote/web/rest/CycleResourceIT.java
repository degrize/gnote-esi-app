package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Cycle;
import com.esi.gnote.domain.enumeration.TypeCycle;
import com.esi.gnote.repository.CycleRepository;
import com.esi.gnote.service.dto.CycleDTO;
import com.esi.gnote.service.mapper.CycleMapper;
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
 * Integration tests for the {@link CycleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CycleResourceIT {

    private static final TypeCycle DEFAULT_NOM_CYCLE = TypeCycle.TS;
    private static final TypeCycle UPDATED_NOM_CYCLE = TypeCycle.ING;

    private static final String ENTITY_API_URL = "/api/cycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private CycleMapper cycleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCycleMockMvc;

    private Cycle cycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycle createEntity(EntityManager em) {
        Cycle cycle = new Cycle().nomCycle(DEFAULT_NOM_CYCLE);
        return cycle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycle createUpdatedEntity(EntityManager em) {
        Cycle cycle = new Cycle().nomCycle(UPDATED_NOM_CYCLE);
        return cycle;
    }

    @BeforeEach
    public void initTest() {
        cycle = createEntity(em);
    }

    @Test
    @Transactional
    void createCycle() throws Exception {
        int databaseSizeBeforeCreate = cycleRepository.findAll().size();
        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);
        restCycleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cycleDTO)))
            .andExpect(status().isCreated());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeCreate + 1);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getNomCycle()).isEqualTo(DEFAULT_NOM_CYCLE);
    }

    @Test
    @Transactional
    void createCycleWithExistingId() throws Exception {
        // Create the Cycle with an existing ID
        cycle.setId(1L);
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        int databaseSizeBeforeCreate = cycleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCycleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cycleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomCycleIsRequired() throws Exception {
        int databaseSizeBeforeTest = cycleRepository.findAll().size();
        // set the field null
        cycle.setNomCycle(null);

        // Create the Cycle, which fails.
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        restCycleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cycleDTO)))
            .andExpect(status().isBadRequest());

        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCycles() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList
        restCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCycle").value(hasItem(DEFAULT_NOM_CYCLE.toString())));
    }

    @Test
    @Transactional
    void getCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        // Get the cycle
        restCycleMockMvc
            .perform(get(ENTITY_API_URL_ID, cycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cycle.getId().intValue()))
            .andExpect(jsonPath("$.nomCycle").value(DEFAULT_NOM_CYCLE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCycle() throws Exception {
        // Get the cycle
        restCycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle
        Cycle updatedCycle = cycleRepository.findById(cycle.getId()).get();
        // Disconnect from session so that the updates on updatedCycle are not directly saved in db
        em.detach(updatedCycle);
        updatedCycle.nomCycle(UPDATED_NOM_CYCLE);
        CycleDTO cycleDTO = cycleMapper.toDto(updatedCycle);

        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cycleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getNomCycle()).isEqualTo(UPDATED_NOM_CYCLE);
    }

    @Test
    @Transactional
    void putNonExistingCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(count.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cycleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(count.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(count.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cycleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCycleWithPatch() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle using partial update
        Cycle partialUpdatedCycle = new Cycle();
        partialUpdatedCycle.setId(cycle.getId());

        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getNomCycle()).isEqualTo(DEFAULT_NOM_CYCLE);
    }

    @Test
    @Transactional
    void fullUpdateCycleWithPatch() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle using partial update
        Cycle partialUpdatedCycle = new Cycle();
        partialUpdatedCycle.setId(cycle.getId());

        partialUpdatedCycle.nomCycle(UPDATED_NOM_CYCLE);

        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getNomCycle()).isEqualTo(UPDATED_NOM_CYCLE);
    }

    @Test
    @Transactional
    void patchNonExistingCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(count.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cycleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(count.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(count.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cycleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        int databaseSizeBeforeDelete = cycleRepository.findAll().size();

        // Delete the cycle
        restCycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cycle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
