package com.atlas.ir.web.rest;

import com.atlas.ir.repository.RepairshopRepository;
import com.atlas.ir.service.RepairshopQueryService;
import com.atlas.ir.service.RepairshopService;
import com.atlas.ir.service.criteria.RepairshopCriteria;
import com.atlas.ir.service.dto.RepairshopDTO;
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
 * REST controller for managing {@link com.atlas.ir.domain.Repairshop}.
 */
@RestController
@RequestMapping("/api")
public class RepairshopResource {

    private final Logger log = LoggerFactory.getLogger(RepairshopResource.class);

    private static final String ENTITY_NAME = "callCenterBimaniRepairshop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepairshopService repairshopService;

    private final RepairshopRepository repairshopRepository;

    private final RepairshopQueryService repairshopQueryService;

    public RepairshopResource(
        RepairshopService repairshopService,
        RepairshopRepository repairshopRepository,
        RepairshopQueryService repairshopQueryService
    ) {
        this.repairshopService = repairshopService;
        this.repairshopRepository = repairshopRepository;
        this.repairshopQueryService = repairshopQueryService;
    }

    /**
     * {@code POST  /repairshops} : Create a new repairshop.
     *
     * @param repairshopDTO the repairshopDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repairshopDTO, or with status {@code 400 (Bad Request)} if the repairshop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/repairshops")
    public ResponseEntity<RepairshopDTO> createRepairshop(@RequestBody RepairshopDTO repairshopDTO) throws URISyntaxException {
        log.debug("REST request to save Repairshop : {}", repairshopDTO);
        if (repairshopDTO.getId() != null) {
            throw new BadRequestAlertException("A new repairshop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepairshopDTO result = repairshopService.save(repairshopDTO);
        return ResponseEntity
            .created(new URI("/api/repairshops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /repairshops/:id} : Updates an existing repairshop.
     *
     * @param id the id of the repairshopDTO to save.
     * @param repairshopDTO the repairshopDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repairshopDTO,
     * or with status {@code 400 (Bad Request)} if the repairshopDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repairshopDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/repairshops/{id}")
    public ResponseEntity<RepairshopDTO> updateRepairshop(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RepairshopDTO repairshopDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Repairshop : {}, {}", id, repairshopDTO);
        if (repairshopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repairshopDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repairshopRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RepairshopDTO result = repairshopService.update(repairshopDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, repairshopDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /repairshops/:id} : Partial updates given fields of an existing repairshop, field will ignore if it is null
     *
     * @param id the id of the repairshopDTO to save.
     * @param repairshopDTO the repairshopDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repairshopDTO,
     * or with status {@code 400 (Bad Request)} if the repairshopDTO is not valid,
     * or with status {@code 404 (Not Found)} if the repairshopDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the repairshopDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/repairshops/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RepairshopDTO> partialUpdateRepairshop(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RepairshopDTO repairshopDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Repairshop partially : {}, {}", id, repairshopDTO);
        if (repairshopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repairshopDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repairshopRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RepairshopDTO> result = repairshopService.partialUpdate(repairshopDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, repairshopDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /repairshops} : get all the repairshops.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repairshops in body.
     */
    @GetMapping("/repairshops")
    public ResponseEntity<List<RepairshopDTO>> getAllRepairshops(
        RepairshopCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Repairshops by criteria: {}", criteria);
        Page<RepairshopDTO> page = repairshopQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /repairshops/count} : count all the repairshops.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/repairshops/count")
    public ResponseEntity<Long> countRepairshops(RepairshopCriteria criteria) {
        log.debug("REST request to count Repairshops by criteria: {}", criteria);
        return ResponseEntity.ok().body(repairshopQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /repairshops/:id} : get the "id" repairshop.
     *
     * @param id the id of the repairshopDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repairshopDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/repairshops/{id}")
    public ResponseEntity<RepairshopDTO> getRepairshop(@PathVariable Long id) {
        log.debug("REST request to get Repairshop : {}", id);
        Optional<RepairshopDTO> repairshopDTO = repairshopService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repairshopDTO);
    }

    /**
     * {@code DELETE  /repairshops/:id} : delete the "id" repairshop.
     *
     * @param id the id of the repairshopDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/repairshops/{id}")
    public ResponseEntity<Void> deleteRepairshop(@PathVariable Long id) {
        log.debug("REST request to delete Repairshop : {}", id);
        repairshopService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
