package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Absence;
import com.esi.gnote.repository.AbsenceRepository;
import com.esi.gnote.service.AbsenceService;
import com.esi.gnote.service.dto.AbsenceDTO;
import com.esi.gnote.service.mapper.AbsenceMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AbsenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AbsenceResourceIT {

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_HEURE_DEBUT = "AAAAAAAAAA";
    private static final String UPDATED_HEURE_DEBUT = "BBBBBBBBBB";

    private static final String DEFAULT_HEURE_FIN = "AAAAAAAAAA";
    private static final String UPDATED_HEURE_FIN = "BBBBBBBBBB";

    private static final String DEFAULT_JUSTIFICATION_ECRIT = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATION_ECRIT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_JUSTIFICATION_NUMERIQUE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_JUSTIFICATION_NUMERIQUE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/absences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AbsenceRepository absenceRepository;

    @Mock
    private AbsenceRepository absenceRepositoryMock;

    @Autowired
    private AbsenceMapper absenceMapper;

    @Mock
    private AbsenceService absenceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbsenceMockMvc;

    private Absence absence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Absence createEntity(EntityManager em) {
        Absence absence = new Absence()
            .etat(DEFAULT_ETAT)
            .heureDebut(DEFAULT_HEURE_DEBUT)
            .heureFin(DEFAULT_HEURE_FIN)
            .justificationEcrit(DEFAULT_JUSTIFICATION_ECRIT)
            .justificationNumerique(DEFAULT_JUSTIFICATION_NUMERIQUE)
            .justificationNumeriqueContentType(DEFAULT_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE);
        return absence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Absence createUpdatedEntity(EntityManager em) {
        Absence absence = new Absence()
            .etat(UPDATED_ETAT)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .justificationEcrit(UPDATED_JUSTIFICATION_ECRIT)
            .justificationNumerique(UPDATED_JUSTIFICATION_NUMERIQUE)
            .justificationNumeriqueContentType(UPDATED_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE);
        return absence;
    }

    @BeforeEach
    public void initTest() {
        absence = createEntity(em);
    }

    @Test
    @Transactional
    void createAbsence() throws Exception {
        int databaseSizeBeforeCreate = absenceRepository.findAll().size();
        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);
        restAbsenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(absenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeCreate + 1);
        Absence testAbsence = absenceList.get(absenceList.size() - 1);
        assertThat(testAbsence.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testAbsence.getHeureDebut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testAbsence.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);
        assertThat(testAbsence.getJustificationEcrit()).isEqualTo(DEFAULT_JUSTIFICATION_ECRIT);
        assertThat(testAbsence.getJustificationNumerique()).isEqualTo(DEFAULT_JUSTIFICATION_NUMERIQUE);
        assertThat(testAbsence.getJustificationNumeriqueContentType()).isEqualTo(DEFAULT_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createAbsenceWithExistingId() throws Exception {
        // Create the Absence with an existing ID
        absence.setId(1L);
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        int databaseSizeBeforeCreate = absenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbsenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(absenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = absenceRepository.findAll().size();
        // set the field null
        absence.setEtat(null);

        // Create the Absence, which fails.
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        restAbsenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(absenceDTO)))
            .andExpect(status().isBadRequest());

        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAbsences() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        // Get all the absenceList
        restAbsenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(absence.getId().intValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN)))
            .andExpect(jsonPath("$.[*].justificationEcrit").value(hasItem(DEFAULT_JUSTIFICATION_ECRIT)))
            .andExpect(jsonPath("$.[*].justificationNumeriqueContentType").value(hasItem(DEFAULT_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE)))
            .andExpect(
                jsonPath("$.[*].justificationNumerique").value(hasItem(Base64Utils.encodeToString(DEFAULT_JUSTIFICATION_NUMERIQUE)))
            );
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbsencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(absenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbsenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(absenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbsencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(absenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbsenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(absenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAbsence() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        // Get the absence
        restAbsenceMockMvc
            .perform(get(ENTITY_API_URL_ID, absence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(absence.getId().intValue()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.heureDebut").value(DEFAULT_HEURE_DEBUT))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN))
            .andExpect(jsonPath("$.justificationEcrit").value(DEFAULT_JUSTIFICATION_ECRIT))
            .andExpect(jsonPath("$.justificationNumeriqueContentType").value(DEFAULT_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE))
            .andExpect(jsonPath("$.justificationNumerique").value(Base64Utils.encodeToString(DEFAULT_JUSTIFICATION_NUMERIQUE)));
    }

    @Test
    @Transactional
    void getNonExistingAbsence() throws Exception {
        // Get the absence
        restAbsenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAbsence() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();

        // Update the absence
        Absence updatedAbsence = absenceRepository.findById(absence.getId()).get();
        // Disconnect from session so that the updates on updatedAbsence are not directly saved in db
        em.detach(updatedAbsence);
        updatedAbsence
            .etat(UPDATED_ETAT)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .justificationEcrit(UPDATED_JUSTIFICATION_ECRIT)
            .justificationNumerique(UPDATED_JUSTIFICATION_NUMERIQUE)
            .justificationNumeriqueContentType(UPDATED_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE);
        AbsenceDTO absenceDTO = absenceMapper.toDto(updatedAbsence);

        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, absenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
        Absence testAbsence = absenceList.get(absenceList.size() - 1);
        assertThat(testAbsence.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testAbsence.getHeureDebut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testAbsence.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
        assertThat(testAbsence.getJustificationEcrit()).isEqualTo(UPDATED_JUSTIFICATION_ECRIT);
        assertThat(testAbsence.getJustificationNumerique()).isEqualTo(UPDATED_JUSTIFICATION_NUMERIQUE);
        assertThat(testAbsence.getJustificationNumeriqueContentType()).isEqualTo(UPDATED_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(count.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, absenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(count.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(count.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(absenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAbsenceWithPatch() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();

        // Update the absence using partial update
        Absence partialUpdatedAbsence = new Absence();
        partialUpdatedAbsence.setId(absence.getId());

        partialUpdatedAbsence.etat(UPDATED_ETAT).heureFin(UPDATED_HEURE_FIN).justificationEcrit(UPDATED_JUSTIFICATION_ECRIT);

        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbsence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbsence))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
        Absence testAbsence = absenceList.get(absenceList.size() - 1);
        assertThat(testAbsence.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testAbsence.getHeureDebut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testAbsence.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
        assertThat(testAbsence.getJustificationEcrit()).isEqualTo(UPDATED_JUSTIFICATION_ECRIT);
        assertThat(testAbsence.getJustificationNumerique()).isEqualTo(DEFAULT_JUSTIFICATION_NUMERIQUE);
        assertThat(testAbsence.getJustificationNumeriqueContentType()).isEqualTo(DEFAULT_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAbsenceWithPatch() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();

        // Update the absence using partial update
        Absence partialUpdatedAbsence = new Absence();
        partialUpdatedAbsence.setId(absence.getId());

        partialUpdatedAbsence
            .etat(UPDATED_ETAT)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .justificationEcrit(UPDATED_JUSTIFICATION_ECRIT)
            .justificationNumerique(UPDATED_JUSTIFICATION_NUMERIQUE)
            .justificationNumeriqueContentType(UPDATED_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE);

        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbsence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbsence))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
        Absence testAbsence = absenceList.get(absenceList.size() - 1);
        assertThat(testAbsence.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testAbsence.getHeureDebut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testAbsence.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
        assertThat(testAbsence.getJustificationEcrit()).isEqualTo(UPDATED_JUSTIFICATION_ECRIT);
        assertThat(testAbsence.getJustificationNumerique()).isEqualTo(UPDATED_JUSTIFICATION_NUMERIQUE);
        assertThat(testAbsence.getJustificationNumeriqueContentType()).isEqualTo(UPDATED_JUSTIFICATION_NUMERIQUE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(count.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, absenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(count.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(count.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAbsence() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        int databaseSizeBeforeDelete = absenceRepository.findAll().size();

        // Delete the absence
        restAbsenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, absence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
