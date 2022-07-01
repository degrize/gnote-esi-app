package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.DemandeInspecteurDE;
import com.esi.gnote.repository.DemandeInspecteurDERepository;
import com.esi.gnote.service.DemandeInspecteurDEService;
import com.esi.gnote.service.dto.DemandeInspecteurDEDTO;
import com.esi.gnote.service.mapper.DemandeInspecteurDEMapper;
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
 * Integration tests for the {@link DemandeInspecteurDEResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DemandeInspecteurDEResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/demande-inspecteur-des";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeInspecteurDERepository demandeInspecteurDERepository;

    @Mock
    private DemandeInspecteurDERepository demandeInspecteurDERepositoryMock;

    @Autowired
    private DemandeInspecteurDEMapper demandeInspecteurDEMapper;

    @Mock
    private DemandeInspecteurDEService demandeInspecteurDEServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeInspecteurDEMockMvc;

    private DemandeInspecteurDE demandeInspecteurDE;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeInspecteurDE createEntity(EntityManager em) {
        DemandeInspecteurDE demandeInspecteurDE = new DemandeInspecteurDE().message(DEFAULT_MESSAGE);
        return demandeInspecteurDE;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeInspecteurDE createUpdatedEntity(EntityManager em) {
        DemandeInspecteurDE demandeInspecteurDE = new DemandeInspecteurDE().message(UPDATED_MESSAGE);
        return demandeInspecteurDE;
    }

    @BeforeEach
    public void initTest() {
        demandeInspecteurDE = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeInspecteurDE() throws Exception {
        int databaseSizeBeforeCreate = demandeInspecteurDERepository.findAll().size();
        // Create the DemandeInspecteurDE
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);
        restDemandeInspecteurDEMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeInspecteurDE testDemandeInspecteurDE = demandeInspecteurDEList.get(demandeInspecteurDEList.size() - 1);
        assertThat(testDemandeInspecteurDE.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void createDemandeInspecteurDEWithExistingId() throws Exception {
        // Create the DemandeInspecteurDE with an existing ID
        demandeInspecteurDE.setId(1L);
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);

        int databaseSizeBeforeCreate = demandeInspecteurDERepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeInspecteurDEMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeInspecteurDERepository.findAll().size();
        // set the field null
        demandeInspecteurDE.setMessage(null);

        // Create the DemandeInspecteurDE, which fails.
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);

        restDemandeInspecteurDEMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemandeInspecteurDES() throws Exception {
        // Initialize the database
        demandeInspecteurDERepository.saveAndFlush(demandeInspecteurDE);

        // Get all the demandeInspecteurDEList
        restDemandeInspecteurDEMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeInspecteurDE.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeInspecteurDESWithEagerRelationshipsIsEnabled() throws Exception {
        when(demandeInspecteurDEServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeInspecteurDEMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeInspecteurDEServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeInspecteurDESWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(demandeInspecteurDEServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeInspecteurDEMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeInspecteurDEServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDemandeInspecteurDE() throws Exception {
        // Initialize the database
        demandeInspecteurDERepository.saveAndFlush(demandeInspecteurDE);

        // Get the demandeInspecteurDE
        restDemandeInspecteurDEMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeInspecteurDE.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeInspecteurDE.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingDemandeInspecteurDE() throws Exception {
        // Get the demandeInspecteurDE
        restDemandeInspecteurDEMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeInspecteurDE() throws Exception {
        // Initialize the database
        demandeInspecteurDERepository.saveAndFlush(demandeInspecteurDE);

        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();

        // Update the demandeInspecteurDE
        DemandeInspecteurDE updatedDemandeInspecteurDE = demandeInspecteurDERepository.findById(demandeInspecteurDE.getId()).get();
        // Disconnect from session so that the updates on updatedDemandeInspecteurDE are not directly saved in db
        em.detach(updatedDemandeInspecteurDE);
        updatedDemandeInspecteurDE.message(UPDATED_MESSAGE);
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(updatedDemandeInspecteurDE);

        restDemandeInspecteurDEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeInspecteurDEDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
        DemandeInspecteurDE testDemandeInspecteurDE = demandeInspecteurDEList.get(demandeInspecteurDEList.size() - 1);
        assertThat(testDemandeInspecteurDE.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void putNonExistingDemandeInspecteurDE() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();
        demandeInspecteurDE.setId(count.incrementAndGet());

        // Create the DemandeInspecteurDE
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeInspecteurDEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeInspecteurDEDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeInspecteurDE() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();
        demandeInspecteurDE.setId(count.incrementAndGet());

        // Create the DemandeInspecteurDE
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInspecteurDEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeInspecteurDE() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();
        demandeInspecteurDE.setId(count.incrementAndGet());

        // Create the DemandeInspecteurDE
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInspecteurDEMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeInspecteurDEWithPatch() throws Exception {
        // Initialize the database
        demandeInspecteurDERepository.saveAndFlush(demandeInspecteurDE);

        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();

        // Update the demandeInspecteurDE using partial update
        DemandeInspecteurDE partialUpdatedDemandeInspecteurDE = new DemandeInspecteurDE();
        partialUpdatedDemandeInspecteurDE.setId(demandeInspecteurDE.getId());

        restDemandeInspecteurDEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeInspecteurDE.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeInspecteurDE))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
        DemandeInspecteurDE testDemandeInspecteurDE = demandeInspecteurDEList.get(demandeInspecteurDEList.size() - 1);
        assertThat(testDemandeInspecteurDE.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateDemandeInspecteurDEWithPatch() throws Exception {
        // Initialize the database
        demandeInspecteurDERepository.saveAndFlush(demandeInspecteurDE);

        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();

        // Update the demandeInspecteurDE using partial update
        DemandeInspecteurDE partialUpdatedDemandeInspecteurDE = new DemandeInspecteurDE();
        partialUpdatedDemandeInspecteurDE.setId(demandeInspecteurDE.getId());

        partialUpdatedDemandeInspecteurDE.message(UPDATED_MESSAGE);

        restDemandeInspecteurDEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeInspecteurDE.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeInspecteurDE))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
        DemandeInspecteurDE testDemandeInspecteurDE = demandeInspecteurDEList.get(demandeInspecteurDEList.size() - 1);
        assertThat(testDemandeInspecteurDE.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeInspecteurDE() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();
        demandeInspecteurDE.setId(count.incrementAndGet());

        // Create the DemandeInspecteurDE
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeInspecteurDEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeInspecteurDEDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeInspecteurDE() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();
        demandeInspecteurDE.setId(count.incrementAndGet());

        // Create the DemandeInspecteurDE
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInspecteurDEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeInspecteurDE() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurDERepository.findAll().size();
        demandeInspecteurDE.setId(count.incrementAndGet());

        // Create the DemandeInspecteurDE
        DemandeInspecteurDEDTO demandeInspecteurDEDTO = demandeInspecteurDEMapper.toDto(demandeInspecteurDE);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInspecteurDEMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurDEDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeInspecteurDE in the database
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeInspecteurDE() throws Exception {
        // Initialize the database
        demandeInspecteurDERepository.saveAndFlush(demandeInspecteurDE);

        int databaseSizeBeforeDelete = demandeInspecteurDERepository.findAll().size();

        // Delete the demandeInspecteurDE
        restDemandeInspecteurDEMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeInspecteurDE.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeInspecteurDE> demandeInspecteurDEList = demandeInspecteurDERepository.findAll();
        assertThat(demandeInspecteurDEList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
