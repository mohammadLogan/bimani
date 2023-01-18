package com.atlas.ir.service;

import com.atlas.ir.service.dto.AddresstypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Addresstype}.
 */
public interface AddresstypeService {
    /**
     * Save a addresstype.
     *
     * @param addresstypeDTO the entity to save.
     * @return the persisted entity.
     */
    AddresstypeDTO save(AddresstypeDTO addresstypeDTO);

    /**
     * Updates a addresstype.
     *
     * @param addresstypeDTO the entity to update.
     * @return the persisted entity.
     */
    AddresstypeDTO update(AddresstypeDTO addresstypeDTO);

    /**
     * Partially updates a addresstype.
     *
     * @param addresstypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AddresstypeDTO> partialUpdate(AddresstypeDTO addresstypeDTO);

    /**
     * Get all the addresstypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AddresstypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" addresstype.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AddresstypeDTO> findOne(Long id);

    /**
     * Delete the "id" addresstype.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
