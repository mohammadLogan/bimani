package com.atlas.ir.repository;

import com.atlas.ir.domain.Cau20status;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cau20status entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Cau20statusRepository extends JpaRepository<Cau20status, Long>, JpaSpecificationExecutor<Cau20status> {}
