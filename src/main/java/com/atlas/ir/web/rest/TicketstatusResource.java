package com.atlas.ir.web.rest;

import com.atlas.ir.repository.TicketstatusRepository;
import com.atlas.ir.service.TicketstatusQueryService;
import com.atlas.ir.service.TicketstatusService;
import com.atlas.ir.service.criteria.TicketstatusCriteria;
import com.atlas.ir.service.dto.TicketstatusDTO;
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
 * REST controller for managing {@link com.atlas.ir.domain.Ticketstatus}.
 */
@RestController
@RequestMapping("/api")
public class TicketstatusResource {

    private final Logger log = LoggerFactory.getLogger(TicketstatusResource.class);

    private static final String ENTITY_NAME = "callCenterBimaniTicketstatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketstatusService ticketstatusService;

    private final TicketstatusRepository ticketstatusRepository;

    private final TicketstatusQueryService ticketstatusQueryService;

    public TicketstatusResource(
        TicketstatusService ticketstatusService,
        TicketstatusRepository ticketstatusRepository,
        TicketstatusQueryService ticketstatusQueryService
    ) {
        this.ticketstatusService = ticketstatusService;
        this.ticketstatusRepository = ticketstatusRepository;
        this.ticketstatusQueryService = ticketstatusQueryService;
    }

    /**
     * {@code POST  /ticketstatuses} : Create a new ticketstatus.
     *
     * @param ticketstatusDTO the ticketstatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketstatusDTO, or with status {@code 400 (Bad Request)} if the ticketstatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ticketstatuses")
    public ResponseEntity<TicketstatusDTO> createTicketstatus(@RequestBody TicketstatusDTO ticketstatusDTO) throws URISyntaxException {
        log.debug("REST request to save Ticketstatus : {}", ticketstatusDTO);
        if (ticketstatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketstatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketstatusDTO result = ticketstatusService.save(ticketstatusDTO);
        return ResponseEntity
            .created(new URI("/api/ticketstatuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ticketstatuses/:id} : Updates an existing ticketstatus.
     *
     * @param id the id of the ticketstatusDTO to save.
     * @param ticketstatusDTO the ticketstatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketstatusDTO,
     * or with status {@code 400 (Bad Request)} if the ticketstatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketstatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ticketstatuses/{id}")
    public ResponseEntity<TicketstatusDTO> updateTicketstatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TicketstatusDTO ticketstatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ticketstatus : {}, {}", id, ticketstatusDTO);
        if (ticketstatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketstatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketstatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TicketstatusDTO result = ticketstatusService.update(ticketstatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ticketstatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ticketstatuses/:id} : Partial updates given fields of an existing ticketstatus, field will ignore if it is null
     *
     * @param id the id of the ticketstatusDTO to save.
     * @param ticketstatusDTO the ticketstatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketstatusDTO,
     * or with status {@code 400 (Bad Request)} if the ticketstatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketstatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketstatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ticketstatuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TicketstatusDTO> partialUpdateTicketstatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TicketstatusDTO ticketstatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ticketstatus partially : {}, {}", id, ticketstatusDTO);
        if (ticketstatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketstatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketstatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TicketstatusDTO> result = ticketstatusService.partialUpdate(ticketstatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ticketstatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ticketstatuses} : get all the ticketstatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketstatuses in body.
     */
    @GetMapping("/ticketstatuses")
    public ResponseEntity<List<TicketstatusDTO>> getAllTicketstatuses(
        TicketstatusCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Ticketstatuses by criteria: {}", criteria);
        Page<TicketstatusDTO> page = ticketstatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ticketstatuses/count} : count all the ticketstatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ticketstatuses/count")
    public ResponseEntity<Long> countTicketstatuses(TicketstatusCriteria criteria) {
        log.debug("REST request to count Ticketstatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(ticketstatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ticketstatuses/:id} : get the "id" ticketstatus.
     *
     * @param id the id of the ticketstatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketstatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ticketstatuses/{id}")
    public ResponseEntity<TicketstatusDTO> getTicketstatus(@PathVariable Long id) {
        log.debug("REST request to get Ticketstatus : {}", id);
        Optional<TicketstatusDTO> ticketstatusDTO = ticketstatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketstatusDTO);
    }

    /**
     * {@code DELETE  /ticketstatuses/:id} : delete the "id" ticketstatus.
     *
     * @param id the id of the ticketstatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ticketstatuses/{id}")
    public ResponseEntity<Void> deleteTicketstatus(@PathVariable Long id) {
        log.debug("REST request to delete Ticketstatus : {}", id);
        ticketstatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
