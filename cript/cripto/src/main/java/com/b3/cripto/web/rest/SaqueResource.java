package com.b3.cripto.web.rest;

import com.b3.cripto.repository.SaqueRepository;
import com.b3.cripto.service.SaqueService;
import com.b3.cripto.service.dto.SaqueDTO;
import com.b3.cripto.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.b3.cripto.domain.Saque}.
 */
@RestController
@RequestMapping("/api")
public class SaqueResource {

    private final Logger log = LoggerFactory.getLogger(SaqueResource.class);

    private static final String ENTITY_NAME = "criptoSaque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaqueService saqueService;

    private final SaqueRepository saqueRepository;

    public SaqueResource(SaqueService saqueService, SaqueRepository saqueRepository) {
        this.saqueService = saqueService;
        this.saqueRepository = saqueRepository;
    }

    /**
     * {@code POST  /saques} : Create a new saque.
     *
     * @param saqueDTO the saqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saqueDTO, or with status {@code 400 (Bad Request)} if the saque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/saques")
    public ResponseEntity<SaqueDTO> createSaque(@RequestBody SaqueDTO saqueDTO) throws URISyntaxException {
        log.debug("REST request to save Saque : {}", saqueDTO);
        if (saqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new saque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaqueDTO result = saqueService.save(saqueDTO);
        return ResponseEntity
            .created(new URI("/api/saques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /saques/:id} : Updates an existing saque.
     *
     * @param id the id of the saqueDTO to save.
     * @param saqueDTO the saqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saqueDTO,
     * or with status {@code 400 (Bad Request)} if the saqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/saques/{id}")
    public ResponseEntity<SaqueDTO> updateSaque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SaqueDTO saqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Saque : {}, {}", id, saqueDTO);
        if (saqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SaqueDTO result = saqueService.update(saqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /saques/:id} : Partial updates given fields of an existing saque, field will ignore if it is null
     *
     * @param id the id of the saqueDTO to save.
     * @param saqueDTO the saqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saqueDTO,
     * or with status {@code 400 (Bad Request)} if the saqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the saqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the saqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/saques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaqueDTO> partialUpdateSaque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SaqueDTO saqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Saque partially : {}, {}", id, saqueDTO);
        if (saqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaqueDTO> result = saqueService.partialUpdate(saqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /saques} : get all the saques.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saques in body.
     */
    @GetMapping("/saques")
    public ResponseEntity<List<SaqueDTO>> getAllSaques(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Saques");
        Page<SaqueDTO> page = saqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /saques/:id} : get the "id" saque.
     *
     * @param id the id of the saqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/saques/{id}")
    public ResponseEntity<SaqueDTO> getSaque(@PathVariable Long id) {
        log.debug("REST request to get Saque : {}", id);
        Optional<SaqueDTO> saqueDTO = saqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saqueDTO);
    }

    /**
     * {@code DELETE  /saques/:id} : delete the "id" saque.
     *
     * @param id the id of the saqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/saques/{id}")
    public ResponseEntity<Void> deleteSaque(@PathVariable Long id) {
        log.debug("REST request to delete Saque : {}", id);
        saqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
