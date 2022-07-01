package com.esi.gnote.web.rest;

import com.esi.gnote.repository.PlancheRepository;
import com.esi.gnote.service.PlancheService;
import com.esi.gnote.service.dto.PlancheDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.Planche}.
 */
@RestController
@RequestMapping("/api")
public class PlancheResource {

    private final Logger log = LoggerFactory.getLogger(PlancheResource.class);

    private static final String ENTITY_NAME = "planche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlancheService plancheService;

    private final PlancheRepository plancheRepository;

    public PlancheResource(PlancheService plancheService, PlancheRepository plancheRepository) {
        this.plancheService = plancheService;
        this.plancheRepository = plancheRepository;
    }

    /**
     * {@code POST  /planches} : Create a new planche.
     *
     * @param plancheDTO the plancheDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plancheDTO, or with status {@code 400 (Bad Request)} if the planche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/planches")
    public ResponseEntity<PlancheDTO> createPlanche(@Valid @RequestBody PlancheDTO plancheDTO) throws URISyntaxException {
        log.debug("REST request to save Planche : {}", plancheDTO);
        if (plancheDTO.getId() != null) {
            throw new BadRequestAlertException("A new planche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlancheDTO result = plancheService.save(plancheDTO);
        return ResponseEntity
            .created(new URI("/api/planches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /planches/:id} : Updates an existing planche.
     *
     * @param id the id of the plancheDTO to save.
     * @param plancheDTO the plancheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plancheDTO,
     * or with status {@code 400 (Bad Request)} if the plancheDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plancheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/planches/{id}")
    public ResponseEntity<PlancheDTO> updatePlanche(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlancheDTO plancheDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Planche : {}, {}", id, plancheDTO);
        if (plancheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plancheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plancheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlancheDTO result = plancheService.update(plancheDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plancheDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /planches/:id} : Partial updates given fields of an existing planche, field will ignore if it is null
     *
     * @param id the id of the plancheDTO to save.
     * @param plancheDTO the plancheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plancheDTO,
     * or with status {@code 400 (Bad Request)} if the plancheDTO is not valid,
     * or with status {@code 404 (Not Found)} if the plancheDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the plancheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/planches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlancheDTO> partialUpdatePlanche(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlancheDTO plancheDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Planche partially : {}, {}", id, plancheDTO);
        if (plancheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plancheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plancheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlancheDTO> result = plancheService.partialUpdate(plancheDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plancheDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /planches} : get all the planches.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planches in body.
     */
    @GetMapping("/planches")
    public ResponseEntity<List<PlancheDTO>> getAllPlanches(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Planches");
        Page<PlancheDTO> page;
        if (eagerload) {
            page = plancheService.findAllWithEagerRelationships(pageable);
        } else {
            page = plancheService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /planches/:id} : get the "id" planche.
     *
     * @param id the id of the plancheDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plancheDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planches/{id}")
    public ResponseEntity<PlancheDTO> getPlanche(@PathVariable Long id) {
        log.debug("REST request to get Planche : {}", id);
        Optional<PlancheDTO> plancheDTO = plancheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plancheDTO);
    }

    /**
     * {@code DELETE  /planches/:id} : delete the "id" planche.
     *
     * @param id the id of the plancheDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/planches/{id}")
    public ResponseEntity<Void> deletePlanche(@PathVariable Long id) {
        log.debug("REST request to delete Planche : {}", id);
        plancheService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
