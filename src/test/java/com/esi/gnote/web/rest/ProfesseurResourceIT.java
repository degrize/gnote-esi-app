package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Professeur;
import com.esi.gnote.repository.ProfesseurRepository;
import com.esi.gnote.service.ProfesseurService;
import com.esi.gnote.service.dto.ProfesseurDTO;
import com.esi.gnote.service.mapper.ProfesseurMapper;
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
 * Integration tests for the {@link ProfesseurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfesseurResourceIT {

    private static final String DEFAULT_NOM_PROF = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PROF = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_PROF = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_PROF = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PROF = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PROF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/professeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Mock
    private ProfesseurRepository professeurRepositoryMock;

    @Autowired
    private ProfesseurMapper professeurMapper;

    @Mock
    private ProfesseurService professeurServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfesseurMockMvc;

    private Professeur professeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professeur createEntity(EntityManager em) {
        Professeur professeur = new Professeur()
            .nomProf(DEFAULT_NOM_PROF)
            .prenomProf(DEFAULT_PRENOM_PROF)
            .contactProf(DEFAULT_CONTACT_PROF);
        return professeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professeur createUpdatedEntity(EntityManager em) {
        Professeur professeur = new Professeur()
            .nomProf(UPDATED_NOM_PROF)
            .prenomProf(UPDATED_PRENOM_PROF)
            .contactProf(UPDATED_CONTACT_PROF);
        return professeur;
    }

    @BeforeEach
    public void initTest() {
        professeur = createEntity(em);
    }

    @Test
    @Transactional
    void createProfesseur() throws Exception {
        int databaseSizeBeforeCreate = professeurRepository.findAll().size();
        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);
        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professeurDTO)))
            .andExpect(status().isCreated());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeCreate + 1);
        Professeur testProfesseur = professeurList.get(professeurList.size() - 1);
        assertThat(testProfesseur.getNomProf()).isEqualTo(DEFAULT_NOM_PROF);
        assertThat(testProfesseur.getPrenomProf()).isEqualTo(DEFAULT_PRENOM_PROF);
        assertThat(testProfesseur.getContactProf()).isEqualTo(DEFAULT_CONTACT_PROF);
    }

    @Test
    @Transactional
    void createProfesseurWithExistingId() throws Exception {
        // Create the Professeur with an existing ID
        professeur.setId(1L);
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        int databaseSizeBeforeCreate = professeurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professeurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomProfIsRequired() throws Exception {
        int databaseSizeBeforeTest = professeurRepository.findAll().size();
        // set the field null
        professeur.setNomProf(null);

        // Create the Professeur, which fails.
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professeurDTO)))
            .andExpect(status().isBadRequest());

        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomProfIsRequired() throws Exception {
        int databaseSizeBeforeTest = professeurRepository.findAll().size();
        // set the field null
        professeur.setPrenomProf(null);

        // Create the Professeur, which fails.
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professeurDTO)))
            .andExpect(status().isBadRequest());

        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfesseurs() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProf").value(hasItem(DEFAULT_NOM_PROF)))
            .andExpect(jsonPath("$.[*].prenomProf").value(hasItem(DEFAULT_PRENOM_PROF)))
            .andExpect(jsonPath("$.[*].contactProf").value(hasItem(DEFAULT_CONTACT_PROF)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfesseursWithEagerRelationshipsIsEnabled() throws Exception {
        when(professeurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfesseurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professeurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfesseursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professeurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfesseurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professeurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get the professeur
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL_ID, professeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professeur.getId().intValue()))
            .andExpect(jsonPath("$.nomProf").value(DEFAULT_NOM_PROF))
            .andExpect(jsonPath("$.prenomProf").value(DEFAULT_PRENOM_PROF))
            .andExpect(jsonPath("$.contactProf").value(DEFAULT_CONTACT_PROF));
    }

    @Test
    @Transactional
    void getNonExistingProfesseur() throws Exception {
        // Get the professeur
        restProfesseurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();

        // Update the professeur
        Professeur updatedProfesseur = professeurRepository.findById(professeur.getId()).get();
        // Disconnect from session so that the updates on updatedProfesseur are not directly saved in db
        em.detach(updatedProfesseur);
        updatedProfesseur.nomProf(UPDATED_NOM_PROF).prenomProf(UPDATED_PRENOM_PROF).contactProf(UPDATED_CONTACT_PROF);
        ProfesseurDTO professeurDTO = professeurMapper.toDto(updatedProfesseur);

        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professeurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professeurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
        Professeur testProfesseur = professeurList.get(professeurList.size() - 1);
        assertThat(testProfesseur.getNomProf()).isEqualTo(UPDATED_NOM_PROF);
        assertThat(testProfesseur.getPrenomProf()).isEqualTo(UPDATED_PRENOM_PROF);
        assertThat(testProfesseur.getContactProf()).isEqualTo(UPDATED_CONTACT_PROF);
    }

    @Test
    @Transactional
    void putNonExistingProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(count.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professeurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professeurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(count.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professeurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(count.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professeurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfesseurWithPatch() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();

        // Update the professeur using partial update
        Professeur partialUpdatedProfesseur = new Professeur();
        partialUpdatedProfesseur.setId(professeur.getId());

        partialUpdatedProfesseur.nomProf(UPDATED_NOM_PROF).prenomProf(UPDATED_PRENOM_PROF).contactProf(UPDATED_CONTACT_PROF);

        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfesseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfesseur))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
        Professeur testProfesseur = professeurList.get(professeurList.size() - 1);
        assertThat(testProfesseur.getNomProf()).isEqualTo(UPDATED_NOM_PROF);
        assertThat(testProfesseur.getPrenomProf()).isEqualTo(UPDATED_PRENOM_PROF);
        assertThat(testProfesseur.getContactProf()).isEqualTo(UPDATED_CONTACT_PROF);
    }

    @Test
    @Transactional
    void fullUpdateProfesseurWithPatch() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();

        // Update the professeur using partial update
        Professeur partialUpdatedProfesseur = new Professeur();
        partialUpdatedProfesseur.setId(professeur.getId());

        partialUpdatedProfesseur.nomProf(UPDATED_NOM_PROF).prenomProf(UPDATED_PRENOM_PROF).contactProf(UPDATED_CONTACT_PROF);

        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfesseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfesseur))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
        Professeur testProfesseur = professeurList.get(professeurList.size() - 1);
        assertThat(testProfesseur.getNomProf()).isEqualTo(UPDATED_NOM_PROF);
        assertThat(testProfesseur.getPrenomProf()).isEqualTo(UPDATED_PRENOM_PROF);
        assertThat(testProfesseur.getContactProf()).isEqualTo(UPDATED_CONTACT_PROF);
    }

    @Test
    @Transactional
    void patchNonExistingProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(count.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professeurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professeurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(count.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professeurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(count.incrementAndGet());

        // Create the Professeur
        ProfesseurDTO professeurDTO = professeurMapper.toDto(professeur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(professeurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        int databaseSizeBeforeDelete = professeurRepository.findAll().size();

        // Delete the professeur
        restProfesseurMockMvc
            .perform(delete(ENTITY_API_URL_ID, professeur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
