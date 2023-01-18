package com.atlas.ir.repository;

import com.atlas.ir.domain.Insurancecase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Insurancecase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsurancecaseRepository extends JpaRepository<Insurancecase, Long>, JpaSpecificationExecutor<Insurancecase> {}
