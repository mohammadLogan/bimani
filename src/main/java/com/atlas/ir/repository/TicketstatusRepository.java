package com.atlas.ir.repository;

import com.atlas.ir.domain.Ticketstatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ticketstatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketstatusRepository extends JpaRepository<Ticketstatus, Long>, JpaSpecificationExecutor<Ticketstatus> {}
