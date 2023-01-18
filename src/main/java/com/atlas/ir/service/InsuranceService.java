package com.atlas.ir.service;

import com.atlas.ir.service.dto.InsuranceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Insurance}.
 */
public interface InsuranceService {
    /**
     * Save a insurance.
     *
     * @param insuranceDTO the entity to save.
     * @return the persisted entity.
     */
    InsuranceDTO save(InsuranceDTO insuranceDTO);

    /**
     * Updates a insurance.
     *
     * @param insuranceDTO the entity to update.
     * @return the persisted entity.
     */
    InsuranceDTO update(InsuranceDTO insuranceDTO);

    /**
     * Partially updates a insurance.
     *
     * @param insuranceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InsuranceDTO> partialUpdate(InsuranceDTO insuranceDTO);

    /**
     * Get all the insurances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InsuranceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" insurance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InsuranceDTO> findOne(Long id);

    /**
     * Delete the "id" insurance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
