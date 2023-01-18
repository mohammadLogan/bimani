package com.atlas.ir.web.rest;

import com.atlas.ir.repository.InsuranceRepository;
import com.atlas.ir.service.InsuranceQueryService;
import com.atlas.ir.service.InsuranceService;
import com.atlas.ir.service.criteria.InsuranceCriteria;
import com.atlas.ir.service.dto.InsuranceDTO;
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
 * REST controller for managing {@link com.atlas.ir.domain.Insurance}.
 */
@RestController
@RequestMapping("/api")
public class InsuranceResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceResource.class);

    private static final String ENTITY_NAME = "callCenterBimaniInsurance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceService insuranceService;

    private final InsuranceRepository insuranceRepository;

    private final InsuranceQueryService insuranceQueryService;

    public InsuranceResource(
        InsuranceService insuranceService,
        InsuranceRepository insuranceRepository,
        InsuranceQueryService insuranceQueryService
    ) {
        this.insuranceService = insuranceService;
        this.insuranceRepository = insuranceRepository;
        this.insuranceQueryService = insuranceQueryService;
    }

    /**
     * {@code POST  /insurances} : Create a new insurance.
     *
     * @param insuranceDTO the insuranceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceDTO, or with status {@code 400 (Bad Request)} if the insurance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/insurances")
    public ResponseEntity<InsuranceDTO> createInsurance(@RequestBody InsuranceDTO insuranceDTO) throws URISyntaxException {
        log.debug("REST request to save Insurance : {}", insuranceDTO);
        if (insuranceDTO.getId() != null) {
            throw new BadRequestAlertException("A new insurance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InsuranceDTO result = insuranceService.save(insuranceDTO);
        return ResponseEntity
            .created(new URI("/api/insurances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /insurances/:id} : Updates an existing insurance.
     *
     * @param id the id of the insuranceDTO to save.
     * @param insuranceDTO the insuranceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceDTO,
     * or with status {@code 400 (Bad Request)} if the insuranceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/insurances/{id}")
    public ResponseEntity<InsuranceDTO> updateInsurance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceDTO insuranceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Insurance : {}, {}", id, insuranceDTO);
        if (insuranceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InsuranceDTO result = insuranceService.update(insuranceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /insurances/:id} : Partial updates given fields of an existing insurance, field will ignore if it is null
     *
     * @param id the id of the insuranceDTO to save.
     * @param insuranceDTO the insuranceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceDTO,
     * or with status {@code 400 (Bad Request)} if the insuranceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/insurances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceDTO> partialUpdateInsurance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceDTO insuranceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Insurance partially : {}, {}", id, insuranceDTO);
        if (insuranceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceDTO> result = insuranceService.partialUpdate(insuranceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /insurances} : get all the insurances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insurances in body.
     */
    @GetMapping("/insurances")
    public ResponseEntity<List<InsuranceDTO>> getAllInsurances(
        InsuranceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Insurances by criteria: {}", criteria);
        Page<InsuranceDTO> page = insuranceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurances/count} : count all the insurances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/insurances/count")
    public ResponseEntity<Long> countInsurances(InsuranceCriteria criteria) {
        log.debug("REST request to count Insurances by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurances/:id} : get the "id" insurance.
     *
     * @param id the id of the insuranceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/insurances/{id}")
    public ResponseEntity<InsuranceDTO> getInsurance(@PathVariable Long id) {
        log.debug("REST request to get Insurance : {}", id);
        Optional<InsuranceDTO> insuranceDTO = insuranceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceDTO);
    }

    /**
     * {@code DELETE  /insurances/:id} : delete the "id" insurance.
     *
     * @param id the id of the insuranceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/insurances/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        log.debug("REST request to delete Insurance : {}", id);
        insuranceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
