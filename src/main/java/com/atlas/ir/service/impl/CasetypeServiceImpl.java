package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Casetype;
import com.atlas.ir.repository.CasetypeRepository;
import com.atlas.ir.service.CasetypeService;
import com.atlas.ir.service.dto.CasetypeDTO;
import com.atlas.ir.service.mapper.CasetypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Casetype}.
 */
@Service
@Transactional
public class CasetypeServiceImpl implements CasetypeService {

    private final Logger log = LoggerFactory.getLogger(CasetypeServiceImpl.class);

    private final CasetypeRepository casetypeRepository;

    private final CasetypeMapper casetypeMapper;

    public CasetypeServiceImpl(CasetypeRepository casetypeRepository, CasetypeMapper casetypeMapper) {
        this.casetypeRepository = casetypeRepository;
        this.casetypeMapper = casetypeMapper;
    }

    @Override
    public CasetypeDTO save(CasetypeDTO casetypeDTO) {
        log.debug("Request to save Casetype : {}", casetypeDTO);
        Casetype casetype = casetypeMapper.toEntity(casetypeDTO);
        casetype = casetypeRepository.save(casetype);
        return casetypeMapper.toDto(casetype);
    }

    @Override
    public CasetypeDTO update(CasetypeDTO casetypeDTO) {
        log.debug("Request to update Casetype : {}", casetypeDTO);
        Casetype casetype = casetypeMapper.toEntity(casetypeDTO);
        casetype = casetypeRepository.save(casetype);
        return casetypeMapper.toDto(casetype);
    }

    @Override
    public Optional<CasetypeDTO> partialUpdate(CasetypeDTO casetypeDTO) {
        log.debug("Request to partially update Casetype : {}", casetypeDTO);

        return casetypeRepository
            .findById(casetypeDTO.getId())
            .map(existingCasetype -> {
                casetypeMapper.partialUpdate(existingCasetype, casetypeDTO);

                return existingCasetype;
            })
            .map(casetypeRepository::save)
            .map(casetypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CasetypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Casetypes");
        return casetypeRepository.findAll(pageable).map(casetypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CasetypeDTO> findOne(Long id) {
        log.debug("Request to get Casetype : {}", id);
        return casetypeRepository.findById(id).map(casetypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Casetype : {}", id);
        casetypeRepository.deleteById(id);
    }
}
