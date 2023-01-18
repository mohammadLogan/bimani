package com.atlas.ir.web.rest;

import com.atlas.ir.repository.CasetypeRepository;
import com.atlas.ir.service.CasetypeQueryService;
import com.atlas.ir.service.CasetypeService;
import com.atlas.ir.service.criteria.CasetypeCriteria;
import com.atlas.ir.service.dto.CasetypeDTO;
import com.atlas.ir.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.atlas.ir.domain.Casetype}.
 */
@RestController
@RequestMapping("/api")
public class CasetypeResource {

    private final Logger log = LoggerFactory.getLogger(CasetypeResource.class);

    private static final String ENTITY_NAME = "callCenterBimaniCasetype";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CasetypeService casetypeService;

    private final CasetypeRepository casetypeRepository;

    private final CasetypeQueryService casetypeQueryService;

    public CasetypeResource(
        CasetypeService casetypeService,
        CasetypeRepository casetypeRepository,
        CasetypeQueryService casetypeQueryService
    ) {
        this.casetypeService = casetypeService;
        this.casetypeRepository = casetypeRepository;
        this.casetypeQueryService = casetypeQueryService;
    }

    /**
     * {@code POST  /casetypes} : Create a new casetype.
     *
     * @param casetypeDTO the casetypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new casetypeDTO, or with status {@code 400 (Bad Request)} if the casetype has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/casetypes")
    public ResponseEntity<CasetypeDTO> createCasetype(@RequestBody CasetypeDTO casetypeDTO) throws URISyntaxException {
        log.debug("REST request to save Casetype : {}", casetypeDTO);
        if (casetypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new casetype cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CasetypeDTO result = casetypeService.save(casetypeDTO);
        return ResponseEntity
            .created(new URI("/api/casetypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /casetypes/:id} : Updates an existing casetype.
     *
     * @param id the id of the casetypeDTO to save.
     * @param casetypeDTO the casetypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casetypeDTO,
     * or with status {@code 400 (Bad Request)} if the casetypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the casetypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/casetypes/{id}")
    public ResponseEntity<CasetypeDTO> updateCasetype(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CasetypeDTO casetypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Casetype : {}, {}", id, casetypeDTO);
        if (casetypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casetypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casetypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CasetypeDTO result = casetypeService.update(casetypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, casetypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /casetypes/:id} : Partial updates given fields of an existing casetype, field will ignore if it is null
     *
     * @param id the id of the casetypeDTO to save.
     * @param casetypeDTO the casetypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casetypeDTO,
     * or with status {@code 400 (Bad Request)} if the casetypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the casetypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the casetypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/casetypes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CasetypeDTO> partialUpdateCasetype(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CasetypeDTO casetypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Casetype partially : {}, {}", id, casetypeDTO);
        if (casetypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casetypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casetypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CasetypeDTO> result = casetypeService.partialUpdate(casetypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, casetypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /casetypes} : get all the casetypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of casetypes in body.
     */
    @GetMapping("/casetypes")
    public ResponseEntity<List<CasetypeDTO>> getAllCasetypes(
        CasetypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Casetypes by criteria: {}", criteria);
        Page<CasetypeDTO> page = casetypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /casetypes/count} : count all the casetypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/casetypes/count")
    public ResponseEntity<Long> countCasetypes(CasetypeCriteria criteria) {
        log.debug("REST request to count Casetypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(casetypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /casetypes/:id} : get the "id" casetype.
     *
     * @param id the id of the casetypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casetypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/casetypes/{id}")
    public ResponseEntity<CasetypeDTO> getCasetype(@PathVariable Long id) {
        log.debug("REST request to get Casetype : {}", id);
        Optional<CasetypeDTO> casetypeDTO = casetypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(casetypeDTO);
    }

    /**
     * {@code DELETE  /casetypes/:id} : delete the "id" casetype.
     *
     * @param id the id of the casetypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/casetypes/{id}")
    public ResponseEntity<Void> deleteCasetype(@PathVariable Long id) {
        log.debug("REST request to delete Casetype : {}", id);
        casetypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
