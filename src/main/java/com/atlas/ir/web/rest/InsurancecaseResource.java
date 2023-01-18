package com.atlas.ir.web.rest;

import com.atlas.ir.repository.InsurancecaseRepository;
import com.atlas.ir.service.InsurancecaseQueryService;
import com.atlas.ir.service.InsurancecaseService;
import com.atlas.ir.service.criteria.InsurancecaseCriteria;
import com.atlas.ir.service.dto.InsurancecaseDTO;
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
 * REST controller for managing {@link com.atlas.ir.domain.Insurancecase}.
 */
@RestController
@RequestMapping("/api")
public class InsurancecaseResource {

    private final Logger log = LoggerFactory.getLogger(InsurancecaseResource.class);

    private static final String ENTITY_NAME = "callCenterBimaniInsurancecase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsurancecaseService insurancecaseService;

    private final InsurancecaseRepository insurancecaseRepository;

    private final InsurancecaseQueryService insurancecaseQueryService;

    public InsurancecaseResource(
        InsurancecaseService insurancecaseService,
        InsurancecaseRepository insurancecaseRepository,
        InsurancecaseQueryService insurancecaseQueryService
    ) {
        this.insurancecaseService = insurancecaseService;
        this.insurancecaseRepository = insurancecaseRepository;
        this.insurancecaseQueryService = insurancecaseQueryService;
    }

    /**
     * {@code POST  /insurancecases} : Create a new insurancecase.
     *
     * @param insurancecaseDTO the insurancecaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insurancecaseDTO, or with status {@code 400 (Bad Request)} if the insurancecase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/insurancecases")
    public ResponseEntity<InsurancecaseDTO> createInsurancecase(@RequestBody InsurancecaseDTO insurancecaseDTO) throws URISyntaxException {
        log.debug("REST request to save Insurancecase : {}", insurancecaseDTO);
        if (insurancecaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new insurancecase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InsurancecaseDTO result = insurancecaseService.save(insurancecaseDTO);
        return ResponseEntity
            .created(new URI("/api/insurancecases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /insurancecases/:id} : Updates an existing insurancecase.
     *
     * @param id the id of the insurancecaseDTO to save.
     * @param insurancecaseDTO the insurancecaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancecaseDTO,
     * or with status {@code 400 (Bad Request)} if the insurancecaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insurancecaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/insurancecases/{id}")
    public ResponseEntity<InsurancecaseDTO> updateInsurancecase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsurancecaseDTO insurancecaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Insurancecase : {}, {}", id, insurancecaseDTO);
        if (insurancecaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancecaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancecaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InsurancecaseDTO result = insurancecaseService.update(insurancecaseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insurancecaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /insurancecases/:id} : Partial updates given fields of an existing insurancecase, field will ignore if it is null
     *
     * @param id the id of the insurancecaseDTO to save.
     * @param insurancecaseDTO the insurancecaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancecaseDTO,
     * or with status {@code 400 (Bad Request)} if the insurancecaseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the insurancecaseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the insurancecaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/insurancecases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsurancecaseDTO> partialUpdateInsurancecase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsurancecaseDTO insurancecaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Insurancecase partially : {}, {}", id, insurancecaseDTO);
        if (insurancecaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancecaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancecaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsurancecaseDTO> result = insurancecaseService.partialUpdate(insurancecaseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insurancecaseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /insurancecases} : get all the insurancecases.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insurancecases in body.
     */
    @GetMapping("/insurancecases")
    public ResponseEntity<List<InsurancecaseDTO>> getAllInsurancecases(
        InsurancecaseCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Insurancecases by criteria: {}", criteria);
        Page<InsurancecaseDTO> page = insurancecaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurancecases/count} : count all the insurancecases.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/insurancecases/count")
    public ResponseEntity<Long> countInsurancecases(InsurancecaseCriteria criteria) {
        log.debug("REST request to count Insurancecases by criteria: {}", criteria);
        return ResponseEntity.ok().body(insurancecaseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurancecases/:id} : get the "id" insurancecase.
     *
     * @param id the id of the insurancecaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insurancecaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/insurancecases/{id}")
    public ResponseEntity<InsurancecaseDTO> getInsurancecase(@PathVariable Long id) {
        log.debug("REST request to get Insurancecase : {}", id);
        Optional<InsurancecaseDTO> insurancecaseDTO = insurancecaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insurancecaseDTO);
    }

    /**
     * {@code DELETE  /insurancecases/:id} : delete the "id" insurancecase.
     *
     * @param id the id of the insurancecaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/insurancecases/{id}")
    public ResponseEntity<Void> deleteInsurancecase(@PathVariable Long id) {
        log.debug("REST request to delete Insurancecase : {}", id);
        insurancecaseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
