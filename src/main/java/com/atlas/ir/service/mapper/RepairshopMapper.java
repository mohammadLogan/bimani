package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Repairshop;
import com.atlas.ir.service.dto.RepairshopDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Repairshop} and its DTO {@link RepairshopDTO}.
 */
@Mapper(componentModel = "spring")
public interface RepairshopMapper extends EntityMapper<RepairshopDTO, Repairshop> {}
