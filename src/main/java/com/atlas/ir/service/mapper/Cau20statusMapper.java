package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Cau20status;
import com.atlas.ir.service.dto.Cau20statusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cau20status} and its DTO {@link Cau20statusDTO}.
 */
@Mapper(componentModel = "spring")
public interface Cau20statusMapper extends EntityMapper<Cau20statusDTO, Cau20status> {}
