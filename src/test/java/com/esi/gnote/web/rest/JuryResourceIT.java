package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Jury;
import com.esi.gnote.repository.JuryRepository;
import com.esi.gnote.service.JuryService;
import com.esi.gnote.service.dto.JuryDTO;
import com.esi.gnote.service.mapper.JuryMapper;
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
 * Integration tests for the {@link JuryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JuryResourceIT {

    private static final String DEFAULT_PRESIDENT_JURY = "AAAAAAAAAA";
    private static final String UPDATED_PRESIDENT_JURY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/juries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JuryRepository juryRepository;

    @Mock
    private JuryRepository juryRepositoryMock;

    @Autowired
    private JuryMapper juryMapper;

    @Mock
    private JuryService juryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJuryMockMvc;

    private Jury jury;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jury createEntity(EntityManager em) {
        Jury jury = new Jury().presidentJury(DEFAULT_PRESIDENT_JURY);
        return jury;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jury createUpdatedEntity(EntityManager em) {
        Jury jury = new Jury().presidentJury(UPDATED_PRESIDENT_JURY);
        return jury;
    }

    @BeforeEach
    public void initTest() {
        jury = createEntity(em);
    }

    @Test
    @Transactional
    void createJury() throws Exception {
        int databaseSizeBeforeCreate = juryRepository.findAll().size();
        // Create the Jury
        JuryDTO juryDTO = juryMapper.toDto(jury);
        restJuryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(juryDTO)))
            .andExpect(status().isCreated());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeCreate + 1);
        Jury testJury = juryList.get(juryList.size() - 1);
        assertThat(testJury.getPresidentJury()).isEqualTo(DEFAULT_PRESIDENT_JURY);
    }

    @Test
    @Transactional
    void createJuryWithExistingId() throws Exception {
        // Create the Jury with an existing ID
        jury.setId(1L);
        JuryDTO juryDTO = juryMapper.toDto(jury);

        int databaseSizeBeforeCreate = juryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJuryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(juryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPresidentJuryIsRequired() throws Exception {
        int databaseSizeBeforeTest = juryRepository.findAll().size();
        // set the field null
        jury.setPresidentJury(null);

        // Create the Jury, which fails.
        JuryDTO juryDTO = juryMapper.toDto(jury);

        restJuryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(juryDTO)))
            .andExpect(status().isBadRequest());

        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJuries() throws Exception {
        // Initialize the database
        juryRepository.saveAndFlush(jury);

        // Get all the juryList
        restJuryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jury.getId().intValue())))
            .andExpect(jsonPath("$.[*].presidentJury").value(hasItem(DEFAULT_PRESIDENT_JURY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJuriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(juryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJuryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(juryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJuriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(juryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJuryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(juryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getJury() throws Exception {
        // Initialize the database
        juryRepository.saveAndFlush(jury);

        // Get the jury
        restJuryMockMvc
            .perform(get(ENTITY_API_URL_ID, jury.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jury.getId().intValue()))
            .andExpect(jsonPath("$.presidentJury").value(DEFAULT_PRESIDENT_JURY));
    }

    @Test
    @Transactional
    void getNonExistingJury() throws Exception {
        // Get the jury
        restJuryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJury() throws Exception {
        // Initialize the database
        juryRepository.saveAndFlush(jury);

        int databaseSizeBeforeUpdate = juryRepository.findAll().size();

        // Update the jury
        Jury updatedJury = juryRepository.findById(jury.getId()).get();
        // Disconnect from session so that the updates on updatedJury are not directly saved in db
        em.detach(updatedJury);
        updatedJury.presidentJury(UPDATED_PRESIDENT_JURY);
        JuryDTO juryDTO = juryMapper.toDto(updatedJury);

        restJuryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, juryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(juryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
        Jury testJury = juryList.get(juryList.size() - 1);
        assertThat(testJury.getPresidentJury()).isEqualTo(UPDATED_PRESIDENT_JURY);
    }

    @Test
    @Transactional
    void putNonExistingJury() throws Exception {
        int databaseSizeBeforeUpdate = juryRepository.findAll().size();
        jury.setId(count.incrementAndGet());

        // Create the Jury
        JuryDTO juryDTO = juryMapper.toDto(jury);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJuryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, juryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(juryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJury() throws Exception {
        int databaseSizeBeforeUpdate = juryRepository.findAll().size();
        jury.setId(count.incrementAndGet());

        // Create the Jury
        JuryDTO juryDTO = juryMapper.toDto(jury);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJuryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(juryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJury() throws Exception {
        int databaseSizeBeforeUpdate = juryRepository.findAll().size();
        jury.setId(count.incrementAndGet());

        // Create the Jury
        JuryDTO juryDTO = juryMapper.toDto(jury);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJuryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(juryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJuryWithPatch() throws Exception {
        // Initialize the database
        juryRepository.saveAndFlush(jury);

        int databaseSizeBeforeUpdate = juryRepository.findAll().size();

        // Update the jury using partial update
        Jury partialUpdatedJury = new Jury();
        partialUpdatedJury.setId(jury.getId());

        restJuryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJury.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJury))
            )
            .andExpect(status().isOk());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
        Jury testJury = juryList.get(juryList.size() - 1);
        assertThat(testJury.getPresidentJury()).isEqualTo(DEFAULT_PRESIDENT_JURY);
    }

    @Test
    @Transactional
    void fullUpdateJuryWithPatch() throws Exception {
        // Initialize the database
        juryRepository.saveAndFlush(jury);

        int databaseSizeBeforeUpdate = juryRepository.findAll().size();

        // Update the jury using partial update
        Jury partialUpdatedJury = new Jury();
        partialUpdatedJury.setId(jury.getId());

        partialUpdatedJury.presidentJury(UPDATED_PRESIDENT_JURY);

        restJuryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJury.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJury))
            )
            .andExpect(status().isOk());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
        Jury testJury = juryList.get(juryList.size() - 1);
        assertThat(testJury.getPresidentJury()).isEqualTo(UPDATED_PRESIDENT_JURY);
    }

    @Test
    @Transactional
    void patchNonExistingJury() throws Exception {
        int databaseSizeBeforeUpdate = juryRepository.findAll().size();
        jury.setId(count.incrementAndGet());

        // Create the Jury
        JuryDTO juryDTO = juryMapper.toDto(jury);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJuryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, juryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(juryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJury() throws Exception {
        int databaseSizeBeforeUpdate = juryRepository.findAll().size();
        jury.setId(count.incrementAndGet());

        // Create the Jury
        JuryDTO juryDTO = juryMapper.toDto(jury);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJuryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(juryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJury() throws Exception {
        int databaseSizeBeforeUpdate = juryRepository.findAll().size();
        jury.setId(count.incrementAndGet());

        // Create the Jury
        JuryDTO juryDTO = juryMapper.toDto(jury);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJuryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(juryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Jury in the database
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJury() throws Exception {
        // Initialize the database
        juryRepository.saveAndFlush(jury);

        int databaseSizeBeforeDelete = juryRepository.findAll().size();

        // Delete the jury
        restJuryMockMvc
            .perform(delete(ENTITY_API_URL_ID, jury.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Jury> juryList = juryRepository.findAll();
        assertThat(juryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
