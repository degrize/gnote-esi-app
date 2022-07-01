package com.esi.gnote.web.rest;

import com.esi.gnote.repository.HoraireRepository;
import com.esi.gnote.service.HoraireService;
import com.esi.gnote.service.dto.HoraireDTO;
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
 * REST controller for managing {@link com.esi.gnote.domain.Horaire}.
 */
@RestController
@RequestMapping("/api")
public class HoraireResource {

    private final Logger log = LoggerFactory.getLogger(HoraireResource.class);

    private static final String ENTITY_NAME = "horaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HoraireService horaireService;

    private final HoraireRepository horaireRepository;

    public HoraireResource(HoraireService horaireService, HoraireRepository horaireRepository) {
        this.horaireService = horaireService;
        this.horaireRepository = horaireRepository;
    }

    /**
     * {@code POST  /horaires} : Create a new horaire.
     *
     * @param horaireDTO the horaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new horaireDTO, or with status {@code 400 (Bad Request)} if the horaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/horaires")
    public ResponseEntity<HoraireDTO> createHoraire(@Valid @RequestBody HoraireDTO horaireDTO) throws URISyntaxException {
        log.debug("REST request to save Horaire : {}", horaireDTO);
        if (horaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new horaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HoraireDTO result = horaireService.save(horaireDTO);
        return ResponseEntity
            .created(new URI("/api/horaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /horaires/:id} : Updates an existing horaire.
     *
     * @param id the id of the horaireDTO to save.
     * @param horaireDTO the horaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horaireDTO,
     * or with status {@code 400 (Bad Request)} if the horaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the horaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/horaires/{id}")
    public ResponseEntity<HoraireDTO> updateHoraire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HoraireDTO horaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Horaire : {}, {}", id, horaireDTO);
        if (horaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, horaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!horaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HoraireDTO result = horaireService.update(horaireDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /horaires/:id} : Partial updates given fields of an existing horaire, field will ignore if it is null
     *
     * @param id the id of the horaireDTO to save.
     * @param horaireDTO the horaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horaireDTO,
     * or with status {@code 400 (Bad Request)} if the horaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the horaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the horaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/horaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HoraireDTO> partialUpdateHoraire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HoraireDTO horaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Horaire partially : {}, {}", id, horaireDTO);
        if (horaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, horaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!horaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HoraireDTO> result = horaireService.partialUpdate(horaireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horaireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /horaires} : get all the horaires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of horaires in body.
     */
    @GetMapping("/horaires")
    public ResponseEntity<List<HoraireDTO>> getAllHoraires(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Horaires");
        Page<HoraireDTO> page = horaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /horaires/:id} : get the "id" horaire.
     *
     * @param id the id of the horaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the horaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/horaires/{id}")
    public ResponseEntity<HoraireDTO> getHoraire(@PathVariable Long id) {
        log.debug("REST request to get Horaire : {}", id);
        Optional<HoraireDTO> horaireDTO = horaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(horaireDTO);
    }

    /**
     * {@code DELETE  /horaires/:id} : delete the "id" horaire.
     *
     * @param id the id of the horaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/horaires/{id}")
    public ResponseEntity<Void> deleteHoraire(@PathVariable Long id) {
        log.debug("REST request to delete Horaire : {}", id);
        horaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
