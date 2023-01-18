package com.atlas.ir.service;

import com.atlas.ir.service.dto.Cau20statusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Cau20status}.
 */
public interface Cau20statusService {
    /**
     * Save a cau20status.
     *
     * @param cau20statusDTO the entity to save.
     * @return the persisted entity.
     */
    Cau20statusDTO save(Cau20statusDTO cau20statusDTO);

    /**
     * Updates a cau20status.
     *
     * @param cau20statusDTO the entity to update.
     * @return the persisted entity.
     */
    Cau20statusDTO update(Cau20statusDTO cau20statusDTO);

    /**
     * Partially updates a cau20status.
     *
     * @param cau20statusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cau20statusDTO> partialUpdate(Cau20statusDTO cau20statusDTO);

    /**
     * Get all the cau20statuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cau20statusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cau20status.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cau20statusDTO> findOne(Long id);

    /**
     * Delete the "id" cau20status.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
