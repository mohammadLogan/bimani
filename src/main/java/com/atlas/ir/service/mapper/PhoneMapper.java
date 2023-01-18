package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Phone;
import com.atlas.ir.service.dto.PhoneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Phone} and its DTO {@link PhoneDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhoneMapper extends EntityMapper<PhoneDTO, Phone> {}
