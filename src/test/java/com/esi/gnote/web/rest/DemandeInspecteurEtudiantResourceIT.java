package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.DemandeInspecteurEtudiant;
import com.esi.gnote.repository.DemandeInspecteurEtudiantRepository;
import com.esi.gnote.service.DemandeInspecteurEtudiantService;
import com.esi.gnote.service.dto.DemandeInspecteurEtudiantDTO;
import com.esi.gnote.service.mapper.DemandeInspecteurEtudiantMapper;
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
 * Integration tests for the {@link DemandeInspecteurEtudiantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DemandeInspecteurEtudiantResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/demande-inspecteur-etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeInspecteurEtudiantRepository demandeInspecteurEtudiantRepository;

    @Mock
    private DemandeInspecteurEtudiantRepository demandeInspecteurEtudiantRepositoryMock;

    @Autowired
    private DemandeInspecteurEtudiantMapper demandeInspecteurEtudiantMapper;

    @Mock
    private DemandeInspecteurEtudiantService demandeInspecteurEtudiantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeInspecteurEtudiantMockMvc;

    private DemandeInspecteurEtudiant demandeInspecteurEtudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeInspecteurEtudiant createEntity(EntityManager em) {
        DemandeInspecteurEtudiant demandeInspecteurEtudiant = new DemandeInspecteurEtudiant().message(DEFAULT_MESSAGE);
        return demandeInspecteurEtudiant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeInspecteurEtudiant createUpdatedEntity(EntityManager em) {
        DemandeInspecteurEtudiant demandeInspecteurEtudiant = new DemandeInspecteurEtudiant().message(UPDATED_MESSAGE);
        return demandeInspecteurEtudiant;
    }

    @BeforeEach
    public void initTest() {
        demandeInspecteurEtudiant = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeInspecteurEtudiant() throws Exception {
        int databaseSizeBeforeCreate = demandeInspecteurEtudiantRepository.findAll().size();
        // Create the DemandeInspecteurEtudiant
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);
        restDemandeInspecteurEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeInspecteurEtudiant testDemandeInspecteurEtudiant = demandeInspecteurEtudiantList.get(
            demandeInspecteurEtudiantList.size() - 1
        );
        assertThat(testDemandeInspecteurEtudiant.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void createDemandeInspecteurEtudiantWithExistingId() throws Exception {
        // Create the DemandeInspecteurEtudiant with an existing ID
        demandeInspecteurEtudiant.setId(1L);
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);

        int databaseSizeBeforeCreate = demandeInspecteurEtudiantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeInspecteurEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeInspecteurEtudiantRepository.findAll().size();
        // set the field null
        demandeInspecteurEtudiant.setMessage(null);

        // Create the DemandeInspecteurEtudiant, which fails.
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);

        restDemandeInspecteurEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemandeInspecteurEtudiants() throws Exception {
        // Initialize the database
        demandeInspecteurEtudiantRepository.saveAndFlush(demandeInspecteurEtudiant);

        // Get all the demandeInspecteurEtudiantList
        restDemandeInspecteurEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeInspecteurEtudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeInspecteurEtudiantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(demandeInspecteurEtudiantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeInspecteurEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeInspecteurEtudiantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeInspecteurEtudiantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(demandeInspecteurEtudiantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeInspecteurEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeInspecteurEtudiantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDemandeInspecteurEtudiant() throws Exception {
        // Initialize the database
        demandeInspecteurEtudiantRepository.saveAndFlush(demandeInspecteurEtudiant);

        // Get the demandeInspecteurEtudiant
        restDemandeInspecteurEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeInspecteurEtudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeInspecteurEtudiant.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingDemandeInspecteurEtudiant() throws Exception {
        // Get the demandeInspecteurEtudiant
        restDemandeInspecteurEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeInspecteurEtudiant() throws Exception {
        // Initialize the database
        demandeInspecteurEtudiantRepository.saveAndFlush(demandeInspecteurEtudiant);

        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();

        // Update the demandeInspecteurEtudiant
        DemandeInspecteurEtudiant updatedDemandeInspecteurEtudiant = demandeInspecteurEtudiantRepository
            .findById(demandeInspecteurEtudiant.getId())
            .get();
        // Disconnect from session so that the updates on updatedDemandeInspecteurEtudiant are not directly saved in db
        em.detach(updatedDemandeInspecteurEtudiant);
        updatedDemandeInspecteurEtudiant.message(UPDATED_MESSAGE);
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(updatedDemandeInspecteurEtudiant);

        restDemandeInspecteurEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeInspecteurEtudiantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
        DemandeInspecteurEtudiant testDemandeInspecteurEtudiant = demandeInspecteurEtudiantList.get(
            demandeInspecteurEtudiantList.size() - 1
        );
        assertThat(testDemandeInspecteurEtudiant.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void putNonExistingDemandeInspecteurEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();
        demandeInspecteurEtudiant.setId(count.incrementAndGet());

        // Create the DemandeInspecteurEtudiant
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeInspecteurEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeInspecteurEtudiantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeInspecteurEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();
        demandeInspecteurEtudiant.setId(count.incrementAndGet());

        // Create the DemandeInspecteurEtudiant
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInspecteurEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeInspecteurEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();
        demandeInspecteurEtudiant.setId(count.incrementAndGet());

        // Create the DemandeInspecteurEtudiant
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInspecteurEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeInspecteurEtudiantWithPatch() throws Exception {
        // Initialize the database
        demandeInspecteurEtudiantRepository.saveAndFlush(demandeInspecteurEtudiant);

        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();

        // Update the demandeInspecteurEtudiant using partial update
        DemandeInspecteurEtudiant partialUpdatedDemandeInspecteurEtudiant = new DemandeInspecteurEtudiant();
        partialUpdatedDemandeInspecteurEtudiant.setId(demandeInspecteurEtudiant.getId());

        partialUpdatedDemandeInspecteurEtudiant.message(UPDATED_MESSAGE);

        restDemandeInspecteurEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeInspecteurEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeInspecteurEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
        DemandeInspecteurEtudiant testDemandeInspecteurEtudiant = demandeInspecteurEtudiantList.get(
            demandeInspecteurEtudiantList.size() - 1
        );
        assertThat(testDemandeInspecteurEtudiant.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateDemandeInspecteurEtudiantWithPatch() throws Exception {
        // Initialize the database
        demandeInspecteurEtudiantRepository.saveAndFlush(demandeInspecteurEtudiant);

        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();

        // Update the demandeInspecteurEtudiant using partial update
        DemandeInspecteurEtudiant partialUpdatedDemandeInspecteurEtudiant = new DemandeInspecteurEtudiant();
        partialUpdatedDemandeInspecteurEtudiant.setId(demandeInspecteurEtudiant.getId());

        partialUpdatedDemandeInspecteurEtudiant.message(UPDATED_MESSAGE);

        restDemandeInspecteurEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeInspecteurEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeInspecteurEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
        DemandeInspecteurEtudiant testDemandeInspecteurEtudiant = demandeInspecteurEtudiantList.get(
            demandeInspecteurEtudiantList.size() - 1
        );
        assertThat(testDemandeInspecteurEtudiant.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeInspecteurEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();
        demandeInspecteurEtudiant.setId(count.incrementAndGet());

        // Create the DemandeInspecteurEtudiant
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeInspecteurEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeInspecteurEtudiantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeInspecteurEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();
        demandeInspecteurEtudiant.setId(count.incrementAndGet());

        // Create the DemandeInspecteurEtudiant
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInspecteurEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeInspecteurEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = demandeInspecteurEtudiantRepository.findAll().size();
        demandeInspecteurEtudiant.setId(count.incrementAndGet());

        // Create the DemandeInspecteurEtudiant
        DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantMapper.toDto(demandeInspecteurEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInspecteurEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInspecteurEtudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeInspecteurEtudiant in the database
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeInspecteurEtudiant() throws Exception {
        // Initialize the database
        demandeInspecteurEtudiantRepository.saveAndFlush(demandeInspecteurEtudiant);

        int databaseSizeBeforeDelete = demandeInspecteurEtudiantRepository.findAll().size();

        // Delete the demandeInspecteurEtudiant
        restDemandeInspecteurEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeInspecteurEtudiant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeInspecteurEtudiant> demandeInspecteurEtudiantList = demandeInspecteurEtudiantRepository.findAll();
        assertThat(demandeInspecteurEtudiantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
