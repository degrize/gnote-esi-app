package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Inspecteur;
import com.esi.gnote.repository.InspecteurRepository;
import com.esi.gnote.service.InspecteurService;
import com.esi.gnote.service.dto.InspecteurDTO;
import com.esi.gnote.service.mapper.InspecteurMapper;
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
 * Integration tests for the {@link InspecteurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InspecteurResourceIT {

    private static final String DEFAULT_NOM_INSPECTEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_INSPECTEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_INSPECTEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_INSPECTEUR = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_INSPECTEUR = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_INSPECTEUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inspecteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InspecteurRepository inspecteurRepository;

    @Mock
    private InspecteurRepository inspecteurRepositoryMock;

    @Autowired
    private InspecteurMapper inspecteurMapper;

    @Mock
    private InspecteurService inspecteurServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInspecteurMockMvc;

    private Inspecteur inspecteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspecteur createEntity(EntityManager em) {
        Inspecteur inspecteur = new Inspecteur()
            .nomInspecteur(DEFAULT_NOM_INSPECTEUR)
            .prenomInspecteur(DEFAULT_PRENOM_INSPECTEUR)
            .contactInspecteur(DEFAULT_CONTACT_INSPECTEUR);
        return inspecteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspecteur createUpdatedEntity(EntityManager em) {
        Inspecteur inspecteur = new Inspecteur()
            .nomInspecteur(UPDATED_NOM_INSPECTEUR)
            .prenomInspecteur(UPDATED_PRENOM_INSPECTEUR)
            .contactInspecteur(UPDATED_CONTACT_INSPECTEUR);
        return inspecteur;
    }

    @BeforeEach
    public void initTest() {
        inspecteur = createEntity(em);
    }

    @Test
    @Transactional
    void createInspecteur() throws Exception {
        int databaseSizeBeforeCreate = inspecteurRepository.findAll().size();
        // Create the Inspecteur
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);
        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteurDTO)))
            .andExpect(status().isCreated());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeCreate + 1);
        Inspecteur testInspecteur = inspecteurList.get(inspecteurList.size() - 1);
        assertThat(testInspecteur.getNomInspecteur()).isEqualTo(DEFAULT_NOM_INSPECTEUR);
        assertThat(testInspecteur.getPrenomInspecteur()).isEqualTo(DEFAULT_PRENOM_INSPECTEUR);
        assertThat(testInspecteur.getContactInspecteur()).isEqualTo(DEFAULT_CONTACT_INSPECTEUR);
    }

    @Test
    @Transactional
    void createInspecteurWithExistingId() throws Exception {
        // Create the Inspecteur with an existing ID
        inspecteur.setId(1L);
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);

        int databaseSizeBeforeCreate = inspecteurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomInspecteurIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspecteurRepository.findAll().size();
        // set the field null
        inspecteur.setNomInspecteur(null);

        // Create the Inspecteur, which fails.
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);

        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteurDTO)))
            .andExpect(status().isBadRequest());

        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInspecteurs() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        // Get all the inspecteurList
        restInspecteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspecteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomInspecteur").value(hasItem(DEFAULT_NOM_INSPECTEUR)))
            .andExpect(jsonPath("$.[*].prenomInspecteur").value(hasItem(DEFAULT_PRENOM_INSPECTEUR)))
            .andExpect(jsonPath("$.[*].contactInspecteur").value(hasItem(DEFAULT_CONTACT_INSPECTEUR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInspecteursWithEagerRelationshipsIsEnabled() throws Exception {
        when(inspecteurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInspecteurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inspecteurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInspecteursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inspecteurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInspecteurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inspecteurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInspecteur() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        // Get the inspecteur
        restInspecteurMockMvc
            .perform(get(ENTITY_API_URL_ID, inspecteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspecteur.getId().intValue()))
            .andExpect(jsonPath("$.nomInspecteur").value(DEFAULT_NOM_INSPECTEUR))
            .andExpect(jsonPath("$.prenomInspecteur").value(DEFAULT_PRENOM_INSPECTEUR))
            .andExpect(jsonPath("$.contactInspecteur").value(DEFAULT_CONTACT_INSPECTEUR));
    }

    @Test
    @Transactional
    void getNonExistingInspecteur() throws Exception {
        // Get the inspecteur
        restInspecteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInspecteur() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();

        // Update the inspecteur
        Inspecteur updatedInspecteur = inspecteurRepository.findById(inspecteur.getId()).get();
        // Disconnect from session so that the updates on updatedInspecteur are not directly saved in db
        em.detach(updatedInspecteur);
        updatedInspecteur
            .nomInspecteur(UPDATED_NOM_INSPECTEUR)
            .prenomInspecteur(UPDATED_PRENOM_INSPECTEUR)
            .contactInspecteur(UPDATED_CONTACT_INSPECTEUR);
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(updatedInspecteur);

        restInspecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspecteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspecteurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
        Inspecteur testInspecteur = inspecteurList.get(inspecteurList.size() - 1);
        assertThat(testInspecteur.getNomInspecteur()).isEqualTo(UPDATED_NOM_INSPECTEUR);
        assertThat(testInspecteur.getPrenomInspecteur()).isEqualTo(UPDATED_PRENOM_INSPECTEUR);
        assertThat(testInspecteur.getContactInspecteur()).isEqualTo(UPDATED_CONTACT_INSPECTEUR);
    }

    @Test
    @Transactional
    void putNonExistingInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // Create the Inspecteur
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspecteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspecteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // Create the Inspecteur
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspecteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // Create the Inspecteur
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInspecteurWithPatch() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();

        // Update the inspecteur using partial update
        Inspecteur partialUpdatedInspecteur = new Inspecteur();
        partialUpdatedInspecteur.setId(inspecteur.getId());

        partialUpdatedInspecteur.nomInspecteur(UPDATED_NOM_INSPECTEUR).contactInspecteur(UPDATED_CONTACT_INSPECTEUR);

        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspecteur))
            )
            .andExpect(status().isOk());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
        Inspecteur testInspecteur = inspecteurList.get(inspecteurList.size() - 1);
        assertThat(testInspecteur.getNomInspecteur()).isEqualTo(UPDATED_NOM_INSPECTEUR);
        assertThat(testInspecteur.getPrenomInspecteur()).isEqualTo(DEFAULT_PRENOM_INSPECTEUR);
        assertThat(testInspecteur.getContactInspecteur()).isEqualTo(UPDATED_CONTACT_INSPECTEUR);
    }

    @Test
    @Transactional
    void fullUpdateInspecteurWithPatch() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();

        // Update the inspecteur using partial update
        Inspecteur partialUpdatedInspecteur = new Inspecteur();
        partialUpdatedInspecteur.setId(inspecteur.getId());

        partialUpdatedInspecteur
            .nomInspecteur(UPDATED_NOM_INSPECTEUR)
            .prenomInspecteur(UPDATED_PRENOM_INSPECTEUR)
            .contactInspecteur(UPDATED_CONTACT_INSPECTEUR);

        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspecteur))
            )
            .andExpect(status().isOk());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
        Inspecteur testInspecteur = inspecteurList.get(inspecteurList.size() - 1);
        assertThat(testInspecteur.getNomInspecteur()).isEqualTo(UPDATED_NOM_INSPECTEUR);
        assertThat(testInspecteur.getPrenomInspecteur()).isEqualTo(UPDATED_PRENOM_INSPECTEUR);
        assertThat(testInspecteur.getContactInspecteur()).isEqualTo(UPDATED_CONTACT_INSPECTEUR);
    }

    @Test
    @Transactional
    void patchNonExistingInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // Create the Inspecteur
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspecteurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspecteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // Create the Inspecteur
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspecteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // Create the Inspecteur
        InspecteurDTO inspecteurDTO = inspecteurMapper.toDto(inspecteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inspecteurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInspecteur() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        int databaseSizeBeforeDelete = inspecteurRepository.findAll().size();

        // Delete the inspecteur
        restInspecteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspecteur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
