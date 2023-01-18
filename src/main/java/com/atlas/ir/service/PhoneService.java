package com.atlas.ir.service;

import com.atlas.ir.service.dto.PhoneDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Phone}.
 */
public interface PhoneService {
    /**
     * Save a phone.
     *
     * @param phoneDTO the entity to save.
     * @return the persisted entity.
     */
    PhoneDTO save(PhoneDTO phoneDTO);

    /**
     * Updates a phone.
     *
     * @param phoneDTO the entity to update.
     * @return the persisted entity.
     */
    PhoneDTO update(PhoneDTO phoneDTO);

    /**
     * Partially updates a phone.
     *
     * @param phoneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PhoneDTO> partialUpdate(PhoneDTO phoneDTO);

    /**
     * Get all the phones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PhoneDTO> findAll(Pageable pageable);

    /**
     * Get the "id" phone.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PhoneDTO> findOne(Long id);

    /**
     * Delete the "id" phone.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
