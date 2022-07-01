package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Classe;
import com.esi.gnote.repository.ClasseRepository;
import com.esi.gnote.service.ClasseService;
import com.esi.gnote.service.dto.ClasseDTO;
import com.esi.gnote.service.mapper.ClasseMapper;
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
 * Integration tests for the {@link ClasseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClasseResourceIT {

    private static final String DEFAULT_NOM_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CLASSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClasseRepository classeRepository;

    @Mock
    private ClasseRepository classeRepositoryMock;

    @Autowired
    private ClasseMapper classeMapper;

    @Mock
    private ClasseService classeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClasseMockMvc;

    private Classe classe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createEntity(EntityManager em) {
        Classe classe = new Classe().nomClasse(DEFAULT_NOM_CLASSE);
        return classe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createUpdatedEntity(EntityManager em) {
        Classe classe = new Classe().nomClasse(UPDATED_NOM_CLASSE);
        return classe;
    }

    @BeforeEach
    public void initTest() {
        classe = createEntity(em);
    }

    @Test
    @Transactional
    void createClasse() throws Exception {
        int databaseSizeBeforeCreate = classeRepository.findAll().size();
        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);
        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isCreated());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeCreate + 1);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getNomClasse()).isEqualTo(DEFAULT_NOM_CLASSE);
    }

    @Test
    @Transactional
    void createClasseWithExistingId() throws Exception {
        // Create the Classe with an existing ID
        classe.setId(1L);
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        int databaseSizeBeforeCreate = classeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomClasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = classeRepository.findAll().size();
        // set the field null
        classe.setNomClasse(null);

        // Create the Classe, which fails.
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isBadRequest());

        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClasses() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomClasse").value(hasItem(DEFAULT_NOM_CLASSE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassesWithEagerRelationshipsIsEnabled() throws Exception {
        when(classeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClasseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(classeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClasseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get the classe
        restClasseMockMvc
            .perform(get(ENTITY_API_URL_ID, classe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classe.getId().intValue()))
            .andExpect(jsonPath("$.nomClasse").value(DEFAULT_NOM_CLASSE));
    }

    @Test
    @Transactional
    void getNonExistingClasse() throws Exception {
        // Get the classe
        restClasseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe
        Classe updatedClasse = classeRepository.findById(classe.getId()).get();
        // Disconnect from session so that the updates on updatedClasse are not directly saved in db
        em.detach(updatedClasse);
        updatedClasse.nomClasse(UPDATED_NOM_CLASSE);
        ClasseDTO classeDTO = classeMapper.toDto(updatedClasse);

        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getNomClasse()).isEqualTo(UPDATED_NOM_CLASSE);
    }

    @Test
    @Transactional
    void putNonExistingClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClasseWithPatch() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe using partial update
        Classe partialUpdatedClasse = new Classe();
        partialUpdatedClasse.setId(classe.getId());

        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getNomClasse()).isEqualTo(DEFAULT_NOM_CLASSE);
    }

    @Test
    @Transactional
    void fullUpdateClasseWithPatch() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe using partial update
        Classe partialUpdatedClasse = new Classe();
        partialUpdatedClasse.setId(classe.getId());

        partialUpdatedClasse.nomClasse(UPDATED_NOM_CLASSE);

        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getNomClasse()).isEqualTo(UPDATED_NOM_CLASSE);
    }

    @Test
    @Transactional
    void patchNonExistingClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // Create the Classe
        ClasseDTO classeDTO = classeMapper.toDto(classe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeDelete = classeRepository.findAll().size();

        // Delete the classe
        restClasseMockMvc
            .perform(delete(ENTITY_API_URL_ID, classe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
