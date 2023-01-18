package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Insurancecase;
import com.atlas.ir.service.dto.InsurancecaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Insurancecase} and its DTO {@link InsurancecaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface InsurancecaseMapper extends EntityMapper<InsurancecaseDTO, Insurancecase> {}
