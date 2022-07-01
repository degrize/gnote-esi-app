package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Matiere;
import com.esi.gnote.repository.MatiereRepository;
import com.esi.gnote.service.dto.MatiereDTO;
import com.esi.gnote.service.mapper.MatiereMapper;
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
 * Integration tests for the {@link MatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MatiereResourceIT {

    private static final String DEFAULT_NOM_EC = "AAAAAAAAAA";
    private static final String UPDATED_NOM_EC = "BBBBBBBBBB";

    private static final Integer DEFAULT_COEFF = 1;
    private static final Integer UPDATED_COEFF = 2;

    private static final String ENTITY_API_URL = "/api/matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private MatiereMapper matiereMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatiereMockMvc;

    private Matiere matiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matiere createEntity(EntityManager em) {
        Matiere matiere = new Matiere().nomEC(DEFAULT_NOM_EC).coeff(DEFAULT_COEFF);
        return matiere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matiere createUpdatedEntity(EntityManager em) {
        Matiere matiere = new Matiere().nomEC(UPDATED_NOM_EC).coeff(UPDATED_COEFF);
        return matiere;
    }

    @BeforeEach
    public void initTest() {
        matiere = createEntity(em);
    }

    @Test
    @Transactional
    void createMatiere() throws Exception {
        int databaseSizeBeforeCreate = matiereRepository.findAll().size();
        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);
        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiereDTO)))
            .andExpect(status().isCreated());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeCreate + 1);
        Matiere testMatiere = matiereList.get(matiereList.size() - 1);
        assertThat(testMatiere.getNomEC()).isEqualTo(DEFAULT_NOM_EC);
        assertThat(testMatiere.getCoeff()).isEqualTo(DEFAULT_COEFF);
    }

    @Test
    @Transactional
    void createMatiereWithExistingId() throws Exception {
        // Create the Matiere with an existing ID
        matiere.setId(1L);
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        int databaseSizeBeforeCreate = matiereRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiereDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomECIsRequired() throws Exception {
        int databaseSizeBeforeTest = matiereRepository.findAll().size();
        // set the field null
        matiere.setNomEC(null);

        // Create the Matiere, which fails.
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiereDTO)))
            .andExpect(status().isBadRequest());

        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoeffIsRequired() throws Exception {
        int databaseSizeBeforeTest = matiereRepository.findAll().size();
        // set the field null
        matiere.setCoeff(null);

        // Create the Matiere, which fails.
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiereDTO)))
            .andExpect(status().isBadRequest());

        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatieres() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        // Get all the matiereList
        restMatiereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEC").value(hasItem(DEFAULT_NOM_EC)))
            .andExpect(jsonPath("$.[*].coeff").value(hasItem(DEFAULT_COEFF)));
    }

    @Test
    @Transactional
    void getMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        // Get the matiere
        restMatiereMockMvc
            .perform(get(ENTITY_API_URL_ID, matiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matiere.getId().intValue()))
            .andExpect(jsonPath("$.nomEC").value(DEFAULT_NOM_EC))
            .andExpect(jsonPath("$.coeff").value(DEFAULT_COEFF));
    }

    @Test
    @Transactional
    void getNonExistingMatiere() throws Exception {
        // Get the matiere
        restMatiereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();

        // Update the matiere
        Matiere updatedMatiere = matiereRepository.findById(matiere.getId()).get();
        // Disconnect from session so that the updates on updatedMatiere are not directly saved in db
        em.detach(updatedMatiere);
        updatedMatiere.nomEC(UPDATED_NOM_EC).coeff(UPDATED_COEFF);
        MatiereDTO matiereDTO = matiereMapper.toDto(updatedMatiere);

        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matiereDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matiereDTO))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
        Matiere testMatiere = matiereList.get(matiereList.size() - 1);
        assertThat(testMatiere.getNomEC()).isEqualTo(UPDATED_NOM_EC);
        assertThat(testMatiere.getCoeff()).isEqualTo(UPDATED_COEFF);
    }

    @Test
    @Transactional
    void putNonExistingMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matiereDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiereDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatiereWithPatch() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();

        // Update the matiere using partial update
        Matiere partialUpdatedMatiere = new Matiere();
        partialUpdatedMatiere.setId(matiere.getId());

        partialUpdatedMatiere.coeff(UPDATED_COEFF);

        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatiere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatiere))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
        Matiere testMatiere = matiereList.get(matiereList.size() - 1);
        assertThat(testMatiere.getNomEC()).isEqualTo(DEFAULT_NOM_EC);
        assertThat(testMatiere.getCoeff()).isEqualTo(UPDATED_COEFF);
    }

    @Test
    @Transactional
    void fullUpdateMatiereWithPatch() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();

        // Update the matiere using partial update
        Matiere partialUpdatedMatiere = new Matiere();
        partialUpdatedMatiere.setId(matiere.getId());

        partialUpdatedMatiere.nomEC(UPDATED_NOM_EC).coeff(UPDATED_COEFF);

        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatiere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatiere))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
        Matiere testMatiere = matiereList.get(matiereList.size() - 1);
        assertThat(testMatiere.getNomEC()).isEqualTo(UPDATED_NOM_EC);
        assertThat(testMatiere.getCoeff()).isEqualTo(UPDATED_COEFF);
    }

    @Test
    @Transactional
    void patchNonExistingMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matiereDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(matiereDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        int databaseSizeBeforeDelete = matiereRepository.findAll().size();

        // Delete the matiere
        restMatiereMockMvc
            .perform(delete(ENTITY_API_URL_ID, matiere.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
