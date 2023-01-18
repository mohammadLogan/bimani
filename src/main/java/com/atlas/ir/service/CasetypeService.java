package com.atlas.ir.service;

import com.atlas.ir.service.dto.CasetypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Casetype}.
 */
public interface CasetypeService {
    /**
     * Save a casetype.
     *
     * @param casetypeDTO the entity to save.
     * @return the persisted entity.
     */
    CasetypeDTO save(CasetypeDTO casetypeDTO);

    /**
     * Updates a casetype.
     *
     * @param casetypeDTO the entity to update.
     * @return the persisted entity.
     */
    CasetypeDTO update(CasetypeDTO casetypeDTO);

    /**
     * Partially updates a casetype.
     *
     * @param casetypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CasetypeDTO> partialUpdate(CasetypeDTO casetypeDTO);

    /**
     * Get all the casetypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CasetypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" casetype.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CasetypeDTO> findOne(Long id);

    /**
     * Delete the "id" casetype.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
