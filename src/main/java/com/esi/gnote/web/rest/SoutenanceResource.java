package com.esi.gnote.web.rest;

import com.esi.gnote.repository.SoutenanceRepository;
import com.esi.gnote.service.SoutenanceService;
import com.esi.gnote.service.dto.SoutenanceDTO;
import com.esi.gnote.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.esi.gnote.domain.Soutenance}.
 */
@RestController
@RequestMapping("/api")
public class SoutenanceResource {

    private final Logger log = LoggerFactory.getLogger(SoutenanceResource.class);

    private static final String ENTITY_NAME = "soutenance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoutenanceService soutenanceService;

    private final SoutenanceRepository soutenanceRepository;

    public SoutenanceResource(SoutenanceService soutenanceService, SoutenanceRepository soutenanceRepository) {
        this.soutenanceService = soutenanceService;
        this.soutenanceRepository = soutenanceRepository;
    }

    /**
     * {@code POST  /soutenances} : Create a new soutenance.
     *
     * @param soutenanceDTO the soutenanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new soutenanceDTO, or with status {@code 400 (Bad Request)} if the soutenance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/soutenances")
    public ResponseEntity<SoutenanceDTO> createSoutenance(@Valid @RequestBody SoutenanceDTO soutenanceDTO) throws URISyntaxException {
        log.debug("REST request to save Soutenance : {}", soutenanceDTO);
        if (soutenanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new soutenance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoutenanceDTO result = soutenanceService.save(soutenanceDTO);
        return ResponseEntity
            .created(new URI("/api/soutenances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /soutenances/:id} : Updates an existing soutenance.
     *
     * @param id the id of the soutenanceDTO to save.
     * @param soutenanceDTO the soutenanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soutenanceDTO,
     * or with status {@code 400 (Bad Request)} if the soutenanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the soutenanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/soutenances/{id}")
    public ResponseEntity<SoutenanceDTO> updateSoutenance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SoutenanceDTO soutenanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Soutenance : {}, {}", id, soutenanceDTO);
        if (soutenanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soutenanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soutenanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SoutenanceDTO result = soutenanceService.update(soutenanceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soutenanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /soutenances/:id} : Partial updates given fields of an existing soutenance, field will ignore if it is null
     *
     * @param id the id of the soutenanceDTO to save.
     * @param soutenanceDTO the soutenanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soutenanceDTO,
     * or with status {@code 400 (Bad Request)} if the soutenanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the soutenanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the soutenanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/soutenances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SoutenanceDTO> partialUpdateSoutenance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SoutenanceDTO soutenanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Soutenance partially : {}, {}", id, soutenanceDTO);
        if (soutenanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soutenanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soutenanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SoutenanceDTO> result = soutenanceService.partialUpdate(soutenanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soutenanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /soutenances} : get all the soutenances.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of soutenances in body.
     */
    @GetMapping("/soutenances")
    public ResponseEntity<List<SoutenanceDTO>> getAllSoutenances(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Soutenances");
        Page<SoutenanceDTO> page;
        if (eagerload) {
            page = soutenanceService.findAllWithEagerRelationships(pageable);
        } else {
            page = soutenanceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /soutenances/:id} : get the "id" soutenance.
     *
     * @param id the id of the soutenanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the soutenanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/soutenances/{id}")
    public ResponseEntity<SoutenanceDTO> getSoutenance(@PathVariable Long id) {
        log.debug("REST request to get Soutenance : {}", id);
        Optional<SoutenanceDTO> soutenanceDTO = soutenanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(soutenanceDTO);
    }

    /**
     * {@code DELETE  /soutenances/:id} : delete the "id" soutenance.
     *
     * @param id the id of the soutenanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/soutenances/{id}")
    public ResponseEntity<Void> deleteSoutenance(@PathVariable Long id) {
        log.debug("REST request to delete Soutenance : {}", id);
        soutenanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
