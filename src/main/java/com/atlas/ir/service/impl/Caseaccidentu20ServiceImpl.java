package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Caseaccidentu20;
import com.atlas.ir.repository.Caseaccidentu20Repository;
import com.atlas.ir.service.Caseaccidentu20Service;
import com.atlas.ir.service.dto.Caseaccidentu20DTO;
import com.atlas.ir.service.mapper.Caseaccidentu20Mapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Caseaccidentu20}.
 */
@Service
@Transactional
public class Caseaccidentu20ServiceImpl implements Caseaccidentu20Service {

    private final Logger log = LoggerFactory.getLogger(Caseaccidentu20ServiceImpl.class);

    private final Caseaccidentu20Repository caseaccidentu20Repository;

    private final Caseaccidentu20Mapper caseaccidentu20Mapper;

    public Caseaccidentu20ServiceImpl(Caseaccidentu20Repository caseaccidentu20Repository, Caseaccidentu20Mapper caseaccidentu20Mapper) {
        this.caseaccidentu20Repository = caseaccidentu20Repository;
        this.caseaccidentu20Mapper = caseaccidentu20Mapper;
    }

    @Override
    public Caseaccidentu20DTO save(Caseaccidentu20DTO caseaccidentu20DTO) {
        log.debug("Request to save Caseaccidentu20 : {}", caseaccidentu20DTO);
        Caseaccidentu20 caseaccidentu20 = caseaccidentu20Mapper.toEntity(caseaccidentu20DTO);
        caseaccidentu20 = caseaccidentu20Repository.save(caseaccidentu20);
        return caseaccidentu20Mapper.toDto(caseaccidentu20);
    }

    @Override
    public Caseaccidentu20DTO update(Caseaccidentu20DTO caseaccidentu20DTO) {
        log.debug("Request to update Caseaccidentu20 : {}", caseaccidentu20DTO);
        Caseaccidentu20 caseaccidentu20 = caseaccidentu20Mapper.toEntity(caseaccidentu20DTO);
        caseaccidentu20 = caseaccidentu20Repository.save(caseaccidentu20);
        return caseaccidentu20Mapper.toDto(caseaccidentu20);
    }

    @Override
    public Optional<Caseaccidentu20DTO> partialUpdate(Caseaccidentu20DTO caseaccidentu20DTO) {
        log.debug("Request to partially update Caseaccidentu20 : {}", caseaccidentu20DTO);

        return caseaccidentu20Repository
            .findById(caseaccidentu20DTO.getId())
            .map(existingCaseaccidentu20 -> {
                caseaccidentu20Mapper.partialUpdate(existingCaseaccidentu20, caseaccidentu20DTO);

                return existingCaseaccidentu20;
            })
            .map(caseaccidentu20Repository::save)
            .map(caseaccidentu20Mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Caseaccidentu20DTO> findAll(Pageable pageable) {
        log.debug("Request to get all Caseaccidentu20s");
        return caseaccidentu20Repository.findAll(pageable).map(caseaccidentu20Mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Caseaccidentu20DTO> findOne(Long id) {
        log.debug("Request to get Caseaccidentu20 : {}", id);
        return caseaccidentu20Repository.findById(id).map(caseaccidentu20Mapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Caseaccidentu20 : {}", id);
        caseaccidentu20Repository.deleteById(id);
    }
}
