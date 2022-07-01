package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Salle;
import com.esi.gnote.repository.SalleRepository;
import com.esi.gnote.service.SalleService;
import com.esi.gnote.service.dto.SalleDTO;
import com.esi.gnote.service.mapper.SalleMapper;
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
 * Integration tests for the {@link SalleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SalleResourceIT {

    private static final String DEFAULT_NUMERO_SALLE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SALLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBRE_PLACE = 1;
    private static final Integer UPDATED_NBRE_PLACE = 2;

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/salles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalleRepository salleRepository;

    @Mock
    private SalleRepository salleRepositoryMock;

    @Autowired
    private SalleMapper salleMapper;

    @Mock
    private SalleService salleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalleMockMvc;

    private Salle salle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salle createEntity(EntityManager em) {
        Salle salle = new Salle().numeroSalle(DEFAULT_NUMERO_SALLE).nbrePlace(DEFAULT_NBRE_PLACE).etat(DEFAULT_ETAT);
        return salle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salle createUpdatedEntity(EntityManager em) {
        Salle salle = new Salle().numeroSalle(UPDATED_NUMERO_SALLE).nbrePlace(UPDATED_NBRE_PLACE).etat(UPDATED_ETAT);
        return salle;
    }

    @BeforeEach
    public void initTest() {
        salle = createEntity(em);
    }

    @Test
    @Transactional
    void createSalle() throws Exception {
        int databaseSizeBeforeCreate = salleRepository.findAll().size();
        // Create the Salle
        SalleDTO salleDTO = salleMapper.toDto(salle);
        restSalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salleDTO)))
            .andExpect(status().isCreated());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeCreate + 1);
        Salle testSalle = salleList.get(salleList.size() - 1);
        assertThat(testSalle.getNumeroSalle()).isEqualTo(DEFAULT_NUMERO_SALLE);
        assertThat(testSalle.getNbrePlace()).isEqualTo(DEFAULT_NBRE_PLACE);
        assertThat(testSalle.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void createSalleWithExistingId() throws Exception {
        // Create the Salle with an existing ID
        salle.setId(1L);
        SalleDTO salleDTO = salleMapper.toDto(salle);

        int databaseSizeBeforeCreate = salleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroSalleIsRequired() throws Exception {
        int databaseSizeBeforeTest = salleRepository.findAll().size();
        // set the field null
        salle.setNumeroSalle(null);

        // Create the Salle, which fails.
        SalleDTO salleDTO = salleMapper.toDto(salle);

        restSalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salleDTO)))
            .andExpect(status().isBadRequest());

        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalles() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);

        // Get all the salleList
        restSalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salle.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroSalle").value(hasItem(DEFAULT_NUMERO_SALLE)))
            .andExpect(jsonPath("$.[*].nbrePlace").value(hasItem(DEFAULT_NBRE_PLACE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSallesWithEagerRelationshipsIsEnabled() throws Exception {
        when(salleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(salleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSallesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(salleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(salleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSalle() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);

        // Get the salle
        restSalleMockMvc
            .perform(get(ENTITY_API_URL_ID, salle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salle.getId().intValue()))
            .andExpect(jsonPath("$.numeroSalle").value(DEFAULT_NUMERO_SALLE))
            .andExpect(jsonPath("$.nbrePlace").value(DEFAULT_NBRE_PLACE))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT));
    }

    @Test
    @Transactional
    void getNonExistingSalle() throws Exception {
        // Get the salle
        restSalleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSalle() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);

        int databaseSizeBeforeUpdate = salleRepository.findAll().size();

        // Update the salle
        Salle updatedSalle = salleRepository.findById(salle.getId()).get();
        // Disconnect from session so that the updates on updatedSalle are not directly saved in db
        em.detach(updatedSalle);
        updatedSalle.numeroSalle(UPDATED_NUMERO_SALLE).nbrePlace(UPDATED_NBRE_PLACE).etat(UPDATED_ETAT);
        SalleDTO salleDTO = salleMapper.toDto(updatedSalle);

        restSalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
        Salle testSalle = salleList.get(salleList.size() - 1);
        assertThat(testSalle.getNumeroSalle()).isEqualTo(UPDATED_NUMERO_SALLE);
        assertThat(testSalle.getNbrePlace()).isEqualTo(UPDATED_NBRE_PLACE);
        assertThat(testSalle.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void putNonExistingSalle() throws Exception {
        int databaseSizeBeforeUpdate = salleRepository.findAll().size();
        salle.setId(count.incrementAndGet());

        // Create the Salle
        SalleDTO salleDTO = salleMapper.toDto(salle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalle() throws Exception {
        int databaseSizeBeforeUpdate = salleRepository.findAll().size();
        salle.setId(count.incrementAndGet());

        // Create the Salle
        SalleDTO salleDTO = salleMapper.toDto(salle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalle() throws Exception {
        int databaseSizeBeforeUpdate = salleRepository.findAll().size();
        salle.setId(count.incrementAndGet());

        // Create the Salle
        SalleDTO salleDTO = salleMapper.toDto(salle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalleWithPatch() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);

        int databaseSizeBeforeUpdate = salleRepository.findAll().size();

        // Update the salle using partial update
        Salle partialUpdatedSalle = new Salle();
        partialUpdatedSalle.setId(salle.getId());

        restSalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalle))
            )
            .andExpect(status().isOk());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
        Salle testSalle = salleList.get(salleList.size() - 1);
        assertThat(testSalle.getNumeroSalle()).isEqualTo(DEFAULT_NUMERO_SALLE);
        assertThat(testSalle.getNbrePlace()).isEqualTo(DEFAULT_NBRE_PLACE);
        assertThat(testSalle.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void fullUpdateSalleWithPatch() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);

        int databaseSizeBeforeUpdate = salleRepository.findAll().size();

        // Update the salle using partial update
        Salle partialUpdatedSalle = new Salle();
        partialUpdatedSalle.setId(salle.getId());

        partialUpdatedSalle.numeroSalle(UPDATED_NUMERO_SALLE).nbrePlace(UPDATED_NBRE_PLACE).etat(UPDATED_ETAT);

        restSalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalle))
            )
            .andExpect(status().isOk());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
        Salle testSalle = salleList.get(salleList.size() - 1);
        assertThat(testSalle.getNumeroSalle()).isEqualTo(UPDATED_NUMERO_SALLE);
        assertThat(testSalle.getNbrePlace()).isEqualTo(UPDATED_NBRE_PLACE);
        assertThat(testSalle.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void patchNonExistingSalle() throws Exception {
        int databaseSizeBeforeUpdate = salleRepository.findAll().size();
        salle.setId(count.incrementAndGet());

        // Create the Salle
        SalleDTO salleDTO = salleMapper.toDto(salle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalle() throws Exception {
        int databaseSizeBeforeUpdate = salleRepository.findAll().size();
        salle.setId(count.incrementAndGet());

        // Create the Salle
        SalleDTO salleDTO = salleMapper.toDto(salle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalle() throws Exception {
        int databaseSizeBeforeUpdate = salleRepository.findAll().size();
        salle.setId(count.incrementAndGet());

        // Create the Salle
        SalleDTO salleDTO = salleMapper.toDto(salle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salle in the database
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalle() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);

        int databaseSizeBeforeDelete = salleRepository.findAll().size();

        // Delete the salle
        restSalleMockMvc
            .perform(delete(ENTITY_API_URL_ID, salle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Salle> salleList = salleRepository.findAll();
        assertThat(salleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
