package com.esi.gnote.web.rest;

import com.esi.gnote.repository.DemandeInspecteurDERepository;
import com.esi.gnote.service.DemandeInspecteurDEService;
import com.esi.gnote.service.dto.DemandeInspecteurDEDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.DemandeInspecteurDE}.
 */
@RestController
@RequestMapping("/api")
public class DemandeInspecteurDEResource {

    private final Logger log = LoggerFactory.getLogger(DemandeInspecteurDEResource.class);

    private static final String ENTITY_NAME = "demandeInspecteurDE";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeInspecteurDEService demandeInspecteurDEService;

    private final DemandeInspecteurDERepository demandeInspecteurDERepository;

    public DemandeInspecteurDEResource(
        DemandeInspecteurDEService demandeInspecteurDEService,
        DemandeInspecteurDERepository demandeInspecteurDERepository
    ) {
        this.demandeInspecteurDEService = demandeInspecteurDEService;
        this.demandeInspecteurDERepository = demandeInspecteurDERepository;
    }

    /**
     * {@code POST  /demande-inspecteur-des} : Create a new demandeInspecteurDE.
     *
     * @param demandeInspecteurDEDTO the demandeInspecteurDEDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeInspecteurDEDTO, or with status {@code 400 (Bad Request)} if the demandeInspecteurDE has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-inspecteur-des")
    public ResponseEntity<DemandeInspecteurDEDTO> createDemandeInspecteurDE(
        @Valid @RequestBody DemandeInspecteurDEDTO demandeInspecteurDEDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DemandeInspecteurDE : {}", demandeInspecteurDEDTO);
        if (demandeInspecteurDEDTO.getId() != null) {
            throw new BadRequestAlertException("A new demandeInspecteurDE cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeInspecteurDEDTO result = demandeInspecteurDEService.save(demandeInspecteurDEDTO);
        return ResponseEntity
            .created(new URI("/api/demande-inspecteur-des/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-inspecteur-des/:id} : Updates an existing demandeInspecteurDE.
     *
     * @param id the id of the demandeInspecteurDEDTO to save.
     * @param demandeInspecteurDEDTO the demandeInspecteurDEDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeInspecteurDEDTO,
     * or with status {@code 400 (Bad Request)} if the demandeInspecteurDEDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeInspecteurDEDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-inspecteur-des/{id}")
    public ResponseEntity<DemandeInspecteurDEDTO> updateDemandeInspecteurDE(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemandeInspecteurDEDTO demandeInspecteurDEDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeInspecteurDE : {}, {}", id, demandeInspecteurDEDTO);
        if (demandeInspecteurDEDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeInspecteurDEDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeInspecteurDERepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeInspecteurDEDTO result = demandeInspecteurDEService.update(demandeInspecteurDEDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeInspecteurDEDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-inspecteur-des/:id} : Partial updates given fields of an existing demandeInspecteurDE, field will ignore if it is null
     *
     * @param id the id of the demandeInspecteurDEDTO to save.
     * @param demandeInspecteurDEDTO the demandeInspecteurDEDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeInspecteurDEDTO,
     * or with status {@code 400 (Bad Request)} if the demandeInspecteurDEDTO is not valid,
     * or with status {@code 404 (Not Found)} if the demandeInspecteurDEDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeInspecteurDEDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-inspecteur-des/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeInspecteurDEDTO> partialUpdateDemandeInspecteurDE(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemandeInspecteurDEDTO demandeInspecteurDEDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeInspecteurDE partially : {}, {}", id, demandeInspecteurDEDTO);
        if (demandeInspecteurDEDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeInspecteurDEDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeInspecteurDERepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeInspecteurDEDTO> result = demandeInspecteurDEService.partialUpdate(demandeInspecteurDEDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeInspecteurDEDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-inspecteur-des} : get all the demandeInspecteurDES.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeInspecteurDES in body.
     */
    @GetMapping("/demande-inspecteur-des")
    public ResponseEntity<List<DemandeInspecteurDEDTO>> getAllDemandeInspecteurDES(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of DemandeInspecteurDES");
        Page<DemandeInspecteurDEDTO> page;
        if (eagerload) {
            page = demandeInspecteurDEService.findAllWithEagerRelationships(pageable);
        } else {
            page = demandeInspecteurDEService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /demande-inspecteur-des/:id} : get the "id" demandeInspecteurDE.
     *
     * @param id the id of the demandeInspecteurDEDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeInspecteurDEDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-inspecteur-des/{id}")
    public ResponseEntity<DemandeInspecteurDEDTO> getDemandeInspecteurDE(@PathVariable Long id) {
        log.debug("REST request to get DemandeInspecteurDE : {}", id);
        Optional<DemandeInspecteurDEDTO> demandeInspecteurDEDTO = demandeInspecteurDEService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandeInspecteurDEDTO);
    }

    /**
     * {@code DELETE  /demande-inspecteur-des/:id} : delete the "id" demandeInspecteurDE.
     *
     * @param id the id of the demandeInspecteurDEDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-inspecteur-des/{id}")
    public ResponseEntity<Void> deleteDemandeInspecteurDE(@PathVariable Long id) {
        log.debug("REST request to delete DemandeInspecteurDE : {}", id);
        demandeInspecteurDEService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
