package com.esi.gnote.web.rest;

import com.esi.gnote.repository.CycleRepository;
import com.esi.gnote.service.CycleService;
import com.esi.gnote.service.dto.CycleDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.Cycle}.
 */
@RestController
@RequestMapping("/api")
public class CycleResource {

    private final Logger log = LoggerFactory.getLogger(CycleResource.class);

    private static final String ENTITY_NAME = "cycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CycleService cycleService;

    private final CycleRepository cycleRepository;

    public CycleResource(CycleService cycleService, CycleRepository cycleRepository) {
        this.cycleService = cycleService;
        this.cycleRepository = cycleRepository;
    }

    /**
     * {@code POST  /cycles} : Create a new cycle.
     *
     * @param cycleDTO the cycleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cycleDTO, or with status {@code 400 (Bad Request)} if the cycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cycles")
    public ResponseEntity<CycleDTO> createCycle(@Valid @RequestBody CycleDTO cycleDTO) throws URISyntaxException {
        log.debug("REST request to save Cycle : {}", cycleDTO);
        if (cycleDTO.getId() != null) {
            throw new BadRequestAlertException("A new cycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CycleDTO result = cycleService.save(cycleDTO);
        return ResponseEntity
            .created(new URI("/api/cycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cycles/:id} : Updates an existing cycle.
     *
     * @param id the id of the cycleDTO to save.
     * @param cycleDTO the cycleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycleDTO,
     * or with status {@code 400 (Bad Request)} if the cycleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cycleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cycles/{id}")
    public ResponseEntity<CycleDTO> updateCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CycleDTO cycleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cycle : {}, {}", id, cycleDTO);
        if (cycleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CycleDTO result = cycleService.update(cycleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cycleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cycles/:id} : Partial updates given fields of an existing cycle, field will ignore if it is null
     *
     * @param id the id of the cycleDTO to save.
     * @param cycleDTO the cycleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycleDTO,
     * or with status {@code 400 (Bad Request)} if the cycleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cycleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cycleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cycles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CycleDTO> partialUpdateCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CycleDTO cycleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cycle partially : {}, {}", id, cycleDTO);
        if (cycleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CycleDTO> result = cycleService.partialUpdate(cycleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cycleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cycles} : get all the cycles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cycles in body.
     */
    @GetMapping("/cycles")
    public ResponseEntity<List<CycleDTO>> getAllCycles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cycles");
        Page<CycleDTO> page = cycleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cycles/:id} : get the "id" cycle.
     *
     * @param id the id of the cycleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cycleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cycles/{id}")
    public ResponseEntity<CycleDTO> getCycle(@PathVariable Long id) {
        log.debug("REST request to get Cycle : {}", id);
        Optional<CycleDTO> cycleDTO = cycleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cycleDTO);
    }

    /**
     * {@code DELETE  /cycles/:id} : delete the "id" cycle.
     *
     * @param id the id of the cycleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cycles/{id}")
    public ResponseEntity<Void> deleteCycle(@PathVariable Long id) {
        log.debug("REST request to delete Cycle : {}", id);
        cycleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
