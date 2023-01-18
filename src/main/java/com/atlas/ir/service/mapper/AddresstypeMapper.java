package com.atlas.ir.service.mapper;

import com.atlas.ir.domain.Addresstype;
import com.atlas.ir.service.dto.AddresstypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Addresstype} and its DTO {@link AddresstypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface AddresstypeMapper extends EntityMapper<AddresstypeDTO, Addresstype> {}
