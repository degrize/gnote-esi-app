package com.esi.gnote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.esi.gnote.IntegrationTest;
import com.esi.gnote.domain.Module;
import com.esi.gnote.repository.ModuleRepository;
import com.esi.gnote.service.dto.ModuleDTO;
import com.esi.gnote.service.mapper.ModuleMapper;
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
 * Integration tests for the {@link ModuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModuleResourceIT {

    private static final String DEFAULT_NOM_UE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_UE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/modules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModuleMockMvc;

    private Module module;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createEntity(EntityManager em) {
        Module module = new Module().nomUE(DEFAULT_NOM_UE);
        return module;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createUpdatedEntity(EntityManager em) {
        Module module = new Module().nomUE(UPDATED_NOM_UE);
        return module;
    }

    @BeforeEach
    public void initTest() {
        module = createEntity(em);
    }

    @Test
    @Transactional
    void createModule() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().size();
        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);
        restModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moduleDTO)))
            .andExpect(status().isCreated());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate + 1);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getNomUE()).isEqualTo(DEFAULT_NOM_UE);
    }

    @Test
    @Transactional
    void createModuleWithExistingId() throws Exception {
        // Create the Module with an existing ID
        module.setId(1L);
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        int databaseSizeBeforeCreate = moduleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllModules() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList
        restModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomUE").value(hasItem(DEFAULT_NOM_UE)));
    }

    @Test
    @Transactional
    void getModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get the module
        restModuleMockMvc
            .perform(get(ENTITY_API_URL_ID, module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(module.getId().intValue()))
            .andExpect(jsonPath("$.nomUE").value(DEFAULT_NOM_UE));
    }

    @Test
    @Transactional
    void getNonExistingModule() throws Exception {
        // Get the module
        restModuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module
        Module updatedModule = moduleRepository.findById(module.getId()).get();
        // Disconnect from session so that the updates on updatedModule are not directly saved in db
        em.detach(updatedModule);
        updatedModule.nomUE(UPDATED_NOM_UE);
        ModuleDTO moduleDTO = moduleMapper.toDto(updatedModule);

        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getNomUE()).isEqualTo(UPDATED_NOM_UE);
    }

    @Test
    @Transactional
    void putNonExistingModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule.nomUE(UPDATED_NOM_UE);

        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModule))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getNomUE()).isEqualTo(UPDATED_NOM_UE);
    }

    @Test
    @Transactional
    void fullUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule.nomUE(UPDATED_NOM_UE);

        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModule))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getNomUE()).isEqualTo(UPDATED_NOM_UE);
    }

    @Test
    @Transactional
    void patchNonExistingModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(moduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        int databaseSizeBeforeDelete = moduleRepository.findAll().size();

        // Delete the module
        restModuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, module.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
