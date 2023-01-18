package com.atlas.ir.repository;

import com.atlas.ir.domain.Caseaccidentu20;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Caseaccidentu20 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Caseaccidentu20Repository extends JpaRepository<Caseaccidentu20, Long>, JpaSpecificationExecutor<Caseaccidentu20> {}
