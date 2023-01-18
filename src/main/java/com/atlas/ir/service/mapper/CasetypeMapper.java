package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Casetype;
import com.atlas.ir.service.dto.CasetypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Casetype} and its DTO {@link CasetypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CasetypeMapper extends EntityMapper<CasetypeDTO, Casetype> {}
