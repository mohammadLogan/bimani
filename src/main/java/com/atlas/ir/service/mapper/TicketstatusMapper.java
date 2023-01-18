package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Ticketstatus;
import com.atlas.ir.service.dto.TicketstatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ticketstatus} and its DTO {@link TicketstatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketstatusMapper extends EntityMapper<TicketstatusDTO, Ticketstatus> {}
