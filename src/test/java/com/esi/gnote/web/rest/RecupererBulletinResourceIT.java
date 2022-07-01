package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.RecupererBulletin;
import com.esi.gnote.repository.RecupererBulletinRepository;
import com.esi.gnote.service.RecupererBulletinService;
import com.esi.gnote.service.dto.RecupererBulletinDTO;
import com.esi.gnote.service.mapper.RecupererBulletinMapper;
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
 * Integration tests for the {@link RecupererBulletinResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecupererBulletinResourceIT {

    private static final String DEFAULT_SIGNATURE_ELEVE = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATURE_ELEVE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_BULLETIN_SCANNE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BULLETIN_SCANNE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BULLETIN_SCANNE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BULLETIN_SCANNE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/recuperer-bulletins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecupererBulletinRepository recupererBulletinRepository;

    @Mock
    private RecupererBulletinRepository recupererBulletinRepositoryMock;

    @Autowired
    private RecupererBulletinMapper recupererBulletinMapper;

    @Mock
    private RecupererBulletinService recupererBulletinServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecupererBulletinMockMvc;

    private RecupererBulletin recupererBulletin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecupererBulletin createEntity(EntityManager em) {
        RecupererBulletin recupererBulletin = new RecupererBulletin()
            .signatureEleve(DEFAULT_SIGNATURE_ELEVE)
            .bulletinScanne(DEFAULT_BULLETIN_SCANNE)
            .bulletinScanneContentType(DEFAULT_BULLETIN_SCANNE_CONTENT_TYPE);
        return recupererBulletin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecupererBulletin createUpdatedEntity(EntityManager em) {
        RecupererBulletin recupererBulletin = new RecupererBulletin()
            .signatureEleve(UPDATED_SIGNATURE_ELEVE)
            .bulletinScanne(UPDATED_BULLETIN_SCANNE)
            .bulletinScanneContentType(UPDATED_BULLETIN_SCANNE_CONTENT_TYPE);
        return recupererBulletin;
    }

    @BeforeEach
    public void initTest() {
        recupererBulletin = createEntity(em);
    }

    @Test
    @Transactional
    void createRecupererBulletin() throws Exception {
        int databaseSizeBeforeCreate = recupererBulletinRepository.findAll().size();
        // Create the RecupererBulletin
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);
        restRecupererBulletinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeCreate + 1);
        RecupererBulletin testRecupererBulletin = recupererBulletinList.get(recupererBulletinList.size() - 1);
        assertThat(testRecupererBulletin.getSignatureEleve()).isEqualTo(DEFAULT_SIGNATURE_ELEVE);
        assertThat(testRecupererBulletin.getBulletinScanne()).isEqualTo(DEFAULT_BULLETIN_SCANNE);
        assertThat(testRecupererBulletin.getBulletinScanneContentType()).isEqualTo(DEFAULT_BULLETIN_SCANNE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createRecupererBulletinWithExistingId() throws Exception {
        // Create the RecupererBulletin with an existing ID
        recupererBulletin.setId(1L);
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);

        int databaseSizeBeforeCreate = recupererBulletinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecupererBulletinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSignatureEleveIsRequired() throws Exception {
        int databaseSizeBeforeTest = recupererBulletinRepository.findAll().size();
        // set the field null
        recupererBulletin.setSignatureEleve(null);

        // Create the RecupererBulletin, which fails.
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);

        restRecupererBulletinMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecupererBulletins() throws Exception {
        // Initialize the database
        recupererBulletinRepository.saveAndFlush(recupererBulletin);

        // Get all the recupererBulletinList
        restRecupererBulletinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recupererBulletin.getId().intValue())))
            .andExpect(jsonPath("$.[*].signatureEleve").value(hasItem(DEFAULT_SIGNATURE_ELEVE)))
            .andExpect(jsonPath("$.[*].bulletinScanneContentType").value(hasItem(DEFAULT_BULLETIN_SCANNE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bulletinScanne").value(hasItem(Base64Utils.encodeToString(DEFAULT_BULLETIN_SCANNE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecupererBulletinsWithEagerRelationshipsIsEnabled() throws Exception {
        when(recupererBulletinServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecupererBulletinMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recupererBulletinServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecupererBulletinsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recupererBulletinServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecupererBulletinMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recupererBulletinServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRecupererBulletin() throws Exception {
        // Initialize the database
        recupererBulletinRepository.saveAndFlush(recupererBulletin);

        // Get the recupererBulletin
        restRecupererBulletinMockMvc
            .perform(get(ENTITY_API_URL_ID, recupererBulletin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recupererBulletin.getId().intValue()))
            .andExpect(jsonPath("$.signatureEleve").value(DEFAULT_SIGNATURE_ELEVE))
            .andExpect(jsonPath("$.bulletinScanneContentType").value(DEFAULT_BULLETIN_SCANNE_CONTENT_TYPE))
            .andExpect(jsonPath("$.bulletinScanne").value(Base64Utils.encodeToString(DEFAULT_BULLETIN_SCANNE)));
    }

    @Test
    @Transactional
    void getNonExistingRecupererBulletin() throws Exception {
        // Get the recupererBulletin
        restRecupererBulletinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecupererBulletin() throws Exception {
        // Initialize the database
        recupererBulletinRepository.saveAndFlush(recupererBulletin);

        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();

        // Update the recupererBulletin
        RecupererBulletin updatedRecupererBulletin = recupererBulletinRepository.findById(recupererBulletin.getId()).get();
        // Disconnect from session so that the updates on updatedRecupererBulletin are not directly saved in db
        em.detach(updatedRecupererBulletin);
        updatedRecupererBulletin
            .signatureEleve(UPDATED_SIGNATURE_ELEVE)
            .bulletinScanne(UPDATED_BULLETIN_SCANNE)
            .bulletinScanneContentType(UPDATED_BULLETIN_SCANNE_CONTENT_TYPE);
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(updatedRecupererBulletin);

        restRecupererBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recupererBulletinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
        RecupererBulletin testRecupererBulletin = recupererBulletinList.get(recupererBulletinList.size() - 1);
        assertThat(testRecupererBulletin.getSignatureEleve()).isEqualTo(UPDATED_SIGNATURE_ELEVE);
        assertThat(testRecupererBulletin.getBulletinScanne()).isEqualTo(UPDATED_BULLETIN_SCANNE);
        assertThat(testRecupererBulletin.getBulletinScanneContentType()).isEqualTo(UPDATED_BULLETIN_SCANNE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingRecupererBulletin() throws Exception {
        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();
        recupererBulletin.setId(count.incrementAndGet());

        // Create the RecupererBulletin
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecupererBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recupererBulletinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecupererBulletin() throws Exception {
        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();
        recupererBulletin.setId(count.incrementAndGet());

        // Create the RecupererBulletin
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecupererBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecupererBulletin() throws Exception {
        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();
        recupererBulletin.setId(count.incrementAndGet());

        // Create the RecupererBulletin
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecupererBulletinMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecupererBulletinWithPatch() throws Exception {
        // Initialize the database
        recupererBulletinRepository.saveAndFlush(recupererBulletin);

        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();

        // Update the recupererBulletin using partial update
        RecupererBulletin partialUpdatedRecupererBulletin = new RecupererBulletin();
        partialUpdatedRecupererBulletin.setId(recupererBulletin.getId());

        partialUpdatedRecupererBulletin.signatureEleve(UPDATED_SIGNATURE_ELEVE);

        restRecupererBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecupererBulletin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecupererBulletin))
            )
            .andExpect(status().isOk());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
        RecupererBulletin testRecupererBulletin = recupererBulletinList.get(recupererBulletinList.size() - 1);
        assertThat(testRecupererBulletin.getSignatureEleve()).isEqualTo(UPDATED_SIGNATURE_ELEVE);
        assertThat(testRecupererBulletin.getBulletinScanne()).isEqualTo(DEFAULT_BULLETIN_SCANNE);
        assertThat(testRecupererBulletin.getBulletinScanneContentType()).isEqualTo(DEFAULT_BULLETIN_SCANNE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRecupererBulletinWithPatch() throws Exception {
        // Initialize the database
        recupererBulletinRepository.saveAndFlush(recupererBulletin);

        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();

        // Update the recupererBulletin using partial update
        RecupererBulletin partialUpdatedRecupererBulletin = new RecupererBulletin();
        partialUpdatedRecupererBulletin.setId(recupererBulletin.getId());

        partialUpdatedRecupererBulletin
            .signatureEleve(UPDATED_SIGNATURE_ELEVE)
            .bulletinScanne(UPDATED_BULLETIN_SCANNE)
            .bulletinScanneContentType(UPDATED_BULLETIN_SCANNE_CONTENT_TYPE);

        restRecupererBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecupererBulletin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecupererBulletin))
            )
            .andExpect(status().isOk());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
        RecupererBulletin testRecupererBulletin = recupererBulletinList.get(recupererBulletinList.size() - 1);
        assertThat(testRecupererBulletin.getSignatureEleve()).isEqualTo(UPDATED_SIGNATURE_ELEVE);
        assertThat(testRecupererBulletin.getBulletinScanne()).isEqualTo(UPDATED_BULLETIN_SCANNE);
        assertThat(testRecupererBulletin.getBulletinScanneContentType()).isEqualTo(UPDATED_BULLETIN_SCANNE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRecupererBulletin() throws Exception {
        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();
        recupererBulletin.setId(count.incrementAndGet());

        // Create the RecupererBulletin
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecupererBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recupererBulletinDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecupererBulletin() throws Exception {
        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();
        recupererBulletin.setId(count.incrementAndGet());

        // Create the RecupererBulletin
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecupererBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecupererBulletin() throws Exception {
        int databaseSizeBeforeUpdate = recupererBulletinRepository.findAll().size();
        recupererBulletin.setId(count.incrementAndGet());

        // Create the RecupererBulletin
        RecupererBulletinDTO recupererBulletinDTO = recupererBulletinMapper.toDto(recupererBulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecupererBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recupererBulletinDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecupererBulletin in the database
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecupererBulletin() throws Exception {
        // Initialize the database
        recupererBulletinRepository.saveAndFlush(recupererBulletin);

        int databaseSizeBeforeDelete = recupererBulletinRepository.findAll().size();

        // Delete the recupererBulletin
        restRecupererBulletinMockMvc
            .perform(delete(ENTITY_API_URL_ID, recupererBulletin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecupererBulletin> recupererBulletinList = recupererBulletinRepository.findAll();
        assertThat(recupererBulletinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
