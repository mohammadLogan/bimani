package com.atlas.ir.web.rest;

import com.atlas.ir.repository.TickethistoryRepository;
import com.atlas.ir.service.TickethistoryQueryService;
import com.atlas.ir.service.TickethistoryService;
import com.atlas.ir.service.criteria.TickethistoryCriteria;
import com.atlas.ir.service.dto.TickethistoryDTO;
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
 * REST controller for managing {@link com.atlas.ir.domain.Tickethistory}.
 */
@RestController
@RequestMapping("/api")
public class TickethistoryResource {

    private final Logger log = LoggerFactory.getLogger(TickethistoryResource.class);

    private static final String ENTITY_NAME = "callCenterBimaniTickethistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TickethistoryService tickethistoryService;

    private final TickethistoryRepository tickethistoryRepository;

    private final TickethistoryQueryService tickethistoryQueryService;

    public TickethistoryResource(
        TickethistoryService tickethistoryService,
        TickethistoryRepository tickethistoryRepository,
        TickethistoryQueryService tickethistoryQueryService
    ) {
        this.tickethistoryService = tickethistoryService;
        this.tickethistoryRepository = tickethistoryRepository;
        this.tickethistoryQueryService = tickethistoryQueryService;
    }

    /**
     * {@code POST  /tickethistories} : Create a new tickethistory.
     *
     * @param tickethistoryDTO the tickethistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tickethistoryDTO, or with status {@code 400 (Bad Request)} if the tickethistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickethistories")
    public ResponseEntity<TickethistoryDTO> createTickethistory(@RequestBody TickethistoryDTO tickethistoryDTO) throws URISyntaxException {
        log.debug("REST request to save Tickethistory : {}", tickethistoryDTO);
        if (tickethistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new tickethistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TickethistoryDTO result = tickethistoryService.save(tickethistoryDTO);
        return ResponseEntity
            .created(new URI("/api/tickethistories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickethistories/:id} : Updates an existing tickethistory.
     *
     * @param id the id of the tickethistoryDTO to save.
     * @param tickethistoryDTO the tickethistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickethistoryDTO,
     * or with status {@code 400 (Bad Request)} if the tickethistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tickethistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tickethistories/{id}")
    public ResponseEntity<TickethistoryDTO> updateTickethistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TickethistoryDTO tickethistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tickethistory : {}, {}", id, tickethistoryDTO);
        if (tickethistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickethistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickethistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TickethistoryDTO result = tickethistoryService.update(tickethistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tickethistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tickethistories/:id} : Partial updates given fields of an existing tickethistory, field will ignore if it is null
     *
     * @param id the id of the tickethistoryDTO to save.
     * @param tickethistoryDTO the tickethistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickethistoryDTO,
     * or with status {@code 400 (Bad Request)} if the tickethistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tickethistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tickethistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tickethistories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TickethistoryDTO> partialUpdateTickethistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TickethistoryDTO tickethistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tickethistory partially : {}, {}", id, tickethistoryDTO);
        if (tickethistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickethistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickethistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TickethistoryDTO> result = tickethistoryService.partialUpdate(tickethistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tickethistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tickethistories} : get all the tickethistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickethistories in body.
     */
    @GetMapping("/tickethistories")
    public ResponseEntity<List<TickethistoryDTO>> getAllTickethistories(
        TickethistoryCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Tickethistories by criteria: {}", criteria);
        Page<TickethistoryDTO> page = tickethistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tickethistories/count} : count all the tickethistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tickethistories/count")
    public ResponseEntity<Long> countTickethistories(TickethistoryCriteria criteria) {
        log.debug("REST request to count Tickethistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(tickethistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tickethistories/:id} : get the "id" tickethistory.
     *
     * @param id the id of the tickethistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tickethistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickethistories/{id}")
    public ResponseEntity<TickethistoryDTO> getTickethistory(@PathVariable Long id) {
        log.debug("REST request to get Tickethistory : {}", id);
        Optional<TickethistoryDTO> tickethistoryDTO = tickethistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tickethistoryDTO);
    }

    /**
     * {@code DELETE  /tickethistories/:id} : delete the "id" tickethistory.
     *
     * @param id the id of the tickethistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tickethistories/{id}")
    public ResponseEntity<Void> deleteTickethistory(@PathVariable Long id) {
        log.debug("REST request to delete Tickethistory : {}", id);
        tickethistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
