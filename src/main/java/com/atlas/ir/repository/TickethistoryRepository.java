package com.atlas.ir.repository;

import com.atlas.ir.domain.Tickethistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tickethistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TickethistoryRepository extends JpaRepository<Tickethistory, Long>, JpaSpecificationExecutor<Tickethistory> {}
