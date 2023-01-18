package com.atlas.ir.service;

import com.atlas.ir.service.dto.RepairshopDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.atlas.ir.domain.Repairshop}.
 */
public interface RepairshopService {
    /**
     * Save a repairshop.
     *
     * @param repairshopDTO the entity to save.
     * @return the persisted entity.
     */
    RepairshopDTO save(RepairshopDTO repairshopDTO);

    /**
     * Updates a repairshop.
     *
     * @param repairshopDTO the entity to update.
     * @return the persisted entity.
     */
    RepairshopDTO update(RepairshopDTO repairshopDTO);

    /**
     * Partially updates a repairshop.
     *
     * @param repairshopDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RepairshopDTO> partialUpdate(RepairshopDTO repairshopDTO);

    /**
     * Get all the repairshops.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RepairshopDTO> findAll(Pageable pageable);

    /**
     * Get the "id" repairshop.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RepairshopDTO> findOne(Long id);

    /**
     * Delete the "id" repairshop.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
