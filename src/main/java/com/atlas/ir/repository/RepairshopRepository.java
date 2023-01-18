package com.atlas.ir.repository;

import com.atlas.ir.domain.Repairshop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Repairshop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepairshopRepository extends JpaRepository<Repairshop, Long>, JpaSpecificationExecutor<Repairshop> {}
