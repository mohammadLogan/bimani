package com.atlas.ir.service;

import com.atlas.ir.service.dto.TickethistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Tickethistory}.
 */
public interface TickethistoryService {
    /**
     * Save a tickethistory.
     *
     * @param tickethistoryDTO the entity to save.
     * @return the persisted entity.
     */
    TickethistoryDTO save(TickethistoryDTO tickethistoryDTO);

    /**
     * Updates a tickethistory.
     *
     * @param tickethistoryDTO the entity to update.
     * @return the persisted entity.
     */
    TickethistoryDTO update(TickethistoryDTO tickethistoryDTO);

    /**
     * Partially updates a tickethistory.
     *
     * @param tickethistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TickethistoryDTO> partialUpdate(TickethistoryDTO tickethistoryDTO);

    /**
     * Get all the tickethistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TickethistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tickethistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TickethistoryDTO> findOne(Long id);

    /**
     * Delete the "id" tickethistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
