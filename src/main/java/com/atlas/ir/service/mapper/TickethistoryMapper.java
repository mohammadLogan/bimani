package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Tickethistory;
import com.atlas.ir.service.dto.TickethistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tickethistory} and its DTO {@link TickethistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface TickethistoryMapper extends EntityMapper<TickethistoryDTO, Tickethistory> {}
