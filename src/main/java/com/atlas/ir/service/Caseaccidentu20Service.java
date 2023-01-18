package com.atlas.ir.service;

import com.atlas.ir.service.dto.Caseaccidentu20DTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Caseaccidentu20}.
 */
public interface Caseaccidentu20Service {
    /**
     * Save a caseaccidentu20.
     *
     * @param caseaccidentu20DTO the entity to save.
     * @return the persisted entity.
     */
    Caseaccidentu20DTO save(Caseaccidentu20DTO caseaccidentu20DTO);

    /**
     * Updates a caseaccidentu20.
     *
     * @param caseaccidentu20DTO the entity to update.
     * @return the persisted entity.
     */
    Caseaccidentu20DTO update(Caseaccidentu20DTO caseaccidentu20DTO);

    /**
     * Partially updates a caseaccidentu20.
     *
     * @param caseaccidentu20DTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Caseaccidentu20DTO> partialUpdate(Caseaccidentu20DTO caseaccidentu20DTO);

    /**
     * Get all the caseaccidentu20s.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Caseaccidentu20DTO> findAll(Pageable pageable);

    /**
     * Get the "id" caseaccidentu20.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Caseaccidentu20DTO> findOne(Long id);

    /**
     * Delete the "id" caseaccidentu20.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
