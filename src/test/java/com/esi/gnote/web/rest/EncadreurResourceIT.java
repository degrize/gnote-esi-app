package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Encadreur;
import com.esi.gnote.repository.EncadreurRepository;
import com.esi.gnote.service.dto.EncadreurDTO;
import com.esi.gnote.service.mapper.EncadreurMapper;
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
 * Integration tests for the {@link EncadreurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EncadreurResourceIT {

    private static final String DEFAULT_NOM_ENC = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ENC = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOMS_ENC = "AAAAAAAAAA";
    private static final String UPDATED_PRENOMS_ENC = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ENC = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ENC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/encadreurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EncadreurRepository encadreurRepository;

    @Autowired
    private EncadreurMapper encadreurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEncadreurMockMvc;

    private Encadreur encadreur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encadreur createEntity(EntityManager em) {
        Encadreur encadreur = new Encadreur().nomEnc(DEFAULT_NOM_ENC).prenomsEnc(DEFAULT_PRENOMS_ENC).emailEnc(DEFAULT_EMAIL_ENC);
        return encadreur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encadreur createUpdatedEntity(EntityManager em) {
        Encadreur encadreur = new Encadreur().nomEnc(UPDATED_NOM_ENC).prenomsEnc(UPDATED_PRENOMS_ENC).emailEnc(UPDATED_EMAIL_ENC);
        return encadreur;
    }

    @BeforeEach
    public void initTest() {
        encadreur = createEntity(em);
    }

    @Test
    @Transactional
    void createEncadreur() throws Exception {
        int databaseSizeBeforeCreate = encadreurRepository.findAll().size();
        // Create the Encadreur
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);
        restEncadreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encadreurDTO)))
            .andExpect(status().isCreated());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeCreate + 1);
        Encadreur testEncadreur = encadreurList.get(encadreurList.size() - 1);
        assertThat(testEncadreur.getNomEnc()).isEqualTo(DEFAULT_NOM_ENC);
        assertThat(testEncadreur.getPrenomsEnc()).isEqualTo(DEFAULT_PRENOMS_ENC);
        assertThat(testEncadreur.getEmailEnc()).isEqualTo(DEFAULT_EMAIL_ENC);
    }

    @Test
    @Transactional
    void createEncadreurWithExistingId() throws Exception {
        // Create the Encadreur with an existing ID
        encadreur.setId(1L);
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);

        int databaseSizeBeforeCreate = encadreurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEncadreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encadreurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomEncIsRequired() throws Exception {
        int databaseSizeBeforeTest = encadreurRepository.findAll().size();
        // set the field null
        encadreur.setNomEnc(null);

        // Create the Encadreur, which fails.
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);

        restEncadreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encadreurDTO)))
            .andExpect(status().isBadRequest());

        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEncadreurs() throws Exception {
        // Initialize the database
        encadreurRepository.saveAndFlush(encadreur);

        // Get all the encadreurList
        restEncadreurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encadreur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEnc").value(hasItem(DEFAULT_NOM_ENC)))
            .andExpect(jsonPath("$.[*].prenomsEnc").value(hasItem(DEFAULT_PRENOMS_ENC)))
            .andExpect(jsonPath("$.[*].emailEnc").value(hasItem(DEFAULT_EMAIL_ENC)));
    }

    @Test
    @Transactional
    void getEncadreur() throws Exception {
        // Initialize the database
        encadreurRepository.saveAndFlush(encadreur);

        // Get the encadreur
        restEncadreurMockMvc
            .perform(get(ENTITY_API_URL_ID, encadreur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(encadreur.getId().intValue()))
            .andExpect(jsonPath("$.nomEnc").value(DEFAULT_NOM_ENC))
            .andExpect(jsonPath("$.prenomsEnc").value(DEFAULT_PRENOMS_ENC))
            .andExpect(jsonPath("$.emailEnc").value(DEFAULT_EMAIL_ENC));
    }

    @Test
    @Transactional
    void getNonExistingEncadreur() throws Exception {
        // Get the encadreur
        restEncadreurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEncadreur() throws Exception {
        // Initialize the database
        encadreurRepository.saveAndFlush(encadreur);

        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();

        // Update the encadreur
        Encadreur updatedEncadreur = encadreurRepository.findById(encadreur.getId()).get();
        // Disconnect from session so that the updates on updatedEncadreur are not directly saved in db
        em.detach(updatedEncadreur);
        updatedEncadreur.nomEnc(UPDATED_NOM_ENC).prenomsEnc(UPDATED_PRENOMS_ENC).emailEnc(UPDATED_EMAIL_ENC);
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(updatedEncadreur);

        restEncadreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, encadreurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encadreurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
        Encadreur testEncadreur = encadreurList.get(encadreurList.size() - 1);
        assertThat(testEncadreur.getNomEnc()).isEqualTo(UPDATED_NOM_ENC);
        assertThat(testEncadreur.getPrenomsEnc()).isEqualTo(UPDATED_PRENOMS_ENC);
        assertThat(testEncadreur.getEmailEnc()).isEqualTo(UPDATED_EMAIL_ENC);
    }

    @Test
    @Transactional
    void putNonExistingEncadreur() throws Exception {
        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();
        encadreur.setId(count.incrementAndGet());

        // Create the Encadreur
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncadreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, encadreurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encadreurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEncadreur() throws Exception {
        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();
        encadreur.setId(count.incrementAndGet());

        // Create the Encadreur
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncadreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encadreurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEncadreur() throws Exception {
        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();
        encadreur.setId(count.incrementAndGet());

        // Create the Encadreur
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncadreurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encadreurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEncadreurWithPatch() throws Exception {
        // Initialize the database
        encadreurRepository.saveAndFlush(encadreur);

        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();

        // Update the encadreur using partial update
        Encadreur partialUpdatedEncadreur = new Encadreur();
        partialUpdatedEncadreur.setId(encadreur.getId());

        partialUpdatedEncadreur.nomEnc(UPDATED_NOM_ENC);

        restEncadreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEncadreur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEncadreur))
            )
            .andExpect(status().isOk());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
        Encadreur testEncadreur = encadreurList.get(encadreurList.size() - 1);
        assertThat(testEncadreur.getNomEnc()).isEqualTo(UPDATED_NOM_ENC);
        assertThat(testEncadreur.getPrenomsEnc()).isEqualTo(DEFAULT_PRENOMS_ENC);
        assertThat(testEncadreur.getEmailEnc()).isEqualTo(DEFAULT_EMAIL_ENC);
    }

    @Test
    @Transactional
    void fullUpdateEncadreurWithPatch() throws Exception {
        // Initialize the database
        encadreurRepository.saveAndFlush(encadreur);

        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();

        // Update the encadreur using partial update
        Encadreur partialUpdatedEncadreur = new Encadreur();
        partialUpdatedEncadreur.setId(encadreur.getId());

        partialUpdatedEncadreur.nomEnc(UPDATED_NOM_ENC).prenomsEnc(UPDATED_PRENOMS_ENC).emailEnc(UPDATED_EMAIL_ENC);

        restEncadreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEncadreur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEncadreur))
            )
            .andExpect(status().isOk());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
        Encadreur testEncadreur = encadreurList.get(encadreurList.size() - 1);
        assertThat(testEncadreur.getNomEnc()).isEqualTo(UPDATED_NOM_ENC);
        assertThat(testEncadreur.getPrenomsEnc()).isEqualTo(UPDATED_PRENOMS_ENC);
        assertThat(testEncadreur.getEmailEnc()).isEqualTo(UPDATED_EMAIL_ENC);
    }

    @Test
    @Transactional
    void patchNonExistingEncadreur() throws Exception {
        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();
        encadreur.setId(count.incrementAndGet());

        // Create the Encadreur
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncadreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, encadreurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encadreurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEncadreur() throws Exception {
        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();
        encadreur.setId(count.incrementAndGet());

        // Create the Encadreur
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncadreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encadreurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEncadreur() throws Exception {
        int databaseSizeBeforeUpdate = encadreurRepository.findAll().size();
        encadreur.setId(count.incrementAndGet());

        // Create the Encadreur
        EncadreurDTO encadreurDTO = encadreurMapper.toDto(encadreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncadreurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(encadreurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Encadreur in the database
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEncadreur() throws Exception {
        // Initialize the database
        encadreurRepository.saveAndFlush(encadreur);

        int databaseSizeBeforeDelete = encadreurRepository.findAll().size();

        // Delete the encadreur
        restEncadreurMockMvc
            .perform(delete(ENTITY_API_URL_ID, encadreur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Encadreur> encadreurList = encadreurRepository.findAll();
        assertThat(encadreurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
