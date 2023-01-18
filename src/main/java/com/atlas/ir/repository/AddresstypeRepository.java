package com.atlas.ir.repository;

import com.atlas.ir.domain.Addresstype;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Addresstype entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddresstypeRepository extends JpaRepository<Addresstype, Long>, JpaSpecificationExecutor<Addresstype> {}
