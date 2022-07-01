package com.esi.gnote.web.rest;

import com.esi.gnote.repository.BulletinRepository;
import com.esi.gnote.service.BulletinService;
import com.esi.gnote.service.dto.BulletinDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.Bulletin}.
 */
@RestController
@RequestMapping("/api")
public class BulletinResource {

    private final Logger log = LoggerFactory.getLogger(BulletinResource.class);

    private static final String ENTITY_NAME = "bulletin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BulletinService bulletinService;

    private final BulletinRepository bulletinRepository;

    public BulletinResource(BulletinService bulletinService, BulletinRepository bulletinRepository) {
        this.bulletinService = bulletinService;
        this.bulletinRepository = bulletinRepository;
    }

    /**
     * {@code POST  /bulletins} : Create a new bulletin.
     *
     * @param bulletinDTO the bulletinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bulletinDTO, or with status {@code 400 (Bad Request)} if the bulletin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bulletins")
    public ResponseEntity<BulletinDTO> createBulletin(@Valid @RequestBody BulletinDTO bulletinDTO) throws URISyntaxException {
        log.debug("REST request to save Bulletin : {}", bulletinDTO);
        if (bulletinDTO.getId() != null) {
            throw new BadRequestAlertException("A new bulletin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BulletinDTO result = bulletinService.save(bulletinDTO);
        return ResponseEntity
            .created(new URI("/api/bulletins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bulletins/:id} : Updates an existing bulletin.
     *
     * @param id the id of the bulletinDTO to save.
     * @param bulletinDTO the bulletinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bulletinDTO,
     * or with status {@code 400 (Bad Request)} if the bulletinDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bulletinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bulletins/{id}")
    public ResponseEntity<BulletinDTO> updateBulletin(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BulletinDTO bulletinDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Bulletin : {}, {}", id, bulletinDTO);
        if (bulletinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bulletinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bulletinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BulletinDTO result = bulletinService.update(bulletinDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bulletinDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bulletins/:id} : Partial updates given fields of an existing bulletin, field will ignore if it is null
     *
     * @param id the id of the bulletinDTO to save.
     * @param bulletinDTO the bulletinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bulletinDTO,
     * or with status {@code 400 (Bad Request)} if the bulletinDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bulletinDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bulletinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bulletins/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BulletinDTO> partialUpdateBulletin(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BulletinDTO bulletinDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bulletin partially : {}, {}", id, bulletinDTO);
        if (bulletinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bulletinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bulletinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BulletinDTO> result = bulletinService.partialUpdate(bulletinDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bulletinDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bulletins} : get all the bulletins.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bulletins in body.
     */
    @GetMapping("/bulletins")
    public ResponseEntity<List<BulletinDTO>> getAllBulletins(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Bulletins");
        Page<BulletinDTO> page;
        if (eagerload) {
            page = bulletinService.findAllWithEagerRelationships(pageable);
        } else {
            page = bulletinService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bulletins/:id} : get the "id" bulletin.
     *
     * @param id the id of the bulletinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bulletinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bulletins/{id}")
    public ResponseEntity<BulletinDTO> getBulletin(@PathVariable Long id) {
        log.debug("REST request to get Bulletin : {}", id);
        Optional<BulletinDTO> bulletinDTO = bulletinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bulletinDTO);
    }

    /**
     * {@code DELETE  /bulletins/:id} : delete the "id" bulletin.
     *
     * @param id the id of the bulletinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bulletins/{id}")
    public ResponseEntity<Void> deleteBulletin(@PathVariable Long id) {
        log.debug("REST request to delete Bulletin : {}", id);
        bulletinService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
