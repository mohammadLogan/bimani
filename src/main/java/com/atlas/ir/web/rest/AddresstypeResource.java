package com.atlas.ir.web.rest;

import com.atlas.ir.repository.AddresstypeRepository;
import com.atlas.ir.service.AddresstypeQueryService;
import com.atlas.ir.service.AddresstypeService;
import com.atlas.ir.service.criteria.AddresstypeCriteria;
import com.atlas.ir.service.dto.AddresstypeDTO;
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
 * REST controller for managing {@link com.atlas.ir.domain.Addresstype}.
 */
@RestController
@RequestMapping("/api")
public class AddresstypeResource {

    private final Logger log = LoggerFactory.getLogger(AddresstypeResource.class);

    private static final String ENTITY_NAME = "callCenterBimaniAddresstype";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AddresstypeService addresstypeService;

    private final AddresstypeRepository addresstypeRepository;

    private final AddresstypeQueryService addresstypeQueryService;

    public AddresstypeResource(
        AddresstypeService addresstypeService,
        AddresstypeRepository addresstypeRepository,
        AddresstypeQueryService addresstypeQueryService
    ) {
        this.addresstypeService = addresstypeService;
        this.addresstypeRepository = addresstypeRepository;
        this.addresstypeQueryService = addresstypeQueryService;
    }

    /**
     * {@code POST  /addresstypes} : Create a new addresstype.
     *
     * @param addresstypeDTO the addresstypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new addresstypeDTO, or with status {@code 400 (Bad Request)} if the addresstype has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/addresstypes")
    public ResponseEntity<AddresstypeDTO> createAddresstype(@RequestBody AddresstypeDTO addresstypeDTO) throws URISyntaxException {
        log.debug("REST request to save Addresstype : {}", addresstypeDTO);
        if (addresstypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new addresstype cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AddresstypeDTO result = addresstypeService.save(addresstypeDTO);
        return ResponseEntity
            .created(new URI("/api/addresstypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /addresstypes/:id} : Updates an existing addresstype.
     *
     * @param id the id of the addresstypeDTO to save.
     * @param addresstypeDTO the addresstypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addresstypeDTO,
     * or with status {@code 400 (Bad Request)} if the addresstypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the addresstypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/addresstypes/{id}")
    public ResponseEntity<AddresstypeDTO> updateAddresstype(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AddresstypeDTO addresstypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Addresstype : {}, {}", id, addresstypeDTO);
        if (addresstypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addresstypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addresstypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AddresstypeDTO result = addresstypeService.update(addresstypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, addresstypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /addresstypes/:id} : Partial updates given fields of an existing addresstype, field will ignore if it is null
     *
     * @param id the id of the addresstypeDTO to save.
     * @param addresstypeDTO the addresstypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addresstypeDTO,
     * or with status {@code 400 (Bad Request)} if the addresstypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the addresstypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the addresstypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/addresstypes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AddresstypeDTO> partialUpdateAddresstype(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AddresstypeDTO addresstypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Addresstype partially : {}, {}", id, addresstypeDTO);
        if (addresstypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addresstypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addresstypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AddresstypeDTO> result = addresstypeService.partialUpdate(addresstypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, addresstypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /addresstypes} : get all the addresstypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of addresstypes in body.
     */
    @GetMapping("/addresstypes")
    public ResponseEntity<List<AddresstypeDTO>> getAllAddresstypes(
        AddresstypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Addresstypes by criteria: {}", criteria);
        Page<AddresstypeDTO> page = addresstypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /addresstypes/count} : count all the addresstypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/addresstypes/count")
    public ResponseEntity<Long> countAddresstypes(AddresstypeCriteria criteria) {
        log.debug("REST request to count Addresstypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(addresstypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /addresstypes/:id} : get the "id" addresstype.
     *
     * @param id the id of the addresstypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the addresstypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/addresstypes/{id}")
    public ResponseEntity<AddresstypeDTO> getAddresstype(@PathVariable Long id) {
        log.debug("REST request to get Addresstype : {}", id);
        Optional<AddresstypeDTO> addresstypeDTO = addresstypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(addresstypeDTO);
    }

    /**
     * {@code DELETE  /addresstypes/:id} : delete the "id" addresstype.
     *
     * @param id the id of the addresstypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/addresstypes/{id}")
    public ResponseEntity<Void> deleteAddresstype(@PathVariable Long id) {
        log.debug("REST request to delete Addresstype : {}", id);
        addresstypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
