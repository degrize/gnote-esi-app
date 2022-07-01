package com.esi.gnote.web.rest;

import com.esi.gnote.repository.RecupererBulletinRepository;
import com.esi.gnote.service.RecupererBulletinService;
import com.esi.gnote.service.dto.RecupererBulletinDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.RecupererBulletin}.
 */
@RestController
@RequestMapping("/api")
public class RecupererBulletinResource {

    private final Logger log = LoggerFactory.getLogger(RecupererBulletinResource.class);

    private static final String ENTITY_NAME = "recupererBulletin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecupererBulletinService recupererBulletinService;

    private final RecupererBulletinRepository recupererBulletinRepository;

    public RecupererBulletinResource(
        RecupererBulletinService recupererBulletinService,
        RecupererBulletinRepository recupererBulletinRepository
    ) {
        this.recupererBulletinService = recupererBulletinService;
        this.recupererBulletinRepository = recupererBulletinRepository;
    }

    /**
     * {@code POST  /recuperer-bulletins} : Create a new recupererBulletin.
     *
     * @param recupererBulletinDTO the recupererBulletinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recupererBulletinDTO, or with status {@code 400 (Bad Request)} if the recupererBulletin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recuperer-bulletins")
    public ResponseEntity<RecupererBulletinDTO> createRecupererBulletin(@Valid @RequestBody RecupererBulletinDTO recupererBulletinDTO)
        throws URISyntaxException {
        log.debug("REST request to save RecupererBulletin : {}", recupererBulletinDTO);
        if (recupererBulletinDTO.getId() != null) {
            throw new BadRequestAlertException("A new recupererBulletin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecupererBulletinDTO result = recupererBulletinService.save(recupererBulletinDTO);
        return ResponseEntity
            .created(new URI("/api/recuperer-bulletins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recuperer-bulletins/:id} : Updates an existing recupererBulletin.
     *
     * @param id the id of the recupererBulletinDTO to save.
     * @param recupererBulletinDTO the recupererBulletinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recupererBulletinDTO,
     * or with status {@code 400 (Bad Request)} if the recupererBulletinDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recupererBulletinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recuperer-bulletins/{id}")
    public ResponseEntity<RecupererBulletinDTO> updateRecupererBulletin(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecupererBulletinDTO recupererBulletinDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RecupererBulletin : {}, {}", id, recupererBulletinDTO);
        if (recupererBulletinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recupererBulletinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recupererBulletinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecupererBulletinDTO result = recupererBulletinService.update(recupererBulletinDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recupererBulletinDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recuperer-bulletins/:id} : Partial updates given fields of an existing recupererBulletin, field will ignore if it is null
     *
     * @param id the id of the recupererBulletinDTO to save.
     * @param recupererBulletinDTO the recupererBulletinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recupererBulletinDTO,
     * or with status {@code 400 (Bad Request)} if the recupererBulletinDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recupererBulletinDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recupererBulletinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recuperer-bulletins/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecupererBulletinDTO> partialUpdateRecupererBulletin(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecupererBulletinDTO recupererBulletinDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RecupererBulletin partially : {}, {}", id, recupererBulletinDTO);
        if (recupererBulletinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recupererBulletinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recupererBulletinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecupererBulletinDTO> result = recupererBulletinService.partialUpdate(recupererBulletinDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recupererBulletinDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recuperer-bulletins} : get all the recupererBulletins.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recupererBulletins in body.
     */
    @GetMapping("/recuperer-bulletins")
    public ResponseEntity<List<RecupererBulletinDTO>> getAllRecupererBulletins(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of RecupererBulletins");
        Page<RecupererBulletinDTO> page;
        if (eagerload) {
            page = recupererBulletinService.findAllWithEagerRelationships(pageable);
        } else {
            page = recupererBulletinService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recuperer-bulletins/:id} : get the "id" recupererBulletin.
     *
     * @param id the id of the recupererBulletinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recupererBulletinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recuperer-bulletins/{id}")
    public ResponseEntity<RecupererBulletinDTO> getRecupererBulletin(@PathVariable Long id) {
        log.debug("REST request to get RecupererBulletin : {}", id);
        Optional<RecupererBulletinDTO> recupererBulletinDTO = recupererBulletinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recupererBulletinDTO);
    }

    /**
     * {@code DELETE  /recuperer-bulletins/:id} : delete the "id" recupererBulletin.
     *
     * @param id the id of the recupererBulletinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recuperer-bulletins/{id}")
    public ResponseEntity<Void> deleteRecupererBulletin(@PathVariable Long id) {
        log.debug("REST request to delete RecupererBulletin : {}", id);
        recupererBulletinService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
