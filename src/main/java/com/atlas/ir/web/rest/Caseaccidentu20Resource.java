package com.atlas.ir.web.rest;

import com.atlas.ir.repository.Caseaccidentu20Repository;
import com.atlas.ir.service.Caseaccidentu20QueryService;
import com.atlas.ir.service.Caseaccidentu20Service;
import com.atlas.ir.service.criteria.Caseaccidentu20Criteria;
import com.atlas.ir.service.dto.Caseaccidentu20DTO;
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
 * REST controller for managing {@link com.atlas.ir.domain.Caseaccidentu20}.
 */
@RestController
@RequestMapping("/api")
public class Caseaccidentu20Resource {

    private final Logger log = LoggerFactory.getLogger(Caseaccidentu20Resource.class);

    private static final String ENTITY_NAME = "callCenterBimaniCaseaccidentu20";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Caseaccidentu20Service caseaccidentu20Service;

    private final Caseaccidentu20Repository caseaccidentu20Repository;

    private final Caseaccidentu20QueryService caseaccidentu20QueryService;

    public Caseaccidentu20Resource(
        Caseaccidentu20Service caseaccidentu20Service,
        Caseaccidentu20Repository caseaccidentu20Repository,
        Caseaccidentu20QueryService caseaccidentu20QueryService
    ) {
        this.caseaccidentu20Service = caseaccidentu20Service;
        this.caseaccidentu20Repository = caseaccidentu20Repository;
        this.caseaccidentu20QueryService = caseaccidentu20QueryService;
    }

    /**
     * {@code POST  /caseaccidentu-20-s} : Create a new caseaccidentu20.
     *
     * @param caseaccidentu20DTO the caseaccidentu20DTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caseaccidentu20DTO, or with status {@code 400 (Bad Request)} if the caseaccidentu20 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caseaccidentu-20-s")
    public ResponseEntity<Caseaccidentu20DTO> createCaseaccidentu20(@RequestBody Caseaccidentu20DTO caseaccidentu20DTO)
        throws URISyntaxException {
        log.debug("REST request to save Caseaccidentu20 : {}", caseaccidentu20DTO);
        if (caseaccidentu20DTO.getId() != null) {
            throw new BadRequestAlertException("A new caseaccidentu20 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Caseaccidentu20DTO result = caseaccidentu20Service.save(caseaccidentu20DTO);
        return ResponseEntity
            .created(new URI("/api/caseaccidentu-20-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caseaccidentu-20-s/:id} : Updates an existing caseaccidentu20.
     *
     * @param id the id of the caseaccidentu20DTO to save.
     * @param caseaccidentu20DTO the caseaccidentu20DTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseaccidentu20DTO,
     * or with status {@code 400 (Bad Request)} if the caseaccidentu20DTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caseaccidentu20DTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caseaccidentu-20-s/{id}")
    public ResponseEntity<Caseaccidentu20DTO> updateCaseaccidentu20(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Caseaccidentu20DTO caseaccidentu20DTO
    ) throws URISyntaxException {
        log.debug("REST request to update Caseaccidentu20 : {}, {}", id, caseaccidentu20DTO);
        if (caseaccidentu20DTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseaccidentu20DTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseaccidentu20Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Caseaccidentu20DTO result = caseaccidentu20Service.update(caseaccidentu20DTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, caseaccidentu20DTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /caseaccidentu-20-s/:id} : Partial updates given fields of an existing caseaccidentu20, field will ignore if it is null
     *
     * @param id the id of the caseaccidentu20DTO to save.
     * @param caseaccidentu20DTO the caseaccidentu20DTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseaccidentu20DTO,
     * or with status {@code 400 (Bad Request)} if the caseaccidentu20DTO is not valid,
     * or with status {@code 404 (Not Found)} if the caseaccidentu20DTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the caseaccidentu20DTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/caseaccidentu-20-s/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Caseaccidentu20DTO> partialUpdateCaseaccidentu20(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Caseaccidentu20DTO caseaccidentu20DTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Caseaccidentu20 partially : {}, {}", id, caseaccidentu20DTO);
        if (caseaccidentu20DTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseaccidentu20DTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseaccidentu20Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Caseaccidentu20DTO> result = caseaccidentu20Service.partialUpdate(caseaccidentu20DTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, caseaccidentu20DTO.getId().toString())
        );
    }

    /**
     * {@code GET  /caseaccidentu-20-s} : get all the caseaccidentu20s.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caseaccidentu20s in body.
     */
    @GetMapping("/caseaccidentu-20-s")
    public ResponseEntity<List<Caseaccidentu20DTO>> getAllCaseaccidentu20s(
        Caseaccidentu20Criteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Caseaccidentu20s by criteria: {}", criteria);
        Page<Caseaccidentu20DTO> page = caseaccidentu20QueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /caseaccidentu-20-s/count} : count all the caseaccidentu20s.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/caseaccidentu-20-s/count")
    public ResponseEntity<Long> countCaseaccidentu20s(Caseaccidentu20Criteria criteria) {
        log.debug("REST request to count Caseaccidentu20s by criteria: {}", criteria);
        return ResponseEntity.ok().body(caseaccidentu20QueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /caseaccidentu-20-s/:id} : get the "id" caseaccidentu20.
     *
     * @param id the id of the caseaccidentu20DTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caseaccidentu20DTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caseaccidentu-20-s/{id}")
    public ResponseEntity<Caseaccidentu20DTO> getCaseaccidentu20(@PathVariable Long id) {
        log.debug("REST request to get Caseaccidentu20 : {}", id);
        Optional<Caseaccidentu20DTO> caseaccidentu20DTO = caseaccidentu20Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(caseaccidentu20DTO);
    }

    /**
     * {@code DELETE  /caseaccidentu-20-s/:id} : delete the "id" caseaccidentu20.
     *
     * @param id the id of the caseaccidentu20DTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caseaccidentu-20-s/{id}")
    public ResponseEntity<Void> deleteCaseaccidentu20(@PathVariable Long id) {
        log.debug("REST request to delete Caseaccidentu20 : {}", id);
        caseaccidentu20Service.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
