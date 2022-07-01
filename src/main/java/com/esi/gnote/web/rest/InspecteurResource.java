package com.esi.gnote.web.rest;

import com.esi.gnote.repository.InspecteurRepository;
import com.esi.gnote.service.InspecteurService;
import com.esi.gnote.service.dto.InspecteurDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.Inspecteur}.
 */
@RestController
@RequestMapping("/api")
public class InspecteurResource {

    private final Logger log = LoggerFactory.getLogger(InspecteurResource.class);

    private static final String ENTITY_NAME = "inspecteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspecteurService inspecteurService;

    private final InspecteurRepository inspecteurRepository;

    public InspecteurResource(InspecteurService inspecteurService, InspecteurRepository inspecteurRepository) {
        this.inspecteurService = inspecteurService;
        this.inspecteurRepository = inspecteurRepository;
    }

    /**
     * {@code POST  /inspecteurs} : Create a new inspecteur.
     *
     * @param inspecteurDTO the inspecteurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspecteurDTO, or with status {@code 400 (Bad Request)} if the inspecteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspecteurs")
    public ResponseEntity<InspecteurDTO> createInspecteur(@Valid @RequestBody InspecteurDTO inspecteurDTO) throws URISyntaxException {
        log.debug("REST request to save Inspecteur : {}", inspecteurDTO);
        if (inspecteurDTO.getId() != null) {
            throw new BadRequestAlertException("A new inspecteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InspecteurDTO result = inspecteurService.save(inspecteurDTO);
        return ResponseEntity
            .created(new URI("/api/inspecteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inspecteurs/:id} : Updates an existing inspecteur.
     *
     * @param id the id of the inspecteurDTO to save.
     * @param inspecteurDTO the inspecteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspecteurDTO,
     * or with status {@code 400 (Bad Request)} if the inspecteurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspecteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspecteurs/{id}")
    public ResponseEntity<InspecteurDTO> updateInspecteur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InspecteurDTO inspecteurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Inspecteur : {}, {}", id, inspecteurDTO);
        if (inspecteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspecteurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspecteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InspecteurDTO result = inspecteurService.update(inspecteurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspecteurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inspecteurs/:id} : Partial updates given fields of an existing inspecteur, field will ignore if it is null
     *
     * @param id the id of the inspecteurDTO to save.
     * @param inspecteurDTO the inspecteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspecteurDTO,
     * or with status {@code 400 (Bad Request)} if the inspecteurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inspecteurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspecteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspecteurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InspecteurDTO> partialUpdateInspecteur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InspecteurDTO inspecteurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inspecteur partially : {}, {}", id, inspecteurDTO);
        if (inspecteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspecteurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspecteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InspecteurDTO> result = inspecteurService.partialUpdate(inspecteurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspecteurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inspecteurs} : get all the inspecteurs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspecteurs in body.
     */
    @GetMapping("/inspecteurs")
    public ResponseEntity<List<InspecteurDTO>> getAllInspecteurs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Inspecteurs");
        Page<InspecteurDTO> page;
        if (eagerload) {
            page = inspecteurService.findAllWithEagerRelationships(pageable);
        } else {
            page = inspecteurService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inspecteurs/:id} : get the "id" inspecteur.
     *
     * @param id the id of the inspecteurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspecteurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspecteurs/{id}")
    public ResponseEntity<InspecteurDTO> getInspecteur(@PathVariable Long id) {
        log.debug("REST request to get Inspecteur : {}", id);
        Optional<InspecteurDTO> inspecteurDTO = inspecteurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspecteurDTO);
    }

    /**
     * {@code DELETE  /inspecteurs/:id} : delete the "id" inspecteur.
     *
     * @param id the id of the inspecteurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspecteurs/{id}")
    public ResponseEntity<Void> deleteInspecteur(@PathVariable Long id) {
        log.debug("REST request to delete Inspecteur : {}", id);
        inspecteurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
