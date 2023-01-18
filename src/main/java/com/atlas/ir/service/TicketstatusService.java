package com.atlas.ir.service;

import com.atlas.ir.service.dto.TicketstatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Ticketstatus}.
 */
public interface TicketstatusService {
    /**
     * Save a ticketstatus.
     *
     * @param ticketstatusDTO the entity to save.
     * @return the persisted entity.
     */
    TicketstatusDTO save(TicketstatusDTO ticketstatusDTO);

    /**
     * Updates a ticketstatus.
     *
     * @param ticketstatusDTO the entity to update.
     * @return the persisted entity.
     */
    TicketstatusDTO update(TicketstatusDTO ticketstatusDTO);

    /**
     * Partially updates a ticketstatus.
     *
     * @param ticketstatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TicketstatusDTO> partialUpdate(TicketstatusDTO ticketstatusDTO);

    /**
     * Get all the ticketstatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TicketstatusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ticketstatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TicketstatusDTO> findOne(Long id);

    /**
     * Delete the "id" ticketstatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
