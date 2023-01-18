package com.atlas.ir.web.rest;

import com.atlas.ir.repository.Cau20statusRepository;
import com.atlas.ir.service.Cau20statusQueryService;
import com.atlas.ir.service.Cau20statusService;
import com.atlas.ir.service.criteria.Cau20statusCriteria;
import com.atlas.ir.service.dto.Cau20statusDTO;
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
 * REST controller for managing {@link com.atlas.ir.domain.Cau20status}.
 */
@RestController
@RequestMapping("/api")
public class Cau20statusResource {

    private final Logger log = LoggerFactory.getLogger(Cau20statusResource.class);

    private static final String ENTITY_NAME = "callCenterBimaniCau20Status";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Cau20statusService cau20statusService;

    private final Cau20statusRepository cau20statusRepository;

    private final Cau20statusQueryService cau20statusQueryService;

    public Cau20statusResource(
        Cau20statusService cau20statusService,
        Cau20statusRepository cau20statusRepository,
        Cau20statusQueryService cau20statusQueryService
    ) {
        this.cau20statusService = cau20statusService;
        this.cau20statusRepository = cau20statusRepository;
        this.cau20statusQueryService = cau20statusQueryService;
    }

    /**
     * {@code POST  /cau-20-statuses} : Create a new cau20status.
     *
     * @param cau20statusDTO the cau20statusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cau20statusDTO, or with status {@code 400 (Bad Request)} if the cau20status has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cau-20-statuses")
    public ResponseEntity<Cau20statusDTO> createCau20status(@RequestBody Cau20statusDTO cau20statusDTO) throws URISyntaxException {
        log.debug("REST request to save Cau20status : {}", cau20statusDTO);
        if (cau20statusDTO.getId() != null) {
            throw new BadRequestAlertException("A new cau20status cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cau20statusDTO result = cau20statusService.save(cau20statusDTO);
        return ResponseEntity
            .created(new URI("/api/cau-20-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cau-20-statuses/:id} : Updates an existing cau20status.
     *
     * @param id the id of the cau20statusDTO to save.
     * @param cau20statusDTO the cau20statusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cau20statusDTO,
     * or with status {@code 400 (Bad Request)} if the cau20statusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cau20statusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cau-20-statuses/{id}")
    public ResponseEntity<Cau20statusDTO> updateCau20status(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cau20statusDTO cau20statusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cau20status : {}, {}", id, cau20statusDTO);
        if (cau20statusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cau20statusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cau20statusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cau20statusDTO result = cau20statusService.update(cau20statusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cau20statusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cau-20-statuses/:id} : Partial updates given fields of an existing cau20status, field will ignore if it is null
     *
     * @param id the id of the cau20statusDTO to save.
     * @param cau20statusDTO the cau20statusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cau20statusDTO,
     * or with status {@code 400 (Bad Request)} if the cau20statusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cau20statusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cau20statusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cau-20-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cau20statusDTO> partialUpdateCau20status(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cau20statusDTO cau20statusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cau20status partially : {}, {}", id, cau20statusDTO);
        if (cau20statusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cau20statusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cau20statusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cau20statusDTO> result = cau20statusService.partialUpdate(cau20statusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cau20statusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cau-20-statuses} : get all the cau20statuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cau20statuses in body.
     */
    @GetMapping("/cau-20-statuses")
    public ResponseEntity<List<Cau20statusDTO>> getAllCau20statuses(
        Cau20statusCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Cau20statuses by criteria: {}", criteria);
        Page<Cau20statusDTO> page = cau20statusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cau-20-statuses/count} : count all the cau20statuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cau-20-statuses/count")
    public ResponseEntity<Long> countCau20statuses(Cau20statusCriteria criteria) {
        log.debug("REST request to count Cau20statuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(cau20statusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cau-20-statuses/:id} : get the "id" cau20status.
     *
     * @param id the id of the cau20statusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cau20statusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cau-20-statuses/{id}")
    public ResponseEntity<Cau20statusDTO> getCau20status(@PathVariable Long id) {
        log.debug("REST request to get Cau20status : {}", id);
        Optional<Cau20statusDTO> cau20statusDTO = cau20statusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cau20statusDTO);
    }

    /**
     * {@code DELETE  /cau-20-statuses/:id} : delete the "id" cau20status.
     *
     * @param id the id of the cau20statusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cau-20-statuses/{id}")
    public ResponseEntity<Void> deleteCau20status(@PathVariable Long id) {
        log.debug("REST request to delete Cau20status : {}", id);
        cau20statusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
