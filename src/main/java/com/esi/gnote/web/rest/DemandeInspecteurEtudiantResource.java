package com.esi.gnote.web.rest;

import com.esi.gnote.repository.DemandeInspecteurEtudiantRepository;
import com.esi.gnote.service.DemandeInspecteurEtudiantService;
import com.esi.gnote.service.dto.DemandeInspecteurEtudiantDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.DemandeInspecteurEtudiant}.
 */
@RestController
@RequestMapping("/api")
public class DemandeInspecteurEtudiantResource {

    private final Logger log = LoggerFactory.getLogger(DemandeInspecteurEtudiantResource.class);

    private static final String ENTITY_NAME = "demandeInspecteurEtudiant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeInspecteurEtudiantService demandeInspecteurEtudiantService;

    private final DemandeInspecteurEtudiantRepository demandeInspecteurEtudiantRepository;

    public DemandeInspecteurEtudiantResource(
        DemandeInspecteurEtudiantService demandeInspecteurEtudiantService,
        DemandeInspecteurEtudiantRepository demandeInspecteurEtudiantRepository
    ) {
        this.demandeInspecteurEtudiantService = demandeInspecteurEtudiantService;
        this.demandeInspecteurEtudiantRepository = demandeInspecteurEtudiantRepository;
    }

    /**
     * {@code POST  /demande-inspecteur-etudiants} : Create a new demandeInspecteurEtudiant.
     *
     * @param demandeInspecteurEtudiantDTO the demandeInspecteurEtudiantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeInspecteurEtudiantDTO, or with status {@code 400 (Bad Request)} if the demandeInspecteurEtudiant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-inspecteur-etudiants")
    public ResponseEntity<DemandeInspecteurEtudiantDTO> createDemandeInspecteurEtudiant(
        @Valid @RequestBody DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DemandeInspecteurEtudiant : {}", demandeInspecteurEtudiantDTO);
        if (demandeInspecteurEtudiantDTO.getId() != null) {
            throw new BadRequestAlertException("A new demandeInspecteurEtudiant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeInspecteurEtudiantDTO result = demandeInspecteurEtudiantService.save(demandeInspecteurEtudiantDTO);
        return ResponseEntity
            .created(new URI("/api/demande-inspecteur-etudiants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-inspecteur-etudiants/:id} : Updates an existing demandeInspecteurEtudiant.
     *
     * @param id the id of the demandeInspecteurEtudiantDTO to save.
     * @param demandeInspecteurEtudiantDTO the demandeInspecteurEtudiantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeInspecteurEtudiantDTO,
     * or with status {@code 400 (Bad Request)} if the demandeInspecteurEtudiantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeInspecteurEtudiantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-inspecteur-etudiants/{id}")
    public ResponseEntity<DemandeInspecteurEtudiantDTO> updateDemandeInspecteurEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeInspecteurEtudiant : {}, {}", id, demandeInspecteurEtudiantDTO);
        if (demandeInspecteurEtudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeInspecteurEtudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeInspecteurEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeInspecteurEtudiantDTO result = demandeInspecteurEtudiantService.update(demandeInspecteurEtudiantDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeInspecteurEtudiantDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /demande-inspecteur-etudiants/:id} : Partial updates given fields of an existing demandeInspecteurEtudiant, field will ignore if it is null
     *
     * @param id the id of the demandeInspecteurEtudiantDTO to save.
     * @param demandeInspecteurEtudiantDTO the demandeInspecteurEtudiantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeInspecteurEtudiantDTO,
     * or with status {@code 400 (Bad Request)} if the demandeInspecteurEtudiantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the demandeInspecteurEtudiantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeInspecteurEtudiantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-inspecteur-etudiants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeInspecteurEtudiantDTO> partialUpdateDemandeInspecteurEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemandeInspecteurEtudiantDTO demandeInspecteurEtudiantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeInspecteurEtudiant partially : {}, {}", id, demandeInspecteurEtudiantDTO);
        if (demandeInspecteurEtudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeInspecteurEtudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeInspecteurEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeInspecteurEtudiantDTO> result = demandeInspecteurEtudiantService.partialUpdate(demandeInspecteurEtudiantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeInspecteurEtudiantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-inspecteur-etudiants} : get all the demandeInspecteurEtudiants.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeInspecteurEtudiants in body.
     */
    @GetMapping("/demande-inspecteur-etudiants")
    public ResponseEntity<List<DemandeInspecteurEtudiantDTO>> getAllDemandeInspecteurEtudiants(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of DemandeInspecteurEtudiants");
        Page<DemandeInspecteurEtudiantDTO> page;
        if (eagerload) {
            page = demandeInspecteurEtudiantService.findAllWithEagerRelationships(pageable);
        } else {
            page = demandeInspecteurEtudiantService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /demande-inspecteur-etudiants/:id} : get the "id" demandeInspecteurEtudiant.
     *
     * @param id the id of the demandeInspecteurEtudiantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeInspecteurEtudiantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-inspecteur-etudiants/{id}")
    public ResponseEntity<DemandeInspecteurEtudiantDTO> getDemandeInspecteurEtudiant(@PathVariable Long id) {
        log.debug("REST request to get DemandeInspecteurEtudiant : {}", id);
        Optional<DemandeInspecteurEtudiantDTO> demandeInspecteurEtudiantDTO = demandeInspecteurEtudiantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandeInspecteurEtudiantDTO);
    }

    /**
     * {@code DELETE  /demande-inspecteur-etudiants/:id} : delete the "id" demandeInspecteurEtudiant.
     *
     * @param id the id of the demandeInspecteurEtudiantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-inspecteur-etudiants/{id}")
    public ResponseEntity<Void> deleteDemandeInspecteurEtudiant(@PathVariable Long id) {
        log.debug("REST request to delete DemandeInspecteurEtudiant : {}", id);
        demandeInspecteurEtudiantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
