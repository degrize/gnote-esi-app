package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Bulletin;
import com.esi.gnote.repository.BulletinRepository;
import com.esi.gnote.service.BulletinService;
import com.esi.gnote.service.dto.BulletinDTO;
import com.esi.gnote.service.mapper.BulletinMapper;
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
 * Integration tests for the {@link BulletinResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BulletinResourceIT {

    private static final String DEFAULT_SIGNATURE_DG = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATURE_DG = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bulletins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BulletinRepository bulletinRepository;

    @Mock
    private BulletinRepository bulletinRepositoryMock;

    @Autowired
    private BulletinMapper bulletinMapper;

    @Mock
    private BulletinService bulletinServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBulletinMockMvc;

    private Bulletin bulletin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bulletin createEntity(EntityManager em) {
        Bulletin bulletin = new Bulletin().signatureDG(DEFAULT_SIGNATURE_DG).observation(DEFAULT_OBSERVATION);
        return bulletin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bulletin createUpdatedEntity(EntityManager em) {
        Bulletin bulletin = new Bulletin().signatureDG(UPDATED_SIGNATURE_DG).observation(UPDATED_OBSERVATION);
        return bulletin;
    }

    @BeforeEach
    public void initTest() {
        bulletin = createEntity(em);
    }

    @Test
    @Transactional
    void createBulletin() throws Exception {
        int databaseSizeBeforeCreate = bulletinRepository.findAll().size();
        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);
        restBulletinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulletinDTO)))
            .andExpect(status().isCreated());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeCreate + 1);
        Bulletin testBulletin = bulletinList.get(bulletinList.size() - 1);
        assertThat(testBulletin.getSignatureDG()).isEqualTo(DEFAULT_SIGNATURE_DG);
        assertThat(testBulletin.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void createBulletinWithExistingId() throws Exception {
        // Create the Bulletin with an existing ID
        bulletin.setId(1L);
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        int databaseSizeBeforeCreate = bulletinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBulletinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulletinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSignatureDGIsRequired() throws Exception {
        int databaseSizeBeforeTest = bulletinRepository.findAll().size();
        // set the field null
        bulletin.setSignatureDG(null);

        // Create the Bulletin, which fails.
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        restBulletinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulletinDTO)))
            .andExpect(status().isBadRequest());

        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObservationIsRequired() throws Exception {
        int databaseSizeBeforeTest = bulletinRepository.findAll().size();
        // set the field null
        bulletin.setObservation(null);

        // Create the Bulletin, which fails.
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        restBulletinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulletinDTO)))
            .andExpect(status().isBadRequest());

        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBulletins() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);

        // Get all the bulletinList
        restBulletinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bulletin.getId().intValue())))
            .andExpect(jsonPath("$.[*].signatureDG").value(hasItem(DEFAULT_SIGNATURE_DG)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBulletinsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bulletinServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBulletinMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bulletinServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBulletinsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bulletinServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBulletinMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bulletinServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBulletin() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);

        // Get the bulletin
        restBulletinMockMvc
            .perform(get(ENTITY_API_URL_ID, bulletin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bulletin.getId().intValue()))
            .andExpect(jsonPath("$.signatureDG").value(DEFAULT_SIGNATURE_DG))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getNonExistingBulletin() throws Exception {
        // Get the bulletin
        restBulletinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBulletin() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);

        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();

        // Update the bulletin
        Bulletin updatedBulletin = bulletinRepository.findById(bulletin.getId()).get();
        // Disconnect from session so that the updates on updatedBulletin are not directly saved in db
        em.detach(updatedBulletin);
        updatedBulletin.signatureDG(UPDATED_SIGNATURE_DG).observation(UPDATED_OBSERVATION);
        BulletinDTO bulletinDTO = bulletinMapper.toDto(updatedBulletin);

        restBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bulletinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bulletinDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
        Bulletin testBulletin = bulletinList.get(bulletinList.size() - 1);
        assertThat(testBulletin.getSignatureDG()).isEqualTo(UPDATED_SIGNATURE_DG);
        assertThat(testBulletin.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void putNonExistingBulletin() throws Exception {
        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();
        bulletin.setId(count.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bulletinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBulletin() throws Exception {
        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();
        bulletin.setId(count.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBulletin() throws Exception {
        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();
        bulletin.setId(count.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulletinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBulletinWithPatch() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);

        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();

        // Update the bulletin using partial update
        Bulletin partialUpdatedBulletin = new Bulletin();
        partialUpdatedBulletin.setId(bulletin.getId());

        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBulletin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBulletin))
            )
            .andExpect(status().isOk());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
        Bulletin testBulletin = bulletinList.get(bulletinList.size() - 1);
        assertThat(testBulletin.getSignatureDG()).isEqualTo(DEFAULT_SIGNATURE_DG);
        assertThat(testBulletin.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdateBulletinWithPatch() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);

        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();

        // Update the bulletin using partial update
        Bulletin partialUpdatedBulletin = new Bulletin();
        partialUpdatedBulletin.setId(bulletin.getId());

        partialUpdatedBulletin.signatureDG(UPDATED_SIGNATURE_DG).observation(UPDATED_OBSERVATION);

        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBulletin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBulletin))
            )
            .andExpect(status().isOk());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
        Bulletin testBulletin = bulletinList.get(bulletinList.size() - 1);
        assertThat(testBulletin.getSignatureDG()).isEqualTo(UPDATED_SIGNATURE_DG);
        assertThat(testBulletin.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingBulletin() throws Exception {
        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();
        bulletin.setId(count.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bulletinDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBulletin() throws Exception {
        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();
        bulletin.setId(count.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBulletin() throws Exception {
        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();
        bulletin.setId(count.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bulletinDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bulletin in the database
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBulletin() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);

        int databaseSizeBeforeDelete = bulletinRepository.findAll().size();

        // Delete the bulletin
        restBulletinMockMvc
            .perform(delete(ENTITY_API_URL_ID, bulletin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bulletin> bulletinList = bulletinRepository.findAll();
        assertThat(bulletinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
