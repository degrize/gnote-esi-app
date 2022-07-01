package com.esi.gnote.web.rest;

import com.esi.gnote.repository.EncadreurRepository;
import com.esi.gnote.service.EncadreurService;
import com.esi.gnote.service.dto.EncadreurDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.Encadreur}.
 */
@RestController
@RequestMapping("/api")
public class EncadreurResource {

    private final Logger log = LoggerFactory.getLogger(EncadreurResource.class);

    private static final String ENTITY_NAME = "encadreur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EncadreurService encadreurService;

    private final EncadreurRepository encadreurRepository;

    public EncadreurResource(EncadreurService encadreurService, EncadreurRepository encadreurRepository) {
        this.encadreurService = encadreurService;
        this.encadreurRepository = encadreurRepository;
    }

    /**
     * {@code POST  /encadreurs} : Create a new encadreur.
     *
     * @param encadreurDTO the encadreurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new encadreurDTO, or with status {@code 400 (Bad Request)} if the encadreur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/encadreurs")
    public ResponseEntity<EncadreurDTO> createEncadreur(@Valid @RequestBody EncadreurDTO encadreurDTO) throws URISyntaxException {
        log.debug("REST request to save Encadreur : {}", encadreurDTO);
        if (encadreurDTO.getId() != null) {
            throw new BadRequestAlertException("A new encadreur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EncadreurDTO result = encadreurService.save(encadreurDTO);
        return ResponseEntity
            .created(new URI("/api/encadreurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /encadreurs/:id} : Updates an existing encadreur.
     *
     * @param id the id of the encadreurDTO to save.
     * @param encadreurDTO the encadreurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encadreurDTO,
     * or with status {@code 400 (Bad Request)} if the encadreurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the encadreurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/encadreurs/{id}")
    public ResponseEntity<EncadreurDTO> updateEncadreur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EncadreurDTO encadreurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Encadreur : {}, {}", id, encadreurDTO);
        if (encadreurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encadreurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encadreurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EncadreurDTO result = encadreurService.update(encadreurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encadreurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /encadreurs/:id} : Partial updates given fields of an existing encadreur, field will ignore if it is null
     *
     * @param id the id of the encadreurDTO to save.
     * @param encadreurDTO the encadreurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encadreurDTO,
     * or with status {@code 400 (Bad Request)} if the encadreurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the encadreurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the encadreurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/encadreurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EncadreurDTO> partialUpdateEncadreur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EncadreurDTO encadreurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Encadreur partially : {}, {}", id, encadreurDTO);
        if (encadreurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encadreurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encadreurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EncadreurDTO> result = encadreurService.partialUpdate(encadreurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encadreurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /encadreurs} : get all the encadreurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of encadreurs in body.
     */
    @GetMapping("/encadreurs")
    public ResponseEntity<List<EncadreurDTO>> getAllEncadreurs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Encadreurs");
        Page<EncadreurDTO> page = encadreurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /encadreurs/:id} : get the "id" encadreur.
     *
     * @param id the id of the encadreurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the encadreurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/encadreurs/{id}")
    public ResponseEntity<EncadreurDTO> getEncadreur(@PathVariable Long id) {
        log.debug("REST request to get Encadreur : {}", id);
        Optional<EncadreurDTO> encadreurDTO = encadreurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(encadreurDTO);
    }

    /**
     * {@code DELETE  /encadreurs/:id} : delete the "id" encadreur.
     *
     * @param id the id of the encadreurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/encadreurs/{id}")
    public ResponseEntity<Void> deleteEncadreur(@PathVariable Long id) {
        log.debug("REST request to delete Encadreur : {}", id);
        encadreurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
