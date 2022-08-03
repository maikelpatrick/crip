package com.b3.cripto.web.rest;

import com.b3.cripto.repository.DepositoRepository;
import com.b3.cripto.service.DepositoService;
import com.b3.cripto.service.dto.DepositoDTO;
import com.b3.cripto.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.b3.cripto.domain.Deposito}.
 */
@RestController
@RequestMapping("/api")
public class DepositoResource {

    private final Logger log = LoggerFactory.getLogger(DepositoResource.class);

    private static final String ENTITY_NAME = "criptoDeposito";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepositoService depositoService;

    private final DepositoRepository depositoRepository;

    public DepositoResource(DepositoService depositoService, DepositoRepository depositoRepository) {
        this.depositoService = depositoService;
        this.depositoRepository = depositoRepository;
    }

    /**
     * {@code POST  /depositos} : Create a new deposito.
     *
     * @param depositoDTO the depositoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depositoDTO, or with status {@code 400 (Bad Request)} if the deposito has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depositos")
    public ResponseEntity<DepositoDTO> createDeposito(@RequestBody DepositoDTO depositoDTO) throws URISyntaxException {
        log.debug("REST request to save Deposito : {}", depositoDTO);
        if (depositoDTO.getId() != null) {
            throw new BadRequestAlertException("A new deposito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepositoDTO result = depositoService.save(depositoDTO);
        return ResponseEntity
            .created(new URI("/api/depositos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depositos/:id} : Updates an existing deposito.
     *
     * @param id the id of the depositoDTO to save.
     * @param depositoDTO the depositoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositoDTO,
     * or with status {@code 400 (Bad Request)} if the depositoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depositoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depositos/{id}")
    public ResponseEntity<DepositoDTO> updateDeposito(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepositoDTO depositoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Deposito : {}, {}", id, depositoDTO);
        if (depositoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depositoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depositoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepositoDTO result = depositoService.update(depositoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depositoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depositos/:id} : Partial updates given fields of an existing deposito, field will ignore if it is null
     *
     * @param id the id of the depositoDTO to save.
     * @param depositoDTO the depositoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositoDTO,
     * or with status {@code 400 (Bad Request)} if the depositoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depositoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depositoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depositos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepositoDTO> partialUpdateDeposito(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepositoDTO depositoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deposito partially : {}, {}", id, depositoDTO);
        if (depositoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depositoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depositoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepositoDTO> result = depositoService.partialUpdate(depositoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depositoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depositos} : get all the depositos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depositos in body.
     */
    @GetMapping("/depositos")
    public List<DepositoDTO> getAllDepositos() {
        log.debug("REST request to get all Depositos");
        return depositoService.findAll();
    }

    /**
     * {@code GET  /depositos/:id} : get the "id" deposito.
     *
     * @param id the id of the depositoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depositoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depositos/{id}")
    public ResponseEntity<DepositoDTO> getDeposito(@PathVariable Long id) {
        log.debug("REST request to get Deposito : {}", id);
        Optional<DepositoDTO> depositoDTO = depositoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depositoDTO);
    }

    /**
     * {@code DELETE  /depositos/:id} : delete the "id" deposito.
     *
     * @param id the id of the depositoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depositos/{id}")
    public ResponseEntity<Void> deleteDeposito(@PathVariable Long id) {
        log.debug("REST request to delete Deposito : {}", id);
        depositoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
