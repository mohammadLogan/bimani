package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Insurance;
import com.atlas.ir.service.dto.InsuranceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Insurance} and its DTO {@link InsuranceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InsuranceMapper extends EntityMapper<InsuranceDTO, Insurance> {}
