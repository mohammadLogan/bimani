package com.atlas.ir.service;

import com.atlas.ir.service.dto.InsurancecaseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Insurancecase}.
 */
public interface InsurancecaseService {
    /**
     * Save a insurancecase.
     *
     * @param insurancecaseDTO the entity to save.
     * @return the persisted entity.
     */
    InsurancecaseDTO save(InsurancecaseDTO insurancecaseDTO);

    /**
     * Updates a insurancecase.
     *
     * @param insurancecaseDTO the entity to update.
     * @return the persisted entity.
     */
    InsurancecaseDTO update(InsurancecaseDTO insurancecaseDTO);

    /**
     * Partially updates a insurancecase.
     *
     * @param insurancecaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InsurancecaseDTO> partialUpdate(InsurancecaseDTO insurancecaseDTO);

    /**
     * Get all the insurancecases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InsurancecaseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" insurancecase.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InsurancecaseDTO> findOne(Long id);

    /**
     * Delete the "id" insurancecase.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
