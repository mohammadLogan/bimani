package com.atlas.ir.repository;

import com.atlas.ir.domain.Casetype;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Casetype entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CasetypeRepository extends JpaRepository<Casetype, Long>, JpaSpecificationExecutor<Casetype> {}
